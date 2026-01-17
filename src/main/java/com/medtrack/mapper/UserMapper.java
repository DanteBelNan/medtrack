package com.medtrack.mapper;

import com.medtrack.dto.UserDTO;
import com.medtrack.dto.UserRegistrationDTO;
import com.medtrack.model.Medicine;
import com.medtrack.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        if (user.getMedicines() != null) {
            dto.setMedicineIds(user.getMedicines().stream()
                    .map(Medicine::getId)
                    .collect(Collectors.toList()));
        } else {
            dto.setMedicineIds(new ArrayList<>());
        }

        return dto;
    }

    public User toEntity(UserRegistrationDTO registrationDto) {
        if (registrationDto == null) return null;

        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());

        return user;
    }
}