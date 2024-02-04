package com.example.ddmdemo.service.interfaces;

import com.example.ddmdemo.dto.AddGovernmentDTO;
import com.example.ddmdemo.dto.ContractParsedDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AuthService {
    boolean addGovernment(MultipartFile documentFile, AddGovernmentDTO addGovernmentDTO, ContractParsedDataDTO contractParsedDataDTO);
}
