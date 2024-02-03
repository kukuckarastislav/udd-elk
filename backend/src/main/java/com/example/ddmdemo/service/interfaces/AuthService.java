package com.example.ddmdemo.service.interfaces;

import com.example.ddmdemo.dto.AddGovernmentDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    boolean addGovernment(AddGovernmentDTO addGovernmentDTO);
}
