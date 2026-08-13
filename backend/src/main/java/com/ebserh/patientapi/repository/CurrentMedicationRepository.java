package com.ebserh.patientapi.repository;

import com.ebserh.patientapi.model.CurrentMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrentMedicationRepository extends JpaRepository<CurrentMedication, Long> {
    List<CurrentMedication> findByPatientId(Long patientId);
    List<CurrentMedication> findByPatientIdOrderByStartDateDesc(Long patientId);
}