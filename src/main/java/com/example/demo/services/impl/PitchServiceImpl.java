package com.example.demo.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.request.PitchRequest;
import com.example.demo.dtos.response.PitchResponse;
import com.example.demo.entites.Complex;
import com.example.demo.entites.Pitch;
import com.example.demo.repositories.ComplexRepository;
import com.example.demo.repositories.PitchRepository;
import com.example.demo.services.interfaces.PitchService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PitchServiceImpl implements PitchService{
    @Autowired
    private PitchRepository pitchRepository;

    @Autowired
    private ComplexRepository complexRepository;

    @Override
    public List<PitchResponse> getAllPitches() {
        return pitchRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PitchResponse> getPitchesByComplex(Integer complexId) {
        return pitchRepository.findByComplex_ComplexId(complexId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PitchResponse getPitchById(Integer id) {
        Pitch pitch = pitchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch not found"));
        return mapToResponse(pitch);
    }

    @Override
    public PitchResponse createPitch(PitchRequest request) {
        Complex complex = complexRepository.findById(request.getComplexId())
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        if (pitchRepository.existsByNameAndComplex(request.getName(), complex)) {
            throw new RuntimeException("Pitch name already exists in this complex");
        }

        Pitch pitch = new Pitch();
        pitch.setName(request.getName());
        pitch.setType(request.getType());
        pitch.setPricePerHour(request.getPricePerHour());
        pitch.setDescription(request.getDescription());
        pitch.setComplex(complex);

        return mapToResponse(pitchRepository.save(pitch));
    }

    @Override
    public PitchResponse updatePitch(Integer id, PitchRequest request) {
        Pitch pitch = pitchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch not found"));

        Complex complex = complexRepository.findById(request.getComplexId())
                .orElseThrow(() -> new RuntimeException("Complex not found"));

        if (!pitch.getName().equals(request.getName())
                && pitchRepository.existsByNameAndComplex(request.getName(), complex)) {
            throw new RuntimeException("Pitch name already exists in this complex");
        }

        pitch.setName(request.getName());
        pitch.setType(request.getType());
        pitch.setPricePerHour(request.getPricePerHour());
        pitch.setDescription(request.getDescription());
        pitch.setComplex(complex);

        return mapToResponse(pitchRepository.save(pitch));
    }

    @Override
    public void deletePitch(Integer id) {
        Pitch pitch = pitchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pitch not found"));
        pitchRepository.delete(pitch);
    }

    private PitchResponse mapToResponse(Pitch pitch) {
        PitchResponse res = new PitchResponse();
        res.setId(pitch.getPitchId());
        res.setName(pitch.getName());
        res.setType(pitch.getType());
        res.setPricePerHour(pitch.getPricePerHour());
        res.setDescription(pitch.getDescription());
        res.setStatus(pitch.getStatus());
        res.setCreatedAt(pitch.getCreatedAt());
        res.setComplexId(pitch.getComplex().getComplexId());
        res.setComplexName(pitch.getComplex().getName());
        return res;
    }
}
