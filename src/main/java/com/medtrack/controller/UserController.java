package com.medtrack.controller;

import com.medtrack.dto.UserDTO;
import com.medtrack.dto.UserRegistrationDTO;
import com.medtrack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegistrationDTO registrationDTO) {
        return userService.register(registrationDTO);
    }

    @GetMapping
    public List<UserDTO> list() {
        return userService.findAll();
    }
}