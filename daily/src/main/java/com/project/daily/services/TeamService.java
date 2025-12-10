package com.project.daily.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.daily.model.entities.Team;
import com.project.daily.model.entities.Member;
import com.project.daily.model.request.TeamRequest;
import com.project.daily.model.response.EntryResponse;
import com.project.daily.model.response.TeamMembersResponse;
import com.project.daily.model.response.TeamResponse;
import com.project.daily.repositories.TeamRepository;
import com.project.daily.repositories.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final EntryService entryService;
    private final AuthService authService;

    public List<TeamResponse> findAll() {
        return teamRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<EntryResponse> getMembersAndEntries(Long teamId, LocalDateTime startDate, LocalDateTime endDate) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found: " + teamId));

        return team.getMembers()
                .stream()
                .flatMap(member -> member.getEntries().stream())
                .filter(entry ->
                        entry.getRemovedAt() == null &&
                        (entry.getCreatedAt().isAfter(startDate) || entry.getCreatedAt().isEqual(startDate)) &&
                        (entry.getCreatedAt().isBefore(endDate) || entry.getCreatedAt().isEqual(endDate))
                )
                .map(entryService::toResponse)
                .collect(Collectors.toList());
    }

    public TeamResponse findById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        return toResponse(team);
    }

    public TeamResponse create(TeamRequest req) {
        Member creator = authService.getLoggedUser();
        if (creator == null) {
            throw new RuntimeException("Member not logged in");
        }
        Team team = Team.builder()
                .name(req.getName())
                .description(req.getDescription())
                .scrumMaster(creator)
                .build();
        team.setMembers(Set.of(creator));
        teamRepository.save(team);
        return toResponse(team);
    }

    public TeamResponse updateName(Long teamId, String newName) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        Member logged = authService.getLoggedUser();
        if (logged == null || !team.getScrumMaster().getId().equals(logged.getId())) {
            throw new RuntimeException("Only the Scrum Master can update the team name");
        }

        team.setName(newName);
        teamRepository.save(team);

        return toResponse(team);
    }

    public TeamResponse updateDescription(Long teamId, String newDescription) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        Member logged = authService.getLoggedUser();
        if (logged == null || !team.getScrumMaster().getId().equals(logged.getId())) {
            throw new RuntimeException("Only the Scrum Master can update the team description");
        }

        team.setDescription(newDescription);
        teamRepository.save(team);

        return toResponse(team);
    }

    public TeamResponse transferScrumMaster(Long teamId, Long newScrumMasterId) {

        Member logged = authService.getLoggedUser();
        if (logged == null) {
            throw new RuntimeException("Member not logged in");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        if (!team.getScrumMaster().getId().equals(logged.getId())) {
            throw new RuntimeException("Only the current Scrum Master can transfer this role.");
        }

        Member newScrumMaster = memberRepository.findById(newScrumMasterId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (!team.getMembers().contains(newScrumMaster)) {
            throw new RuntimeException("The new Scrum Master must be a member of the team.");
        }

        team.setScrumMaster(newScrumMaster);
        teamRepository.save(team);

        return toResponse(team);
    }

    public void delete(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        Member logged = authService.getLoggedUser();
        if (logged == null || !team.getScrumMaster().getId().equals(logged.getId())) {
            throw new RuntimeException("Only the Scrum Master can delete the team");
        }

        teamRepository.delete(team);
    }

    private TeamResponse toResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .scrumMaster(team.getScrumMaster() != null
                        ? team.getScrumMaster().getName()
                        : null)
                .members(team.getMembers()
                        .stream()
                        .map((member) -> {
                                return TeamMembersResponse.builder()
                                .name(member.getName())
                                .id(member.getId())
                                .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    public TeamResponse addMember(Long teamId, Long memberId) {
            Team team = teamRepository.findById(teamId)
                    .orElseThrow(() -> new EntityNotFoundException("Team not found"));
    
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found"));
    
            team.getMembers().add(member);
            teamRepository.save(team);
    
            return toResponse(team);
    }    

    public TeamResponse removeMember(Long teamId, Long memberId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        team.getMembers().remove(member);
        teamRepository.save(team);

        return toResponse(team);
    }
}
