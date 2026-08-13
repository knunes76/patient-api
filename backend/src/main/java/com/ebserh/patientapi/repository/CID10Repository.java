package com.ebserh.patientapi.repository;

import com.ebserh.patientapi.model.CID10;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CID10Repository extends JpaRepository<CID10, Long> {
}