package com.example.demo.controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.PitchRequest;
import com.example.demo.dtos.response.PitchResponse;
import com.example.demo.services.interfaces.PitchService;

@RestController
@RequestMapping("/api/pitches")
public class PitchController {
    @Autowired
    private PitchService pitchService;

    @GetMapping
    public List<PitchResponse> getAllPitches() {
        return pitchService.getAllPitches();
    }

    @GetMapping("/{id}")
    public PitchResponse getPitchById(@PathVariable("id") Integer id) {
        return pitchService.getPitchById(id);
    }

    @GetMapping("/complex/{complexId}")
    public List<PitchResponse> getByComplex(@PathVariable("complexId") Integer complexId) {
        return pitchService.getPitchesByComplex(complexId);
    }

    @PostMapping("/create")
    public PitchResponse createPitch(@RequestBody PitchRequest request) {
        return pitchService.createPitch(request);
    }

    @PutMapping("/update/{id}")
    public PitchResponse updatePitch(@PathVariable("id") Integer id, @RequestBody PitchRequest request) {
        return pitchService.updatePitch(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePitch(@PathVariable("id") Integer id) {
        pitchService.deletePitch(id);
    }
}
