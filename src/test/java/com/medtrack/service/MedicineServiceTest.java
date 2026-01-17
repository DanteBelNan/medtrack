package com.medtrack.service;

import com.medtrack.model.Medicine;
import com.medtrack.repository.MedicineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @InjectMocks
    private MedicineService medicineService;

    @Test
    void shouldReturnMedicinesWhenUserExists() {

        Long userId = 1L;
        Medicine med1 = new Medicine();
        med1.setName("Ibuprofeno");

        Medicine med2 = new Medicine();
        med2.setName("Aspirina");

        when(medicineRepository.findByUserId(userId)).thenReturn(Arrays.asList(med1, med2));

        List<Medicine> result = medicineService.findByUserId(userId);

        assertThat(result)
                .hasSize(2)
                .extracting(Medicine::getName)
                .containsExactly("Ibuprofeno", "Aspirina");
        verify(medicineRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldSaveMedicineCorrectly() {
        // Arrange
        Medicine medicineToSave = new Medicine();
        medicineToSave.setName("Paracetamol");

        Medicine savedMedicine = new Medicine();
        savedMedicine.setId(10L);
        savedMedicine.setName("Paracetamol");

        when(medicineRepository.save(any(Medicine.class))).thenReturn(savedMedicine);

        Medicine result = medicineService.save(medicineToSave);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 10L)
                .hasFieldOrPropertyWithValue("name", "Paracetamol");

        verify(medicineRepository).save(medicineToSave);
    }

    @Test
    void shouldReturnAllMedicines() {
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(new Medicine(), new Medicine()));

        List<Medicine> result = medicineService.findAll();

        assertThat(result).hasSize(2);
        verify(medicineRepository, times(1)).findAll();
    }
}