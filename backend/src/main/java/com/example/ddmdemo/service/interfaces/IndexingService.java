package com.example.ddmdemo.service.interfaces;

import com.example.ddmdemo.dto.ContractParsedDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface IndexingService {

    String indexDocument(MultipartFile documentFile);

    String indexContract(MultipartFile documentFile, ContractParsedDataDTO contractParsedDataDTO, String fileId);
    String indexContractAndSaveFile(MultipartFile documentFile, ContractParsedDataDTO contractParsedDataDTO);
}
