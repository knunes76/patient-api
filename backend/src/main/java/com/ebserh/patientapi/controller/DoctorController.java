package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.Doctor;
import com.ebserh.patientapi.model.dto.DoctorRequestDTO;
import com.ebserh.patientapi.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody DoctorRequestDTO doctorDTO) {
        Doctor doctor = new Doctor(
            doctorDTO.getName(),
            doctorDTO.getCrm(),
            doctorDTO.getSpecialty()
        );
        doctor.setPhone(doctorDTO.getPhone());
        doctor.setEmail(doctorDTO.getEmail());
        Doctor savedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(savedDoctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody DoctorRequestDTO doctorDTO) {
        return doctorRepository.findById(id)
                .map(doctor -> {
                    doctor.setName(doctorDTO.getName());
                    doctor.setCrm(doctorDTO.getCrm());
                    doctor.setSpecialty(doctorDTO.getSpecialty());
                    doctor.setPhone(doctorDTO.getPhone());
                    doctor.setEmail(doctorDTO.getEmail());
                    return ResponseEntity.ok(doctorRepository.save(doctor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}