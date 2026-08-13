package com.ebserh.patientapi.model.dto;

import jakarta.validation.constraints.*;

public class SpecialtyRequestDTO {

    @NotBlank(message = "Specialty name is required")
    @Size(min = 2, max = 100, message = "Specialty name must be between 2 and 100 characters")
    private String name;

    public SpecialtyRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}