package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.Specialty;
import com.ebserh.patientapi.model.dto.SpecialtyRequestDTO;
import com.ebserh.patientapi.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialty")
public class SpecialtyController {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @GetMapping
    public ResponseEntity<List<Specialty>> getAllSpecialties() {
        return ResponseEntity.ok(specialtyRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> getSpecialtyById(@PathVariable Long id) {
        return specialtyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Specialty> createSpecialty(@RequestBody SpecialtyRequestDTO specialtyDTO) {
        Specialty specialty = new Specialty(specialtyDTO.getName());
        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return ResponseEntity.ok(savedSpecialty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> updateSpecialty(@PathVariable Long id, @RequestBody SpecialtyRequestDTO specialtyDTO) {
        return specialtyRepository.findById(id)
                .map(specialty -> {
                    specialty.setName(specialtyDTO.getName());
                    return ResponseEntity.ok(specialtyRepository.save(specialty));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        if (specialtyRepository.existsById(id)) {
            specialtyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}