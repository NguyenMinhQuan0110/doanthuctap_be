package com.example.demo.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.PitchGroupRequest;
import com.example.demo.dtos.response.PitchGroupResponse;
import com.example.demo.entites.Complex;
import com.example.demo.entites.Pitch;
import com.example.demo.entites.PitchGroup;
import com.example.demo.entites.enums.PitchStatus;
import com.example.demo.repositories.ComplexRepository;
import com.example.demo.repositories.PitchGroupRepository;
import com.example.demo.repositories.PitchRepository;
import com.example.demo.services.interfaces.PitchGroupService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PitchGroupServiceImpl implements PitchGroupService{
    @Autowired
    private PitchGroupRepository pitchGroupRepository;

    @Autowired
    private ComplexRepository complexRepository;

    @Autowired
    private PitchRepository pitchRepository;

    @Override
    public List<PitchGroupResponse> getAllPitchGroups() {
        return pitchGroupRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PitchGroupResponse> getPitchGroupsByComplex(Integer complexId) {
        return pitchGroupRepository.findByComplex_ComplexId(complexId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PitchGroupResponse getPitchGroupById(Integer id) {
        PitchGroup group = pitchGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch group not found"));
        return mapToResponse(group);
    }

    @Override
    public PitchGroupResponse createPitchGroup(PitchGroupRequest request) {
        Complex complex = complexRepository.findById(request.getComplexId())
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        if (pitchGroupRepository.existsByNameAndComplex(request.getName(), complex)) {
            throw new RuntimeException("Pitch group name already exists in this complex");
        }

        PitchGroup group = new PitchGroup();
        group.setName(request.getName());
        group.setType(request.getType());
        group.setPricePerHour(request.getPricePerHour());
        group.setComplex(complex);

        // ✅ Kiểm tra sân trùng nhóm
        if (request.getPitchIds() != null && !request.getPitchIds().isEmpty()) {
            Set<Pitch> pitches = request.getPitchIds().stream()
                    .map(id -> pitchRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Pitch not found with id: " + id)))
                    .collect(Collectors.toSet());

            // 🔍 Kiểm tra sân nào đã thuộc nhóm khác
            for (Pitch pitch : pitches) {
                if (!pitch.getGroups().isEmpty()) {
                    String existingGroupNames = pitch.getGroups().stream()
                            .map(PitchGroup::getName)
                            .collect(Collectors.joining(", "));
                    throw new RuntimeException(
                            "Pitch '" + pitch.getName() + "' already belongs to group(s): " + existingGroupNames);
                }
            }

            group.setPitches(pitches);
        }

        return mapToResponse(pitchGroupRepository.save(group));
    }

    @Override
    public PitchGroupResponse updatePitchGroup(Integer id, PitchGroupRequest request) {
        PitchGroup group = pitchGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch group not found"));

        Complex complex = complexRepository.findById(request.getComplexId())
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        if (!group.getName().equals(request.getName())
                && pitchGroupRepository.existsByNameAndComplex(request.getName(), complex)) {
            throw new RuntimeException("Pitch group name already exists in this complex");
        }

        group.setName(request.getName());
        group.setType(request.getType());
        group.setPricePerHour(request.getPricePerHour());
        group.setComplex(complex);

        if (request.getPitchIds() != null) {
            Set<Pitch> pitches = request.getPitchIds().stream()
                    .map(pitchId -> pitchRepository.findById(pitchId)
                            .orElseThrow(() -> new RuntimeException("Pitch not found with id: " + pitchId)))
                    .collect(Collectors.toSet());

            // 🔍 Kiểm tra trùng nhóm (ngoại trừ nhóm hiện tại)
            for (Pitch pitch : pitches) {
                boolean belongsToOtherGroup = pitch.getGroups().stream()
                        .anyMatch(g -> !g.getGroupId().equals(id));
                if (belongsToOtherGroup) {
                    String existingGroupNames = pitch.getGroups().stream()
                            .filter(g -> !g.getGroupId().equals(id))
                            .map(PitchGroup::getName)
                            .collect(Collectors.joining(", "));
                    throw new RuntimeException(
                            "Pitch '" + pitch.getName() + "' already belongs to group(s): " + existingGroupNames);
                }
            }

            group.setPitches(pitches);
        }

        return mapToResponse(pitchGroupRepository.save(group));
    }

    @Override
    public void deletePitchGroup(Integer id) {
        PitchGroup group = pitchGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch group not found"));
        pitchGroupRepository.delete(group);
    }

    @Override
    public PitchGroupResponse updateStatus(Integer id, String status) {
        PitchGroup pitchGroup = pitchGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch not found"));

        try {
            PitchStatus newStatus = PitchStatus.valueOf(status.toLowerCase());
            pitchGroup.setStatus(newStatus);
            return mapToResponse(pitchGroupRepository.save(pitchGroup));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }
    }
    
    private PitchGroupResponse mapToResponse(PitchGroup group) {
        PitchGroupResponse res = new PitchGroupResponse();
        res.setId(group.getGroupId());
        res.setName(group.getName());
        res.setType(group.getType());
        res.setPricePerHour(group.getPricePerHour());
        res.setStatus(group.getStatus());
        res.setComplexId(group.getComplex().getComplexId());
        res.setComplexName(group.getComplex().getName());
        res.setPitchIds(group.getPitches().stream()
                .map(Pitch::getPitchId)
                .collect(Collectors.toList()));
        res.setPitchNames(group.getPitches().stream()
                .map(Pitch::getName)
                .collect(Collectors.toList()));
        return res;
    }
}
