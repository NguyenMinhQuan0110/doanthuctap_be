package com.example.demo.controlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.request.PitchGroupRequest;
import com.example.demo.dtos.response.PitchGroupResponse;
import com.example.demo.services.interfaces.PitchGroupService;

@RestController
@RequestMapping("/api/pitch-groups")
public class PitchGroupController {
    @Autowired
    private PitchGroupService pitchGroupService;
    
    @GetMapping
    public List<PitchGroupResponse> getAll() {
        return pitchGroupService.getAllPitchGroups();
    }

    @GetMapping("/{id}")
    public PitchGroupResponse getById(@PathVariable("id") Integer id) {
        return pitchGroupService.getPitchGroupById(id);
    }

    @GetMapping("/complex/{complexId}")
    public List<PitchGroupResponse> getByComplex(@PathVariable("complexId") Integer complexId) {
        return pitchGroupService.getPitchGroupsByComplex(complexId);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PostMapping("/create")
    public PitchGroupResponse create(@RequestBody PitchGroupRequest request) {
        return pitchGroupService.createPitchGroup(request);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @PutMapping("/update/{id}")
    public PitchGroupResponse update(@PathVariable("id") Integer id, @RequestBody PitchGroupRequest request) {
        return pitchGroupService.updatePitchGroup(id, request);
    }

    @PreAuthorize("hasAnyRole('admin')")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        pitchGroupService.deletePitchGroup(id);
    }
}
