package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.dto.PatientRequestDTO;
import com.ebserh.patientapi.model.dto.PatientResponseDTO;
import com.ebserh.patientapi.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private PatientRequestDTO patientDTO;
    private PatientResponseDTO patientResponseDTO;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientRequestDTO();
        patientDTO.setName("John Doe");
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail("john.doe@example.com");
        patientDTO.setPhone("11987654321");
        patientDTO.setBirthDate(LocalDate.of(1990, 1, 1));

        patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(1L);
        patientResponseDTO.setName("John Doe");
        patientResponseDTO.setCpf("12345678901");
        patientResponseDTO.setEmail("john.doe@example.com");
        patientResponseDTO.setPhone("11987654321");
        patientResponseDTO.setBirthDate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void createPatient_Success() throws Exception {
        when(patientService.createPatient(any(PatientRequestDTO.class))).thenReturn(patientResponseDTO);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void createPatient_ValidationError() throws Exception {
        patientDTO.setName(""); // Invalid name

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPatientById_Success() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(patientResponseDTO);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getPatientByCpf_Success() throws Exception {
        when(patientService.getPatientByCpf("12345678901")).thenReturn(patientResponseDTO);

        mockMvc.perform(get("/api/patients/cpf/12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getAllPatients_Success() throws Exception {
        PatientResponseDTO patient2 = new PatientResponseDTO();
        patient2.setId(2L);
        patient2.setName("Jane Smith");
        patient2.setCpf("98765432109");
        patient2.setEmail("jane.smith@example.com");
        patient2.setPhone("11912345678");
        patient2.setBirthDate(LocalDate.of(1985, 5, 15));

        Pageable pageable = PageRequest.of(0, 10);
        Page<PatientResponseDTO> patientPage = new PageImpl<>(Arrays.asList(patientResponseDTO, patient2));

        when(patientService.getAllPatients(any(Pageable.class))).thenReturn(patientPage);

        mockMvc.perform(get("/api/patients")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void searchPatientsByName_Success() throws Exception {
        PatientResponseDTO patient2 = new PatientResponseDTO();
        patient2.setId(2L);
        patient2.setName("Johnny Smith");
        patient2.setCpf("98765432109");
        patient2.setEmail("johnny.smith@example.com");
        patient2.setPhone("11912345678");
        patient2.setBirthDate(LocalDate.of(1985, 5, 15));

        List<PatientResponseDTO> patients = Arrays.asList(patientResponseDTO, patient2);
        when(patientService.searchPatientsByName("John")).thenReturn(patients);

        mockMvc.perform(get("/api/patients/search")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updatePatient_Success() throws Exception {
        patientResponseDTO.setName("John Updated");
        when(patientService.updatePatient(eq(1L), any(PatientRequestDTO.class))).thenReturn(patientResponseDTO);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deletePatient_Success() throws Exception {
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }
}
