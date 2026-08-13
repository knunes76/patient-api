package com.ebserh.patientapi.service;

import com.ebserh.patientapi.exception.ResourceAlreadyExistsException;
import com.ebserh.patientapi.exception.ResourceNotFoundException;
import com.ebserh.patientapi.model.Patient;
import com.ebserh.patientapi.model.Unity;
import com.ebserh.patientapi.model.dto.PatientRequestDTO;
import com.ebserh.patientapi.model.dto.PatientResponseDTO;
import com.ebserh.patientapi.repository.PatientRepository;
import com.ebserh.patientapi.repository.UnityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UnityRepository unityRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientRequestDTO patientDTO;
    private Unity unity;

    @BeforeEach
    void setUp() {
        unity = new Unity();
        unity.setId(1L);
        unity.setName("Hospital Univ. de BH");

        patient = new Patient();
        patient.setId(1L);
        patient.setName("John Doe");
        patient.setCpf("12345678901");
        patient.setEmail("john.doe@example.com");
        patient.setPhone("11987654321");
        patient.setBirthDate(LocalDate.of(1990, 1, 1));
        patient.setUnity(unity);

        patientDTO = new PatientRequestDTO();
        patientDTO.setName("John Doe");
        patientDTO.setCpf("12345678901");
        patientDTO.setEmail("john.doe@example.com");
        patientDTO.setPhone("11987654321");
        patientDTO.setBirthDate(LocalDate.of(1990, 1, 1));
        patientDTO.setUnityId(1L);
    }

    @Test
    void createPatient_Success() {
        when(patientRepository.existsByCpf(anyString())).thenReturn(false);
        when(patientRepository.existsByEmail(anyString())).thenReturn(false);
        when(unityRepository.findById(anyLong())).thenReturn(Optional.of(unity));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientResponseDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("12345678901", result.getCpf());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void createPatient_CpfAlreadyExists_ThrowsException() {
        when(patientRepository.existsByCpf(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> patientService.createPatient(patientDTO));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void createPatient_EmailAlreadyExists_ThrowsException() {
        when(patientRepository.existsByCpf(anyString())).thenReturn(false);
        when(patientRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> patientService.createPatient(patientDTO));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void getPatientById_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        PatientResponseDTO result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientById_NotFound_ThrowsException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(1L));
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientByCpf_Success() {
        when(patientRepository.findByCpf("12345678901")).thenReturn(Optional.of(patient));

        PatientResponseDTO result = patientService.getPatientByCpf("12345678901");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(patientRepository, times(1)).findByCpf("12345678901");
    }

    @Test
    void getPatientByCpf_NotFound_ThrowsException() {
        when(patientRepository.findByCpf("12345678901")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientByCpf("12345678901"));
        verify(patientRepository, times(1)).findByCpf("12345678901");
    }

    @Test
    void getAllPatients_Success() {
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Jane Smith");
        patient2.setCpf("98765432109");
        patient2.setEmail("jane.smith@example.com");
        patient2.setPhone("11912345678");
        patient2.setBirthDate(LocalDate.of(1985, 5, 15));

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient, patient2));

        List<PatientResponseDTO> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getAllPatients_Pageable_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> patientPage = new PageImpl<>(Arrays.asList(patient));

        when(patientRepository.findAll(pageable)).thenReturn(patientPage);

        Page<PatientResponseDTO> result = patientService.getAllPatients(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).getName());
        verify(patientRepository, times(1)).findAll(pageable);
    }

    @Test
    void updatePatient_Success() {
        Patient existingPatient = new Patient();
        existingPatient.setId(1L);
        existingPatient.setName("John Doe");
        existingPatient.setCpf("12345678901");
        existingPatient.setEmail("john.doe@example.com");
        existingPatient.setPhone("11987654321");
        existingPatient.setBirthDate(LocalDate.of(1990, 1, 1));
        existingPatient.setUnity(unity);

        patientDTO.setName("John Updated");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(unityRepository.findById(anyLong())).thenReturn(Optional.of(unity));
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

        PatientResponseDTO result = patientService.updatePatient(1L, patientDTO);

        assertNotNull(result);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_NotFound_ThrowsException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(1L, patientDTO));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deletePatient_Success() {
        when(patientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(patientRepository).deleteById(1L);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatient_NotFound_ThrowsException() {
        when(patientRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatient(1L));
        verify(patientRepository, never()).deleteById(1L);
    }

    @Test
    void searchPatientsByName_Success() {
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Johnny Smith");
        patient2.setCpf("98765432109");
        patient2.setEmail("johnny.smith@example.com");
        patient2.setPhone("11912345678");
        patient2.setBirthDate(LocalDate.of(1985, 5, 15));

        when(patientRepository.findByNameContainingIgnoreCase("John")).thenReturn(Arrays.asList(patient, patient2));

        List<PatientResponseDTO> result = patientService.searchPatientsByName("John");

        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findByNameContainingIgnoreCase("John");
    }
}
