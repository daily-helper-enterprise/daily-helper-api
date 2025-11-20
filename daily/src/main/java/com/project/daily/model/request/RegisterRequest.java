package com.project.daily.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String username;
    private String password;
    public Long getRoleId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoleId'");
    }
}
