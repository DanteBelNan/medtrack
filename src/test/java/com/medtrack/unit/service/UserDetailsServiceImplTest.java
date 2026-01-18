package com.medtrack.unit.service;

import com.medtrack.UnitTestBase;
import com.medtrack.model.Role;
import com.medtrack.model.User;
import com.medtrack.repository.UserRepository;
import com.medtrack.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest extends UnitTestBase {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldLoadUserByUsernameWhenUserExists() {
        User user = new User();
        user.setEmail("johndoe@email.com");
        user.setPassword("encoded_password");
        user.setRole(Role.USER);

        when(userRepository.findByEmail("johndoe@email.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("johndoe@email.com");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("johndoe@email.com");
        assertThat(userDetails.getPassword()).isEqualTo("encoded_password");
        assertThat(userDetails.getAuthorities()).isNotEmpty();
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findByEmail("doesnotexist@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("doesnotexist@test.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}