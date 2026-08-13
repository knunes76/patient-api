package com.ebserh.patientapi.repository;

import com.ebserh.patientapi.model.Unity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnityRepository extends JpaRepository<Unity, Long> {
}