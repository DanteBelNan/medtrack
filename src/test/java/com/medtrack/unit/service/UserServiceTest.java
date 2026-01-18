package com.medtrack.unit.service;

import com.medtrack.UnitTestBase;
import com.medtrack.dto.UserDTO;
import com.medtrack.dto.UserRegistrationDTO;
import com.medtrack.mapper.UserMapper;
import com.medtrack.model.User;
import com.medtrack.repository.UserRepository;
import com.medtrack.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends UnitTestBase {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldHashPasswordOnRegistration() {
        UserRegistrationDTO regDto = new UserRegistrationDTO();
        regDto.setName("John Doe");
        regDto.setEmail("johndoe@email.com");
        regDto.setPassword("plainTextPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("John Doe");
        savedUser.setEmail("johndoe@email.com");
        savedUser.setPassword("$2a$10$encodedHash...");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.register(regDto);

        assertThat(result.getEmail()).isEqualTo("johndoe@email.com");
        verify(userRepository).save(argThat(user ->
                !user.getPassword().equals("plainTextPassword") &&
                        user.getPassword().startsWith("$2a$")
        ));
    }
}