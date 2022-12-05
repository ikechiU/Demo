package com.example.demotest.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Contact is required")
    private String contact;
    @NotBlank(message = "Password is required")
    private String password;
}
