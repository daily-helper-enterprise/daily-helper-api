package com.project.daily.model;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.daily.model.entities.Member;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class MemberDetails implements UserDetails {
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = member.getRole() != null ? member.getRole().getName() : "USER";
        return List.of(new SimpleGrantedAuthority("ROLE_" + roleName));
    }

    @Override public String getPassword() { return member.getPassword(); }
    @Override public String getUsername() { return member.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return member.getRemovedAt() == null; }
}

