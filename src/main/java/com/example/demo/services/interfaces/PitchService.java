package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.dtos.request.PitchRequest;
import com.example.demo.dtos.response.PitchResponse;

public interface PitchService {
    List<PitchResponse> getAllPitches();
    List<PitchResponse> getPitchesByComplex(Integer complexId);
    PitchResponse getPitchById(Integer id);
    PitchResponse createPitch(PitchRequest request);
    PitchResponse updatePitch(Integer id, PitchRequest request);
    void deletePitch(Integer id);
}
