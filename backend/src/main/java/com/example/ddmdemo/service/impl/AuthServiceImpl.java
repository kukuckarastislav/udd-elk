package com.example.ddmdemo.service.impl;

import com.example.ddmdemo.dto.AddGovernmentDTO;
import com.example.ddmdemo.dto.ContractParsedDataDTO;
import com.example.ddmdemo.model.Government;
import com.example.ddmdemo.service.interfaces.AuthService;
import com.example.ddmdemo.service.interfaces.FileService;
import com.example.ddmdemo.service.interfaces.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.ddmdemo.respository.GovernmentRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final IndexingService indexingService;
    private final GovernmentRepository governmentRepository;
    private final FileService fileService;

    @Override
    public boolean addGovernment(MultipartFile documentFile, AddGovernmentDTO addGovernmentDTO, ContractParsedDataDTO contractParsedDataDTO) {
        String fileId = fileService.store(documentFile, UUID.randomUUID().toString()); // 000.pdf

        // creating record in the postgres database for new government
        Government newGovernment = new Government(addGovernmentDTO);
        var savedGovernment = governmentRepository.save(newGovernment);

        // indexing the Contract document
        var titleOfDoc = indexingService.indexContract(documentFile, contractParsedDataDTO, fileId);

        return true;
    }
}
