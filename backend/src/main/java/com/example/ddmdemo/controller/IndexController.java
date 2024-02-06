package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.*;
import com.example.ddmdemo.service.interfaces.IndexingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexingService indexingService;

    @PostMapping("/contract/parse")
    @ResponseStatus(HttpStatus.OK)
    public ContractParsedDataDTO parseContract(@RequestBody MultipartFile documentFile) {
        return indexingService.parseContract(documentFile);
    }


    @PostMapping(value = "/contract", consumes = { "multipart/form-data" })
    @ResponseStatus(HttpStatus.CREATED)
    public String addContract(@RequestParam("documentFile") MultipartFile documentFile,
                              @RequestParam("contractParsedDataDTO") String contractParsedDataDTOStr) {

        ContractParsedDataDTO contractParsedDataDTO = null;
        try {
            contractParsedDataDTO = new ObjectMapper().readValue(contractParsedDataDTOStr, ContractParsedDataDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var serverFilename = indexingService.indexContractAndSaveFile(documentFile, contractParsedDataDTO);
        return serverFilename;
    }

    @PostMapping(value = "/law", consumes = { "multipart/form-data" })
    @ResponseStatus(HttpStatus.CREATED)
    public String addLaw(@RequestParam("documentFile") MultipartFile documentFile) {
        var serverFilename = indexingService.indexLawAndSaveFile(documentFile);
        return serverFilename;
    }
}
