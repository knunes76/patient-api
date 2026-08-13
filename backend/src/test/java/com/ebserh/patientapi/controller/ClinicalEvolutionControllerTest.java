package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.CID10;
import com.ebserh.patientapi.model.ClinicalEvolution;
import com.ebserh.patientapi.model.Doctor;
import com.ebserh.patientapi.model.Patient;
import com.ebserh.patientapi.model.Specialty;
import com.ebserh.patientapi.model.User;
import com.ebserh.patientapi.model.dto.ClinicalEvolutionRequestDTO;
import com.ebserh.patientapi.model.dto.ClinicalEvolutionResponseDTO;
import com.ebserh.patientapi.repository.CID10Repository;
import com.ebserh.patientapi.repository.ClinicalEvolutionRepository;
import com.ebserh.patientapi.repository.DoctorRepository;
import com.ebserh.patientapi.repository.PatientRepository;
import com.ebserh.patientapi.repository.SpecialtyRepository;
import com.ebserh.patientapi.repository.UserRepository;
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

@WebMvcTest(ClinicalEvolutionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClinicalEvolutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClinicalEvolutionRepository clinicalEvolutionRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private CID10Repository cid10Repository;

    @MockBean
    private SpecialtyRepository specialtyRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private ClinicalEvolutionRequestDTO evolutionDTO;
    private ClinicalEvolutionResponseDTO evolutionResponseDTO;
    private Patient patient;
    private Doctor doctor;
    private CID10 cid10;
    private Specialty specialty;
    private User user;
    private ClinicalEvolution clinicalEvolution;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Smith");
        doctor.setCrm("12345");

        cid10 = new CID10();
        cid10.setId(1L);
        cid10.setCode("A00");
        cid10.setDescription("Cholera");

        specialty = new Specialty();
        specialty.setId(1L);
        specialty.setName("Cardiology");

        user = new User();
        user.setId(1L);
        user.setFullName("Admin User");

        clinicalEvolution = new ClinicalEvolution(patient, LocalDate.now(), "Headache");
        clinicalEvolution.setId(1L);
        clinicalEvolution.setDoctor(doctor);
        clinicalEvolution.setCid10(cid10);
        clinicalEvolution.setSpecialty(specialty);
        clinicalEvolution.setDiagnosis("Migraine");
        clinicalEvolution.setConsultationType("Consultation");
        clinicalEvolution.setSubject("Follow-up");
        clinicalEvolution.setCreatedBy(user);

        evolutionDTO = new ClinicalEvolutionRequestDTO();
        evolutionDTO.setPatientId(1L);
        evolutionDTO.setDoctorId(1L);
        evolutionDTO.setCid10Id(1L);
        evolutionDTO.setSpecialtyId(1L);
        evolutionDTO.setAppointmentDate(LocalDate.now());
        evolutionDTO.setComplaint("Headache");
        evolutionDTO.setDiagnosis("Migraine");
        evolutionDTO.setConsultationType("Consultation");
        evolutionDTO.setSubject("Follow-up");
        evolutionDTO.setCreatedBy(1L);

        evolutionResponseDTO = new ClinicalEvolutionResponseDTO();
        evolutionResponseDTO.setId(1L);
        evolutionResponseDTO.setPatientId(1L);
        evolutionResponseDTO.setPatientName("John Doe");
        evolutionResponseDTO.setDoctorId(1L);
        evolutionResponseDTO.setDoctorName("Dr. Smith");
        evolutionResponseDTO.setDoctorCrm("12345");
        evolutionResponseDTO.setCid10Id(1L);
        evolutionResponseDTO.setCid10Code("A00");
        evolutionResponseDTO.setCid10Description("Cholera");
        evolutionResponseDTO.setSpecialtyId(1L);
        evolutionResponseDTO.setSpecialtyName("Cardiology");
        evolutionResponseDTO.setAppointmentDate(LocalDate.now());
        evolutionResponseDTO.setComplaint("Headache");
        evolutionResponseDTO.setDiagnosis("Migraine");
        evolutionResponseDTO.setConsultationType("Consultation");
        evolutionResponseDTO.setSubject("Follow-up");
        evolutionResponseDTO.setCreatedBy(1L);
        evolutionResponseDTO.setCreatedByName("Admin User");
    }

    @Test
    void createClinicalEvolution_Success() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(cid10Repository.findById(1L)).thenReturn(Optional.of(cid10));
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialty));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(clinicalEvolutionRepository.save(any(ClinicalEvolution.class))).thenReturn(clinicalEvolution);

        mockMvc.perform(post("/api/clinical-evolution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.cid10Id").value(1))
                .andExpect(jsonPath("$.specialtyId").value(1))
                .andExpect(jsonPath("$.complaint").value("Headache"));
    }

    @Test
    void createClinicalEvolution_WithoutOptionalFields_Success() throws Exception {
        evolutionDTO.setDoctorId(null);
        evolutionDTO.setCid10Id(null);
        evolutionDTO.setSpecialtyId(null);
        evolutionDTO.setCreatedBy(null);
        clinicalEvolution.setDoctor(null);
        clinicalEvolution.setCid10(null);
        clinicalEvolution.setSpecialty(null);
        clinicalEvolution.setCreatedBy(null);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(clinicalEvolutionRepository.save(any(ClinicalEvolution.class))).thenReturn(clinicalEvolution);

        mockMvc.perform(post("/api/clinical-evolution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.complaint").value("Headache"));
    }

    @Test
    void createClinicalEvolution_PatientNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/clinical-evolution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createClinicalEvolution_DoctorNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/clinical-evolution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createClinicalEvolution_Cid10NotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(cid10Repository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/clinical-evolution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createClinicalEvolution_SpecialtyNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(cid10Repository.findById(1L)).thenReturn(Optional.of(cid10));
        when(specialtyRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/clinical-evolution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createClinicalEvolution_UserNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(cid10Repository.findById(1L)).thenReturn(Optional.of(cid10));
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialty));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/clinical-evolution")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getClinicalEvolutionById_Success() throws Exception {
        when(clinicalEvolutionRepository.findById(1L)).thenReturn(Optional.of(clinicalEvolution));

        mockMvc.perform(get("/api/clinical-evolution/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.doctorId").value(1))
                .andExpect(jsonPath("$.cid10Id").value(1))
                .andExpect(jsonPath("$.specialtyId").value(1));
    }

    @Test
    void getClinicalEvolutionById_NotFound() throws Exception {
        when(clinicalEvolutionRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clinical-evolution/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getClinicalEvolutionsByPatient_Success() throws Exception {
        ClinicalEvolution evolution2 = new ClinicalEvolution(patient, LocalDate.now(), "Fever");
        evolution2.setId(2L);

        when(clinicalEvolutionRepository.findByPatientIdOrderByAppointmentDateDesc(1L))
                .thenReturn(Arrays.asList(clinicalEvolution, evolution2));

        mockMvc.perform(get("/api/clinical-evolution/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateClinicalEvolution_Success() throws Exception {
        evolutionResponseDTO.setDiagnosis("Updated diagnosis");

        when(clinicalEvolutionRepository.findById(1L)).thenReturn(Optional.of(clinicalEvolution));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(cid10Repository.findById(1L)).thenReturn(Optional.of(cid10));
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialty));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(clinicalEvolutionRepository.save(any(ClinicalEvolution.class))).thenReturn(clinicalEvolution);

        mockMvc.perform(put("/api/clinical-evolution/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateClinicalEvolution_NotFound() throws Exception {
        when(clinicalEvolutionRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/clinical-evolution/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(evolutionDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteClinicalEvolution_Success() throws Exception {
        when(clinicalEvolutionRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/clinical-evolution/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteClinicalEvolution_NotFound() throws Exception {
        when(clinicalEvolutionRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/clinical-evolution/1"))
                .andExpect(status().isNotFound());
    }
}