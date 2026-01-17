package com.medtrack.service;

import com.medtrack.dto.MedicineDTO;
import com.medtrack.mapper.MedicineMapper;
import com.medtrack.model.Medicine;
import com.medtrack.model.User;
import com.medtrack.repository.MedicineRepository;
import com.medtrack.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private UserRepository userRepository;

    @Spy // Usamos Spy para que use la lógica real del mapper en el test
    private MedicineMapper medicineMapper;

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

        List<MedicineDTO> result = medicineService.findByUserId(userId);

        assertThat(result)
                .hasSize(2)
                .extracting(MedicineDTO::getName)
                .containsExactly("Ibuprofeno", "Aspirina");
        verify(medicineRepository, times(1)).findByUserId(userId);
    }

    @Test
    void shouldSaveMedicineCorrectly() {
        Long userId = 1L;
        MedicineDTO medicineDto = new MedicineDTO();
        medicineDto.setName("Paracetamol");
        medicineDto.setUserId(userId);

        User user = new com.medtrack.model.User();
        user.setId(userId);

        Medicine savedMedicine = new Medicine();
        savedMedicine.setId(10L);
        savedMedicine.setName("Paracetamol");
        savedMedicine.setUser(user);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(savedMedicine);

        MedicineDTO result = medicineService.save(medicineDto);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 10L)
                .hasFieldOrPropertyWithValue("userId", userId);

        verify(userRepository).findById(userId);
        verify(medicineRepository).save(any(Medicine.class));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        Long userId = 99L;
        MedicineDTO medicineDto = new MedicineDTO();
        medicineDto.setUserId(userId);
        medicineDto.setName("Loratadina");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> medicineService.save(medicineDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(medicineRepository, never()).save(any(Medicine.class));
    }

    @Test
    void shouldReturnAllMedicines() {
        when(medicineRepository.findAll()).thenReturn(Arrays.asList(new Medicine(), new Medicine()));

        List<MedicineDTO> result = medicineService.findAll();

        assertThat(result).hasSize(2);
        verify(medicineRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnMedicineWhenIdExists() {
        Long medId = 10L;
        Medicine med = new Medicine();
        med.setId(medId);
        med.setName("Ibupirac");

        when(medicineRepository.findById(medId)).thenReturn(java.util.Optional.of(med));

        MedicineDTO result = medicineService.findById(medId);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Ibupirac");
        verify(medicineRepository).findById(medId);
    }
}