package com.smaatix.application.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String identifier;  // Can be email, phone, or username
    private String password;
}