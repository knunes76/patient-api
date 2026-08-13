package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.Exam;
import com.ebserh.patientapi.model.ExamResult;
import com.ebserh.patientapi.model.Patient;
import com.ebserh.patientapi.model.dto.ExamResultRequestDTO;
import com.ebserh.patientapi.model.dto.ExamResultResponseDTO;
import com.ebserh.patientapi.repository.ExamRepository;
import com.ebserh.patientapi.repository.ExamResultRepository;
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

@WebMvcTest(ExamResultController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExamResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExamResultRepository examResultRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private ExamRepository examRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private ExamResultRequestDTO examResultDTO;
    private ExamResultResponseDTO examResultResponseDTO;
    private Patient patient;
    private Exam exam;
    private ExamResult examResult;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");

        exam = new Exam();
        exam.setId(1L);
        exam.setName("Blood Test");

        examResult = new ExamResult(patient, exam, LocalDate.now(), "Normal");
        examResult.setId(1L);
        examResult.setNotes("No issues found");

        examResultDTO = new ExamResultRequestDTO();
        examResultDTO.setPatientId(1L);
        examResultDTO.setExamId(1L);
        examResultDTO.setResultDate(LocalDate.now());
        examResultDTO.setResultValue("Normal");
        examResultDTO.setNotes("No issues found");

        examResultResponseDTO = new ExamResultResponseDTO();
        examResultResponseDTO.setId(1L);
        examResultResponseDTO.setPatientId(1L);
        examResultResponseDTO.setPatientName("John Doe");
        examResultResponseDTO.setExamId(1L);
        examResultResponseDTO.setExamName("Blood Test");
        examResultResponseDTO.setResultDate(LocalDate.now());
        examResultResponseDTO.setResultValue("Normal");
        examResultResponseDTO.setNotes("No issues found");
    }

    @Test
    void createExamResult_Success() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(examResultRepository.save(any(ExamResult.class))).thenReturn(examResult);

        mockMvc.perform(post("/api/exam-results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examResultDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.examId").value(1))
                .andExpect(jsonPath("$.resultValue").value("Normal"));
    }

    @Test
    void createExamResult_PatientNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/exam-results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examResultDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createExamResult_ExamNotFound_ThrowsException() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(examRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/exam-results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examResultDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getExamResultById_Success() throws Exception {
        when(examResultRepository.findById(1L)).thenReturn(Optional.of(examResult));

        mockMvc.perform(get("/api/exam-results/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.examId").value(1));
    }

    @Test
    void getExamResultById_NotFound() throws Exception {
        when(examResultRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/exam-results/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getExamResultsByPatient_Success() throws Exception {
        ExamResult examResult2 = new ExamResult(patient, exam, LocalDate.now(), "Abnormal");
        examResult2.setId(2L);

        when(examResultRepository.findByPatientIdOrderByResultDateDesc(1L))
                .thenReturn(Arrays.asList(examResult, examResult2));

        mockMvc.perform(get("/api/exam-results/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateExamResult_Success() throws Exception {
        examResultResponseDTO.setResultValue("Updated Result");

        when(examResultRepository.findById(1L)).thenReturn(Optional.of(examResult));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(examResultRepository.save(any(ExamResult.class))).thenReturn(examResult);

        mockMvc.perform(put("/api/exam-results/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examResultDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void updateExamResult_NotFound() throws Exception {
        when(examResultRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/exam-results/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examResultDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExamResult_Success() throws Exception {
        when(examResultRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/exam-results/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteExamResult_NotFound() throws Exception {
        when(examResultRepository.existsById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/exam-results/1"))
                .andExpect(status().isNotFound());
    }
}