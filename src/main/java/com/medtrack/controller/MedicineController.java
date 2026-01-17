package com.medtrack.controller;

import com.medtrack.model.Medicine;
import com.medtrack.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineRepository repository;

    @GetMapping
    public List<Medicine> list() {
        return repository.findAll();
    }

    @PostMapping
    public Medicine create(@RequestBody Medicine medicine) {
        return repository.save(medicine);
    }
}