package com.ebserh.patientapi.controller;

import com.ebserh.patientapi.model.Unity;
import com.ebserh.patientapi.model.dto.UnityRequestDTO;
import com.ebserh.patientapi.repository.UnityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unity")
public class UnityController {

    @Autowired
    private UnityRepository unityRepository;

    @GetMapping
    public ResponseEntity<List<Unity>> getAllUnities() {
        return ResponseEntity.ok(unityRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unity> getUnityById(@PathVariable Long id) {
        return unityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Unity> createUnity(@RequestBody UnityRequestDTO unityDTO) {
        Unity unity = new Unity(unityDTO.getName());
        Unity savedUnity = unityRepository.save(unity);
        return ResponseEntity.ok(savedUnity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unity> updateUnity(@PathVariable Long id, @RequestBody UnityRequestDTO unityDTO) {
        return unityRepository.findById(id)
                .map(unity -> {
                    unity.setName(unityDTO.getName());
                    return ResponseEntity.ok(unityRepository.save(unity));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnity(@PathVariable Long id) {
        if (unityRepository.existsById(id)) {
            unityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}