package com.project.daily.services;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.daily.builders.MemberBuilder;
import com.project.daily.model.MemberDetails;
import com.project.daily.model.entities.Member;
import com.project.daily.model.entities.Role;
import com.project.daily.model.request.LoginRequest;
import com.project.daily.model.request.RegisterRequest;
import com.project.daily.model.response.JwtResponse;
import com.project.daily.repositories.MemberRepository;
import com.project.daily.repositories.RoleRepository;
import com.project.daily.utils.JwtUtil;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthService(MemberRepository memberRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }


    public ResponseEntity login(LoginRequest login) {
        try {
            var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
            String token = jwtUtil.generateToken(login.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    public Member getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof MemberDetails memberDetails) {
            return memberDetails.getMember();
        }

        return null;
    }

    public Member register(RegisterRequest request) {

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }

        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role não encontrada"));

        Member member = MemberBuilder.build(request.getName(), request.getEmail(), request.getUsername(), passwordEncoder.encode(request.getPassword()), role);

        return memberRepository.save(member);
    }
}
