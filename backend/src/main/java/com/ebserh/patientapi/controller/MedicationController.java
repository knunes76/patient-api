package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.Medication;
import com.ebserh.patientapi.model.dto.MedicationRequestDTO;
import com.ebserh.patientapi.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    @Autowired
    private MedicationRepository medicationRepository;

    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        return ResponseEntity.ok(medicationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        return medicationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medication> createMedication(@RequestBody MedicationRequestDTO medicationDTO) {
        Medication medication = new Medication(
            medicationDTO.getName(),
            medicationDTO.getDescription(),
            medicationDTO.getDosageForm()
        );
        Medication savedMedication = medicationRepository.save(medication);
        return ResponseEntity.ok(savedMedication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long id, @RequestBody MedicationRequestDTO medicationDTO) {
        return medicationRepository.findById(id)
                .map(medication -> {
                    medication.setName(medicationDTO.getName());
                    medication.setDescription(medicationDTO.getDescription());
                    medication.setDosageForm(medicationDTO.getDosageForm());
                    return ResponseEntity.ok(medicationRepository.save(medication));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        if (medicationRepository.existsById(id)) {
            medicationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}