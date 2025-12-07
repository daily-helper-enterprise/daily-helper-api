package com.project.daily.services;


import org.apache.coyote.BadRequestException;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.daily.builders.MemberBuilder;
import com.project.daily.exception.EntityAlreadyExistsException;
import com.project.daily.exception.EntityNotFoundException;
import com.project.daily.message.LogMessageEnum;
import com.project.daily.model.MemberDetails;
import com.project.daily.model.entities.Member;
import com.project.daily.model.entities.Role;
import com.project.daily.model.request.LoginRequest;
import com.project.daily.model.request.RegisterRequest;
import com.project.daily.model.response.JwtResponse;
import com.project.daily.model.response.MemberResponse;
import com.project.daily.repositories.MemberRepository;
import com.project.daily.repositories.RoleRepository;
import com.project.daily.utils.JwtUtil;

@Service
public class AuthService {


    private final String ROLE_USER = "USER";
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
            authManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
            String token = jwtUtil.generateToken(login.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    public MemberResponse getLoggedUserResponse() {
        Member member = getLoggedUser();
        if (member == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }

    public Member getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MemberDetails memberDetails) {
            return memberDetails.getMember();
        }
        return null;
    }

    public Member register(RegisterRequest request) throws BadRequestException {

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format(LogMessageEnum.EMAIL_ALREADY_IN_USE.getMessage(), request.getEmail()));
        }

        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format(LogMessageEnum.USERNAME_ALREADY_IN_USE.getMessage(), request.getUsername()));
        }

        Role role = roleRepository.findByName(ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException(String.format(LogMessageEnum.ROLE_NOT_FOUND.getMessage(), ROLE_USER)));

        Member member = MemberBuilder.build(request.getName(), request.getEmail(), request.getUsername(), passwordEncoder.encode(request.getPassword()), role);

        return memberRepository.save(member);
    }
}
