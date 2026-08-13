package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.Exam;
import com.ebserh.patientapi.model.ExamResult;
import com.ebserh.patientapi.model.Patient;
import com.ebserh.patientapi.model.dto.ExamResultRequestDTO;
import com.ebserh.patientapi.model.dto.ExamResultResponseDTO;
import com.ebserh.patientapi.repository.ExamRepository;
import com.ebserh.patientapi.repository.ExamResultRepository;
import com.ebserh.patientapi.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exam-results")
public class ExamResultController {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ExamRepository examRepository;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ExamResultResponseDTO>> getExamResultsByPatient(@PathVariable Long patientId) {
        List<ExamResult> results = examResultRepository.findByPatientIdOrderByResultDateDesc(patientId);
        List<ExamResultResponseDTO> dtos = results.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResultResponseDTO> getExamResultById(@PathVariable Long id) {
        return examResultRepository.findById(id)
                .map(result -> ResponseEntity.ok(convertToDTO(result)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExamResultResponseDTO> createExamResult(@RequestBody ExamResultRequestDTO examResultDTO) {
        Patient patient = patientRepository.findById(examResultDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Exam exam = examRepository.findById(examResultDTO.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        
        ExamResult examResult = new ExamResult(
            patient,
            exam,
            examResultDTO.getResultDate(),
            examResultDTO.getResultValue()
        );
        examResult.setNotes(examResultDTO.getNotes());
        
        ExamResult savedResult = examResultRepository.save(examResult);
        return ResponseEntity.ok(convertToDTO(savedResult));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResultResponseDTO> updateExamResult(@PathVariable Long id, @RequestBody ExamResultRequestDTO examResultDTO) {
        return examResultRepository.findById(id)
                .map(result -> {
                    Patient patient = patientRepository.findById(examResultDTO.getPatientId())
                            .orElseThrow(() -> new RuntimeException("Patient not found"));
                    
                    Exam exam = examRepository.findById(examResultDTO.getExamId())
                            .orElseThrow(() -> new RuntimeException("Exam not found"));
                    
                    result.setPatient(patient);
                    result.setExam(exam);
                    result.setResultDate(examResultDTO.getResultDate());
                    result.setResultValue(examResultDTO.getResultValue());
                    result.setNotes(examResultDTO.getNotes());
                    
                    return ResponseEntity.ok(convertToDTO(examResultRepository.save(result)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamResult(@PathVariable Long id) {
        if (examResultRepository.existsById(id)) {
            examResultRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ExamResultResponseDTO convertToDTO(ExamResult result) {
        ExamResultResponseDTO dto = new ExamResultResponseDTO();
        dto.setId(result.getId());
        dto.setPatientId(result.getPatient().getId());
        dto.setPatientName(result.getPatient().getName());
        dto.setExamId(result.getExam().getId());
        dto.setExamName(result.getExam().getName());
        dto.setResultDate(result.getResultDate());
        dto.setResultValue(result.getResultValue());
        dto.setNotes(result.getNotes());
        dto.setCreatedAt(result.getCreatedAt());
        dto.setUpdatedAt(result.getUpdatedAt());
        return dto;
    }
}