package com.medtrack.service;

import com.medtrack.dto.UserDTO;
import com.medtrack.dto.UserRegistrationDTO;
import com.medtrack.mapper.UserMapper;
import com.medtrack.model.User;
import com.medtrack.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Instancia para hashear
    }

    public UserDTO register(UserRegistrationDTO registrationDto) {
        User user = userMapper.toEntity(registrationDto);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
}