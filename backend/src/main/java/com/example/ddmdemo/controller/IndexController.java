package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.AddContractDTO;
import com.example.ddmdemo.dto.DummyDocumentFileDTO;
import com.example.ddmdemo.dto.DummyDocumentFileResponseDTO;
import com.example.ddmdemo.service.interfaces.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexingService indexingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DummyDocumentFileResponseDTO addDocumentFile(
        @ModelAttribute DummyDocumentFileDTO documentFile) {
        var serverFilename = indexingService.indexDocument(documentFile.file());
        return new DummyDocumentFileResponseDTO(serverFilename);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addContract(@RequestBody AddContractDTO addContractDTO) {
        var serverFilename = indexingService.indexContractAndSaveFile(addContractDTO.documentFile(), addContractDTO.contractParsedDataDTO());
        return serverFilename;
    }
}
