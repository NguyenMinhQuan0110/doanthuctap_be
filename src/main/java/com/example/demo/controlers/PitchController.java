package com.example.demo.controlers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    
    @PreAuthorize("hasAnyRole('admin')")
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
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PostMapping("/create")
    public PitchResponse createPitch(@RequestBody PitchRequest request) {
        return pitchService.createPitch(request);
    }

    @PreAuthorize("hasAnyRole('admin','owner')")
    @PutMapping("/update/{id}")
    public PitchResponse updatePitch(@PathVariable("id") Integer id, @RequestBody PitchRequest request) {
        return pitchService.updatePitch(id, request);
    }

    @PreAuthorize("hasAnyRole('admin','owner')")
    @DeleteMapping("/delete/{id}")
    public void deletePitch(@PathVariable("id") Integer id) {
        pitchService.deletePitch(id);
    }
    
    @PreAuthorize("hasAnyRole('admin','owner')")
    @PutMapping("/{id}/status")
    public ResponseEntity<PitchResponse> updateStatus(
            @PathVariable("id") Integer id,
            @RequestBody Map<String, String> requestBody) {

        String status = requestBody.get("status");
        return ResponseEntity.ok(pitchService.updateStatus(id, status));
    }
}
