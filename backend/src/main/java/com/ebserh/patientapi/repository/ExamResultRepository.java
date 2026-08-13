package com.ebserh.patientapi.repository;

import com.ebserh.patientapi.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByPatientId(Long patientId);
    List<ExamResult> findByPatientIdOrderByResultDateDesc(Long patientId);
}