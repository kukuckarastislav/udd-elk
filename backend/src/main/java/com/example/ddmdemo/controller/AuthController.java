package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.AddGovernmentDTO;
import com.example.ddmdemo.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addGovernment(@RequestBody AddGovernmentDTO addGovernmentDTO) {
        return authService.addGovernment(addGovernmentDTO);
    }
}
