package com.ebserh.patientapi.model.dto;

import jakarta.validation.constraints.*;

public class UnityRequestDTO {

    @NotBlank(message = "Unity name is required")
    @Size(min = 2, max = 100, message = "Unity name must be between 2 and 100 characters")
    private String name;

    public UnityRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}