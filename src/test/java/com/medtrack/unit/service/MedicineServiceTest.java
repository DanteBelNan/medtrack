package com.medtrack.unit.service;

import com.medtrack.UnitTestBase;
import com.medtrack.dto.MedicineDTO;
import com.medtrack.mapper.MedicineMapper;
import com.medtrack.model.Medicine;
import com.medtrack.model.Role;
import com.medtrack.model.User;
import com.medtrack.repository.MedicineRepository;
import com.medtrack.repository.UserRepository;
import com.medtrack.service.MedicineService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicineServiceTest extends UnitTestBase {

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private UserRepository userRepository;

    @Spy // Usamos Spy para que use la lógica real del mapper en el test
    private MedicineMapper medicineMapper;

    @InjectMocks
    private MedicineService medicineService;

    @AfterEach
    void tearDown() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext(User user) {
        org.springframework.security.core.Authentication auth =
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        user.getEmail(), null, java.util.Collections.emptyList()
                );
        org.springframework.security.core.context.SecurityContext context =
                org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        org.springframework.security.core.context.SecurityContextHolder.setContext(context);
    }

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
        verify(medicineRepository).findByUserId(userId);
    }

    @Test
    void shouldReturnMyMedicines() {
        User user = new User();
        user.setId(1L);
        user.setEmail("johndoe@email.com");
        mockSecurityContext(user);

        when(userRepository.findByEmail("johndoe@email.com")).thenReturn(java.util.Optional.of(user));
        when(medicineRepository.findByUserId(1L)).thenReturn(Arrays.asList(new Medicine()));

        List<MedicineDTO> result = medicineService.findMyMedicines();

        assertThat(result).hasSize(1);
        verify(medicineRepository).findByUserId(1L);
    }

    @Test
    void shouldSaveMedicineCorrectly() {
        User user = new User();
        user.setId(1L);
        user.setEmail("johndoe@email.com");
        mockSecurityContext(user);

        MedicineDTO medicineDto = new MedicineDTO();
        medicineDto.setName("Paracetamol");
        medicineDto.setDaysOfWeek(Arrays.asList("MONDAY", "FRIDAY"));
        medicineDto.setIntakeTimes(Arrays.asList(LocalTime.of(8, 0), LocalTime.of(20, 0)));

        Medicine savedMedicine = new Medicine();
        savedMedicine.setId(10L);
        savedMedicine.setName("Paracetamol");
        savedMedicine.setDaysOfWeek(medicineDto.getDaysOfWeek());
        savedMedicine.setIntakeTimes(medicineDto.getIntakeTimes());
        savedMedicine.setUser(user);

        when(userRepository.findByEmail("johndoe@email.com")).thenReturn(java.util.Optional.of(user));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(savedMedicine);

        MedicineDTO result = medicineService.save(medicineDto);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getDaysOfWeek()).containsExactly("MONDAY", "FRIDAY");
        assertThat(result.getIntakeTimes()).hasSize(2);
        verify(medicineRepository).save(argThat(medicine ->
                medicine.getDaysOfWeek().contains("MONDAY") &&
                        medicine.getIntakeTimes().contains(LocalTime.of(8, 0))
        ));
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        User user = new User();
        user.setEmail("johndoe@email.com");
        mockSecurityContext(user);
        MedicineDTO medicineDto = new MedicineDTO();
        medicineDto.setName("Loratadina");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> medicineService.save(medicineDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Authenticated user not found");

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
        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);
        user.setEmail("johndoe@email.com");
        mockSecurityContext(user);

        Medicine med = new Medicine();
        med.setId(medId);
        med.setName("Ibupirac");
        med.setUser(user);
        med.setDaysOfWeek(Arrays.asList("EVERYDAY"));

        when(medicineRepository.findById(medId)).thenReturn(java.util.Optional.of(med));
        when(userRepository.findByEmail("johndoe@email.com")).thenReturn(java.util.Optional.of(user));

        MedicineDTO result = medicineService.findById(medId);

        assertThat(result).isNotNull();
        assertThat(result.getDaysOfWeek()).contains("EVERYDAY");
    }

    @Test
    void shouldThrowExceptionWhenUserAccessesAnotherUsersMedicine() {
        Long medId = 10L;
        User owner = new User();
        owner.setId(1L);

        User intruder = new User();
        intruder.setId(2L); // Diferente ID
        intruder.setRole(Role.USER);
        intruder.setEmail("intruder@email.com");
        mockSecurityContext(intruder);

        Medicine med = new Medicine();
        med.setId(medId);
        med.setUser(owner);

        when(medicineRepository.findById(medId)).thenReturn(java.util.Optional.of(med));
        when(userRepository.findByEmail("intruder@email.com")).thenReturn(java.util.Optional.of(intruder));

        assertThatThrownBy(() -> medicineService.findById(medId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unauthorized access to this medicine");
    }
}