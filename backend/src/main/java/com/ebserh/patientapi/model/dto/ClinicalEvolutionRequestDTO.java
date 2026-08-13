package com.ebserh.patientapi.model.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ClinicalEvolutionRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long doctorId;

    private Long cid10Id;

    private Long specialtyId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotBlank(message = "Complaint is required")
    @Size(max = 1000, message = "Complaint must not exceed 1000 characters")
    private String complaint;

    @Size(max = 1000, message = "Diagnosis must not exceed 1000 characters")
    private String diagnosis;

    @Size(max = 20, message = "Consultation type must not exceed 20 characters")
    private String consultationType;

    @Size(max = 200, message = "Subject must not exceed 200 characters")
    private String subject;

    private Long createdBy;

    public ClinicalEvolutionRequestDTO() {
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getCid10Id() {
        return cid10Id;
    }

    public void setCid10Id(Long cid10Id) {
        this.cid10Id = cid10Id;
    }

    public Long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}