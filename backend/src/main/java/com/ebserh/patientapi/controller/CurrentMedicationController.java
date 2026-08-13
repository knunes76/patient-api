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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/current-medication")
public class CurrentMedicationController {

    @Autowired
    private CurrentMedicationRepository currentMedicationRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<CurrentMedicationResponseDTO>> getCurrentMedicationsByPatient(@PathVariable Long patientId) {
        List<CurrentMedication> medications = currentMedicationRepository.findByPatientIdOrderByStartDateDesc(patientId);
        List<CurrentMedicationResponseDTO> dtos = medications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrentMedicationResponseDTO> getCurrentMedicationById(@PathVariable Long id) {
        return currentMedicationRepository.findById(id)
                .map(medication -> ResponseEntity.ok(convertToDTO(medication)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CurrentMedicationResponseDTO> createCurrentMedication(@RequestBody CurrentMedicationRequestDTO medicationDTO) {
        Patient patient = patientRepository.findById(medicationDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Medication medication = medicationRepository.findById(medicationDTO.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication not found"));
        
        Doctor doctor = null;
        if (medicationDTO.getDoctorId() != null) {
            doctor = doctorRepository.findById(medicationDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
        }
        
        CurrentMedication currentMedication = new CurrentMedication(
            patient,
            medication,
            medicationDTO.getStartDate(),
            medicationDTO.getDosage()
        );
        currentMedication.setDoctor(doctor);
        currentMedication.setNotes(medicationDTO.getNotes());
        
        CurrentMedication savedMedication = currentMedicationRepository.save(currentMedication);
        return ResponseEntity.ok(convertToDTO(savedMedication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrentMedicationResponseDTO> updateCurrentMedication(@PathVariable Long id, @RequestBody CurrentMedicationRequestDTO medicationDTO) {
        return currentMedicationRepository.findById(id)
                .map(currentMedication -> {
                    Patient patient = patientRepository.findById(medicationDTO.getPatientId())
                            .orElseThrow(() -> new RuntimeException("Patient not found"));
                    
                    Medication medication = medicationRepository.findById(medicationDTO.getMedicationId())
                            .orElseThrow(() -> new RuntimeException("Medication not found"));
                    
                    Doctor doctor = null;
                    if (medicationDTO.getDoctorId() != null) {
                        doctor = doctorRepository.findById(medicationDTO.getDoctorId())
                                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                    }
                    
                    currentMedication.setPatient(patient);
                    currentMedication.setMedication(medication);
                    currentMedication.setDoctor(doctor);
                    currentMedication.setStartDate(medicationDTO.getStartDate());
                    currentMedication.setDosage(medicationDTO.getDosage());
                    currentMedication.setNotes(medicationDTO.getNotes());
                    
                    return ResponseEntity.ok(convertToDTO(currentMedicationRepository.save(currentMedication)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrentMedication(@PathVariable Long id) {
        if (currentMedicationRepository.existsById(id)) {
            currentMedicationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private CurrentMedicationResponseDTO convertToDTO(CurrentMedication medication) {
        CurrentMedicationResponseDTO dto = new CurrentMedicationResponseDTO();
        dto.setId(medication.getId());
        dto.setPatientId(medication.getPatient().getId());
        dto.setPatientName(medication.getPatient().getName());
        dto.setMedicationId(medication.getMedication().getId());
        dto.setMedicationName(medication.getMedication().getName());
        dto.setMedicationDosageForm(medication.getMedication().getDosageForm());
        
        if (medication.getDoctor() != null) {
            dto.setDoctorId(medication.getDoctor().getId());
            dto.setDoctorName(medication.getDoctor().getName());
            dto.setDoctorCrm(medication.getDoctor().getCrm());
        }
        
        dto.setStartDate(medication.getStartDate());
        dto.setDosage(medication.getDosage());
        dto.setNotes(medication.getNotes());
        dto.setCreatedAt(medication.getCreatedAt());
        dto.setUpdatedAt(medication.getUpdatedAt());
        return dto;
    }
}