package com.ebserh.patientapi.model.dto;

import jakarta.validation.constraints.*;

public class CID10RequestDTO {

    @NotBlank(message = "CID10 code is required")
    @Size(min = 1, max = 10, message = "CID10 code must be between 1 and 10 characters")
    private String code;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters")
    private String description;

    public CID10RequestDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}