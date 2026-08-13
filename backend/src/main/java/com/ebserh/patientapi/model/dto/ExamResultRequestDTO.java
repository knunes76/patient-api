package com.ebserh.patientapi.model.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ExamResultRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Result date is required")
    private LocalDate resultDate;

    @NotBlank(message = "Result value is required")
    @Size(max = 500, message = "Result value must not exceed 500 characters")
    private String resultValue;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    public ExamResultRequestDTO() {
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public LocalDate getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}