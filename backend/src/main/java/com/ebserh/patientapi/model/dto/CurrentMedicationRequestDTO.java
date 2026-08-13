package com.ebserh.patientapi.model.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class CurrentMedicationRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Medication ID is required")
    private Long medicationId;

    private Long doctorId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotBlank(message = "Dosage is required")
    @Size(max = 500, message = "Dosage must not exceed 500 characters")
    private String dosage;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    public CurrentMedicationRequestDTO() {
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}