package com.project.daily.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogMessageEnum {
    EMAIL_ALREADY_IN_USE("Email already in use. [email: %s]"),
    USERNAME_ALREADY_IN_USE("Username already in use. [username: %s]"),
    ROLE_NOT_FOUND("Role not found. [role: %s]"),
    ENTRY_NOT_FOUND("Entry not found. [id: %s]");

    private String message;
}
