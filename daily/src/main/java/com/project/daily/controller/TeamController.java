package com.project.daily.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.daily.model.request.TeamRequest;
import com.project.daily.model.response.TeamResponse;
import com.project.daily.services.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public List<TeamResponse> getAll() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public TeamResponse getById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TeamResponse> create(@RequestBody TeamRequest request) {
        TeamResponse response = teamService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/name")
    public TeamResponse updateName(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return teamService.updateName(id, body.get("name"));
    }

    @PatchMapping("/{id}/description")
    public TeamResponse updateDescription(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return teamService.updateDescription(id, body.get("description"));
    }

    @PatchMapping("/{id}/scrum-master/{newScrumMasterId}")
    public TeamResponse transferScrumMaster(
            @PathVariable Long id,
            @PathVariable Long newScrumMasterId
    ) {
        return teamService.transferScrumMaster(id, newScrumMasterId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/members/{memberId}")
    public TeamResponse addMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId
    ) {
        return teamService.addMember(teamId, memberId);
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    public TeamResponse removeMember(
            @PathVariable Long teamId,
            @PathVariable Long memberId
    ) {
        return teamService.removeMember(teamId, memberId);
    }
}
