package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.AddGovernmentDTO;
import com.example.ddmdemo.dto.ContractParsedDataDTO;
import com.example.ddmdemo.service.interfaces.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping(value = "/government", consumes = { "multipart/form-data" })
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addGovernment(@RequestParam("documentFile") MultipartFile documentFile,
                                 @RequestParam("addGovernmentDTO") String addGovernmentDTOStr,
                                 @RequestParam("contractParsedDataDTO") String contractParsedDataDTOStr)
    {
        AddGovernmentDTO addGovernmentDTO = null;
        try {
            addGovernmentDTO = new ObjectMapper().readValue(addGovernmentDTOStr, AddGovernmentDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ContractParsedDataDTO contractParsedDataDTO = null;
        try {
            contractParsedDataDTO = new ObjectMapper().readValue(contractParsedDataDTOStr, ContractParsedDataDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return authService.addGovernment(documentFile, addGovernmentDTO, contractParsedDataDTO);
    }
}
