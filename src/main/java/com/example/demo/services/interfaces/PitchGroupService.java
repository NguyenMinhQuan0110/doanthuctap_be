package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.dtos.request.PitchGroupRequest;
import com.example.demo.dtos.response.PitchGroupResponse;

public interface PitchGroupService {
    List<PitchGroupResponse> getAllPitchGroups();
    List<PitchGroupResponse> getPitchGroupsByComplex(Integer complexId);
    PitchGroupResponse getPitchGroupById(Integer id);
    PitchGroupResponse createPitchGroup(PitchGroupRequest request);
    PitchGroupResponse updatePitchGroup(Integer id, PitchGroupRequest request);
    void deletePitchGroup(Integer id);
    PitchGroupResponse updateStatus(Integer id, String status);
}
