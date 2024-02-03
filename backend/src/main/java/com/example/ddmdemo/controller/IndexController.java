package com.example.ddmdemo.controller;

import com.example.ddmdemo.dto.*;
import com.example.ddmdemo.service.interfaces.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexingService indexingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DummyDocumentFileResponseDTO addDocumentFile(
        @ModelAttribute DummyDocumentFileDTO documentFile) {
        // OVO NE KORISTIMO MOZE DELETE :D
        var serverFilename = indexingService.indexDocument(documentFile.file());
        return new DummyDocumentFileResponseDTO(serverFilename);
    }

    @PostMapping("/contract/parse")
    @ResponseStatus(HttpStatus.OK)
    public ContractParsedDataDTO parseContract(@RequestBody MultipartFile documentFile) {
        return indexingService.parseContract(documentFile);
    }

    @PostMapping("/contract")
    @ResponseStatus(HttpStatus.CREATED)
    public String addContract(@RequestBody AddContractDTO addContractDTO) {
        var serverFilename = indexingService.indexContractAndSaveFile(addContractDTO.documentFile(),
                addContractDTO.contractParsedDataDTO());
        return serverFilename;
    }

    @PostMapping(value = "/law", consumes = { "multipart/form-data" })
    @ResponseStatus(HttpStatus.CREATED)
    public String addLaw(@RequestParam("documentFile") MultipartFile documentFile) {
        var serverFilename = indexingService.indexLawAndSaveFile(documentFile);
        return serverFilename;
    }
}
