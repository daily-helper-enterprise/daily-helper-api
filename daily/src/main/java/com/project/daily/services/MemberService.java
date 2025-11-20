package com.project.daily.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.daily.model.entities.Member;
import com.project.daily.model.entities.Role;
import com.project.daily.model.request.RegisterRequest;
import com.project.daily.model.response.MemberResponse;
import com.project.daily.repositories.MemberRepository;
import com.project.daily.repositories.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
        return toResponse(member);
    }

    public MemberResponse create(RegisterRequest req) {

        Role role = roleRepository.findById(req.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        Member member = Member.builder()
                .name(req.getName())
                .email(req.getEmail())
                .username(req.getUsername())
                .password(req.getPassword()) // futuramente hashed
                .role(role)
                .build();

        memberRepository.save(member);
        return toResponse(member);
    }

    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        member.setRemovedAt(java.time.LocalDateTime.now());
        memberRepository.save(member);
    }

    private MemberResponse toResponse(Member m) {
        return MemberResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .email(m.getEmail())
                .username(m.getUsername())
                .build();
    }
}
