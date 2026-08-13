package com.ebserh.patientapi.repository;

import com.ebserh.patientapi.model.ClinicalEvolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalEvolutionRepository extends JpaRepository<ClinicalEvolution, Long> {
    List<ClinicalEvolution> findByPatientId(Long patientId);
    List<ClinicalEvolution> findByPatientIdOrderByAppointmentDateDesc(Long patientId);
}