package com.example.ddmdemo.service.impl;

import com.example.ddmdemo.dto.AddGovernmentDTO;
import com.example.ddmdemo.model.Government;
import com.example.ddmdemo.service.interfaces.AuthService;
import com.example.ddmdemo.service.interfaces.FileService;
import com.example.ddmdemo.service.interfaces.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.ddmdemo.respository.GovernmentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final IndexingService indexingService;
    private final GovernmentRepository governmentRepository;
    private final FileService fileService;


    @Override
    public boolean addGovernment(AddGovernmentDTO addGovernmentDTO) {

        // storing the document in the database MINIO
        String fileId = fileService.store(addGovernmentDTO.getDocumentFile(), UUID.randomUUID().toString()); // 000.pdf

        // creating record in the postgres database for new government
        Government newGovernment = new Government(addGovernmentDTO);
        var savedGovernment = governmentRepository.save(newGovernment);

        // indexing the Contract document
        var titleOfDoc = indexingService.indexContract(
                addGovernmentDTO.getDocumentFile(),
                addGovernmentDTO.getContractParsedDataDTO(),
                fileId);

        return true;
    }
}
