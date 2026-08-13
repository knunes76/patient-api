package com.ebserh.patientapi.model.dto;

import jakarta.validation.constraints.*;

public class ExamRequestDTO {

    @NotBlank(message = "Exam name is required")
    @Size(min = 2, max = 100, message = "Exam name must be between 2 and 100 characters")
    private String name;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    public ExamRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}