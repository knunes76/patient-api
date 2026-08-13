package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.CID10;
import com.ebserh.patientapi.model.dto.CID10RequestDTO;
import com.ebserh.patientapi.repository.CID10Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cid10")
public class CID10Controller {

    @Autowired
    private CID10Repository cid10Repository;

    @GetMapping
    public ResponseEntity<List<CID10>> getAllCID10() {
        return ResponseEntity.ok(cid10Repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CID10> getCID10ById(@PathVariable Long id) {
        return cid10Repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CID10> createCID10(@RequestBody CID10RequestDTO cid10DTO) {
        CID10 cid10 = new CID10(cid10DTO.getCode(), cid10DTO.getDescription());
        CID10 savedCID10 = cid10Repository.save(cid10);
        return ResponseEntity.ok(savedCID10);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CID10> updateCID10(@PathVariable Long id, @RequestBody CID10RequestDTO cid10DTO) {
        return cid10Repository.findById(id)
                .map(cid10 -> {
                    cid10.setCode(cid10DTO.getCode());
                    cid10.setDescription(cid10DTO.getDescription());
                    return ResponseEntity.ok(cid10Repository.save(cid10));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCID10(@PathVariable Long id) {
        if (cid10Repository.existsById(id)) {
            cid10Repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}