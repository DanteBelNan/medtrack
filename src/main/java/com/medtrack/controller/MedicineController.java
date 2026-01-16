package com.medtrack.controller;

import com.medtrack.model.Medicine;
import com.medtrack.repository.RemedioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/remedios")
public class RemedioController {

    @Autowired
    private RemedioRepository repository;

    @GetMapping
    public List<Medicine> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Medicine crear(@RequestBody Medicine medicine) {
        return repository.save(medicine);
    }
}