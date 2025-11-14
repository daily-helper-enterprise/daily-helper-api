package com.project.daily.builders;

import java.util.UUID;

import com.project.daily.model.entities.Member;
import com.project.daily.model.entities.Role;

public class MemberBuilder {
    

    public static Member build(String name, String email, String username, String passwordEncoded, Role role) {
        return Member.builder()
                .uuid(UUID.randomUUID())
                .name(name)
                .email(email)
                .username(username)
                .password(passwordEncoded)
                .role(role)
                .build();
    }
}
