package com.ebserh.patientapi.service;

import com.ebserh.patientapi.exception.ResourceAlreadyExistsException;
import com.ebserh.patientapi.exception.ResourceNotFoundException;
import com.ebserh.patientapi.model.Patient;
import com.ebserh.patientapi.model.dto.PatientRequestDTO;
import com.ebserh.patientapi.model.dto.PatientResponseDTO;
import com.ebserh.patientapi.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientDTO) {
        if (patientRepository.existsByCpf(patientDTO.getCpf())) {
            throw new ResourceAlreadyExistsException("Patient with CPF " + patientDTO.getCpf() + " already exists");
        }

        if (patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Patient with email " + patientDTO.getEmail() + " already exists");
        }

        Patient patient = convertToEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return convertToDTO(savedPatient);
    }

    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return convertToDTO(patient);
    }

    public PatientResponseDTO getPatientByCpf(String cpf) {
        Patient patient = patientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with CPF: " + cpf));
        return convertToDTO(patient);
    }

    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<PatientResponseDTO> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientDTO) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (!existingPatient.getCpf().equals(patientDTO.getCpf()) && 
            patientRepository.existsByCpf(patientDTO.getCpf())) {
            throw new ResourceAlreadyExistsException("Patient with CPF " + patientDTO.getCpf() + " already exists");
        }

        if (!existingPatient.getEmail().equals(patientDTO.getEmail()) && 
            patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Patient with email " + patientDTO.getEmail() + " already exists");
        }

        updateEntityFromDTO(existingPatient, patientDTO);
        Patient updatedPatient = patientRepository.save(existingPatient);
        return convertToDTO(updatedPatient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }

    public List<PatientResponseDTO> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Patient convertToEntity(PatientRequestDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setCpf(dto.getCpf());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setBirthDate(dto.getBirthDate());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setCity(dto.getCity());
        patient.setState(dto.getState());
        patient.setZipCode(dto.getZipCode());
        patient.setBloodType(dto.getBloodType());
        patient.setAllergies(dto.getAllergies());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setEmergencyPhone(dto.getEmergencyPhone());
        return patient;
    }

    private PatientResponseDTO convertToDTO(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId());
        dto.setName(patient.getName());
        dto.setCpf(patient.getCpf());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setBirthDate(patient.getBirthDate());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setCity(patient.getCity());
        dto.setState(patient.getState());
        dto.setZipCode(patient.getZipCode());
        dto.setBloodType(patient.getBloodType());
        dto.setAllergies(patient.getAllergies());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setEmergencyContact(patient.getEmergencyContact());
        dto.setEmergencyPhone(patient.getEmergencyPhone());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());
        return dto;
    }

    private void updateEntityFromDTO(Patient patient, PatientRequestDTO dto) {
        patient.setName(dto.getName());
        patient.setCpf(dto.getCpf());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        patient.setBirthDate(dto.getBirthDate());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setCity(dto.getCity());
        patient.setState(dto.getState());
        patient.setZipCode(dto.getZipCode());
        patient.setBloodType(dto.getBloodType());
        patient.setAllergies(dto.getAllergies());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setEmergencyPhone(dto.getEmergencyPhone());
    }
}
