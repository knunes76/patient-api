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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clinical-evolution")
public class ClinicalEvolutionController {

    @Autowired
    private ClinicalEvolutionRepository clinicalEvolutionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CID10Repository cid10Repository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ClinicalEvolutionResponseDTO>> getClinicalEvolutionsByPatient(@PathVariable Long patientId) {
        List<ClinicalEvolution> evolutions = clinicalEvolutionRepository.findByPatientIdOrderByAppointmentDateDesc(patientId);
        List<ClinicalEvolutionResponseDTO> dtos = evolutions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalEvolutionResponseDTO> getClinicalEvolutionById(@PathVariable Long id) {
        return clinicalEvolutionRepository.findById(id)
                .map(evolution -> ResponseEntity.ok(convertToDTO(evolution)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClinicalEvolutionResponseDTO> createClinicalEvolution(@RequestBody ClinicalEvolutionRequestDTO evolutionDTO) {
        Patient patient = patientRepository.findById(evolutionDTO.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Doctor doctor = null;
        if (evolutionDTO.getDoctorId() != null) {
            doctor = doctorRepository.findById(evolutionDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
        }
        
        CID10 cid10 = null;
        if (evolutionDTO.getCid10Id() != null) {
            cid10 = cid10Repository.findById(evolutionDTO.getCid10Id())
                    .orElseThrow(() -> new RuntimeException("CID10 not found"));
        }
        
        Specialty specialty = null;
        if (evolutionDTO.getSpecialtyId() != null) {
            specialty = specialtyRepository.findById(evolutionDTO.getSpecialtyId())
                    .orElseThrow(() -> new RuntimeException("Specialty not found"));
        }
        
        User createdBy = null;
        if (evolutionDTO.getCreatedBy() != null) {
            createdBy = userRepository.findById(evolutionDTO.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        
        ClinicalEvolution clinicalEvolution = new ClinicalEvolution(
            patient,
            evolutionDTO.getAppointmentDate(),
            evolutionDTO.getComplaint()
        );
        clinicalEvolution.setDoctor(doctor);
        clinicalEvolution.setCid10(cid10);
        clinicalEvolution.setSpecialty(specialty);
        clinicalEvolution.setDiagnosis(evolutionDTO.getDiagnosis());
        clinicalEvolution.setConsultationType(evolutionDTO.getConsultationType());
        clinicalEvolution.setSubject(evolutionDTO.getSubject());
        clinicalEvolution.setCreatedBy(createdBy);
        
        ClinicalEvolution savedEvolution = clinicalEvolutionRepository.save(clinicalEvolution);
        return ResponseEntity.ok(convertToDTO(savedEvolution));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicalEvolutionResponseDTO> updateClinicalEvolution(@PathVariable Long id, @RequestBody ClinicalEvolutionRequestDTO evolutionDTO) {
        return clinicalEvolutionRepository.findById(id)
                .map(evolution -> {
                    Patient patient = patientRepository.findById(evolutionDTO.getPatientId())
                            .orElseThrow(() -> new RuntimeException("Patient not found"));
                    
                    Doctor doctor = null;
                    if (evolutionDTO.getDoctorId() != null) {
                        doctor = doctorRepository.findById(evolutionDTO.getDoctorId())
                                .orElseThrow(() -> new RuntimeException("Doctor not found"));
                    }
                    
                    CID10 cid10 = null;
                    if (evolutionDTO.getCid10Id() != null) {
                        cid10 = cid10Repository.findById(evolutionDTO.getCid10Id())
                                .orElseThrow(() -> new RuntimeException("CID10 not found"));
                    }
                    
                    Specialty specialty = null;
                    if (evolutionDTO.getSpecialtyId() != null) {
                        specialty = specialtyRepository.findById(evolutionDTO.getSpecialtyId())
                                .orElseThrow(() -> new RuntimeException("Specialty not found"));
                    }
                    
                    User createdBy = null;
                    if (evolutionDTO.getCreatedBy() != null) {
                        createdBy = userRepository.findById(evolutionDTO.getCreatedBy())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                    }
                    
                    evolution.setPatient(patient);
                    evolution.setDoctor(doctor);
                    evolution.setCid10(cid10);
                    evolution.setSpecialty(specialty);
                    evolution.setAppointmentDate(evolutionDTO.getAppointmentDate());
                    evolution.setComplaint(evolutionDTO.getComplaint());
                    evolution.setDiagnosis(evolutionDTO.getDiagnosis());
                    evolution.setConsultationType(evolutionDTO.getConsultationType());
                    evolution.setSubject(evolutionDTO.getSubject());
                    evolution.setCreatedBy(createdBy);
                    
                    return ResponseEntity.ok(convertToDTO(clinicalEvolutionRepository.save(evolution)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinicalEvolution(@PathVariable Long id) {
        if (clinicalEvolutionRepository.existsById(id)) {
            clinicalEvolutionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ClinicalEvolutionResponseDTO convertToDTO(ClinicalEvolution evolution) {
        ClinicalEvolutionResponseDTO dto = new ClinicalEvolutionResponseDTO();
        dto.setId(evolution.getId());
        dto.setPatientId(evolution.getPatient().getId());
        dto.setPatientName(evolution.getPatient().getName());
        
        if (evolution.getDoctor() != null) {
            dto.setDoctorId(evolution.getDoctor().getId());
            dto.setDoctorName(evolution.getDoctor().getName());
            dto.setDoctorCrm(evolution.getDoctor().getCrm());
        }
        
        if (evolution.getCid10() != null) {
            dto.setCid10Id(evolution.getCid10().getId());
            dto.setCid10Code(evolution.getCid10().getCode());
            dto.setCid10Description(evolution.getCid10().getDescription());
        }
        
        if (evolution.getSpecialty() != null) {
            dto.setSpecialtyId(evolution.getSpecialty().getId());
            dto.setSpecialtyName(evolution.getSpecialty().getName());
        }
        
        dto.setAppointmentDate(evolution.getAppointmentDate());
        dto.setComplaint(evolution.getComplaint());
        dto.setDiagnosis(evolution.getDiagnosis());
        dto.setConsultationType(evolution.getConsultationType());
        dto.setSubject(evolution.getSubject());
        
        if (evolution.getCreatedBy() != null) {
            dto.setCreatedBy(evolution.getCreatedBy().getId());
            dto.setCreatedByName(evolution.getCreatedBy().getFullName());
        }
        
        dto.setCreatedAt(evolution.getCreatedAt());
        dto.setUpdatedAt(evolution.getUpdatedAt());
        return dto;
    }
}