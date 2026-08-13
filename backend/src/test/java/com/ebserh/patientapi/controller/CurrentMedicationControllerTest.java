package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.CurrentMedication;
import com.ebserh.patientapi.model.Doctor;
import com.ebserh.patientapi.model.Medication;
import com.ebserh.patientapi.model.Patient;
import com.ebserh.patientapi.model.dto.CurrentMedicationRequestDTO;
import com.ebserh.patientapi.model.dto.CurrentMedicationResponseDTO;
import com.ebserh.patientapi.repository.CurrentMedicationRepository;
import com.ebserh.patientapi.repository.DoctorRepository;
import com.ebserh.patientapi.repository.MedicationRepository;
import com.ebserh.patientapi.repository.PatientRepository;
import com.ebserh.patientapi.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrentMedicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class CurrentMedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrentMedicationRepository currentMedicationRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private MedicationRepository medicationRepository;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private CurrentMedicationRequestDTO medicationDTO;
    private CurrentMedicationResponseDTO medicationResponseDTO;
    private Patient patient;
    private Medication medication;
    private Doctor doctor;
    private CurrentMedication currentMedication;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");

        medication = new Medication();
        medication.setId(1L);
        medication.setName("Paracetamol");
        medication.setDosageForm("Tablet");

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setCrm("12345");

        currentMedication = new CurrentMedication(patient, medication, LocalDate.now(), "500mg twice daily");
        currentMedication.setId(1L);
        currentMedication.setDoctor(doctor);
        currentMedication.setNotes("Take with food");

        medicationDTO = new CurrentMedicationRequestDTO();
        medicationDTO.setPatientId(1L);
        medicationDTO.setMedicationId(1L);
        medicationDTO.setDoctorId(1L);
        medicationDTO.setStartDate(LocalDate.now());
        medicationDTO.setDosage("500mg twice daily");
        medicationDTO.setNotes("Take with food");

        medicationResponseDTO = new CurrentMedicationResponseDTO();
        medicationResponseDTO.setId(1L);
        medicationResponseDTO.setPatientId(1L);
        medicationResponseDTO.setPatientName("John Doe");
        medicationResponseDTO.setMedicationId(1L);
        medicationResponseDTO.setMedicationName("Paracetamol");
        medicationResponseDTO.setMedicationDosageForm("Tablet");
        medicationResponseDTO.setDoctorId(1L);
        medicationResponseDTO.setDoctorName("Dr. Smith");
        medicationResponseDTO.setDoctorCrm("12345");
        medicationResponseDTO.setStartDate(LocalDate.now());
        medicationResponseDTO.setDosage("500mg twice daily");
        medicationResponseDTO.setNotes("Take with food");
    }

    @Test
    void createCurrentMedication_Success() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(currentMedicationRepository.save(any(CurrentMedication.class))).thenReturn(currentMedication);

        mockMvc.perform(post("/api/current-medication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.medicationId").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.dosage").value("500mg twice daily"));
    }

    @Test
    void createCurrentMedication_WithoutDoctor_Success() throws Exception {
        medicationDTO.setDoctorId(null);
        currentMedication.setDoctor(null);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(currentMedicationRepository.save(any(CurrentMedication.class))).thenReturn(currentMedication);

        mockMvc.perform(post("/api/current-medication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.medicationId").value(1));
    }

    @Test
    void createCurrentMedication_PatientNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/current-medication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createCurrentMedication_MedicationNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/current-medication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createCurrentMedication_DoctorNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/current-medication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getCurrentMedicationById_Success() throws Exception {
        when(currentMedicationRepository.findById(1L)).thenReturn(Optional.of(currentMedication));

        mockMvc.perform(get("/api/current-medication/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.medicationId").value(1))
                .andExpect(jsonPath("$.doctorId").value(1));
    }

    @Test
    void getCurrentMedicationById_NotFound() throws Exception {
        when(currentMedicationRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/current-medication/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCurrentMedicationsByPatient_Success() throws Exception {
        CurrentMedication medication2 = new CurrentMedication(patient, medication, LocalDate.now(), "1000mg daily");
        medication2.setId(2L);

        when(currentMedicationRepository.findByPatientIdOrderByStartDateDesc(1L))
                .thenReturn(Arrays.asList(currentMedication, medication2));

        mockMvc.perform(get("/api/current-medication/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateCurrentMedication_Success() throws Exception {
        medicationResponseDTO.setDosage("Updated dosage");

        when(currentMedicationRepository.findById(1L)).thenReturn(Optional.of(currentMedication));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(currentMedicationRepository.save(any(CurrentMedication.class))).thenReturn(currentMedication);

        mockMvc.perform(put("/api/current-medication/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCurrentMedication_NotFound() throws Exception {
        when(currentMedicationRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/current-medication/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medicationDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCurrentMedication_Success() throws Exception {
        when(currentMedicationRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/current-medication/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCurrentMedication_NotFound() throws Exception {
        when(currentMedicationRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/current-medication/1"))
                .andExpect(status().isNotFound());
    }
}