package com.example.ddmdemo.service.impl;

import com.example.ddmdemo.dto.ContractParsedDataDTO;
import com.example.ddmdemo.exceptionhandling.exception.LoadingException;
import com.example.ddmdemo.exceptionhandling.exception.StorageException;
import com.example.ddmdemo.indexmodel.IndexUnit;
import com.example.ddmdemo.model.TypeOfDoc;
import com.example.ddmdemo.service.interfaces.FileService;
import com.example.ddmdemo.service.interfaces.IndexingService;
import com.example.ddmdemo.utils.GeoSearchAPI;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.ddmdemo.indexrepository.IndexRepository;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    private final IndexRepository indexRepository;
    private final FileService fileService;
    private final LanguageDetector languageDetector;


    @Override
    @Transactional
    public String indexContract(MultipartFile documentFile, ContractParsedDataDTO contractParsedDataDTO, String fileId) {
        IndexUnit newContract = new IndexUnit(contractParsedDataDTO);
        newContract.setFileId(fileId);
        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newContract.setTitle(title);

        var documentContent = extractDocumentContent(documentFile);
        newContract.setContractText(documentContent);

        var geo = GeoSearchAPI.getGeoLocation(contractParsedDataDTO.getGovernmentAddress());
        System.out.println("ADRESA: "+ contractParsedDataDTO.getGovernmentAddress() + " lat:" +geo.getLat() + " lon:" + geo.getLon());
        newContract.setLocation(new GeoPoint(geo.getLat(), geo.getLon()));

        indexRepository.save(newContract);

        return title;
    }

    @Override
    public String indexContractAndSaveFile(MultipartFile documentFile, ContractParsedDataDTO contractParsedDataDTO) {

        var fileId = fileService.store(documentFile, UUID.randomUUID().toString());

        IndexUnit newContract = new IndexUnit(contractParsedDataDTO);
        newContract.setFileId(fileId);
        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newContract.setTitle(title);

        var documentContent = extractDocumentContent(documentFile);
        newContract.setContractText(documentContent);

        var geo = GeoSearchAPI.getGeoLocation(contractParsedDataDTO.getGovernmentAddress());
        System.out.println("ADRESA: "+ contractParsedDataDTO.getGovernmentAddress() + " lat:" +geo.getLat() + " lon:" + geo.getLon());
        newContract.setLocation(new GeoPoint(geo.getLat(), geo.getLon()));

        indexRepository.save(newContract);

        return title;
    }

    @Override
    public String indexLawAndSaveFile(MultipartFile documentFile) {
        var fileId = fileService.store(documentFile, UUID.randomUUID().toString());

        IndexUnit newContract = new IndexUnit();
        newContract.setTypeOfDoc(TypeOfDoc.LAW);
        newContract.setFileId(fileId);
        var title = Objects.requireNonNull(documentFile.getOriginalFilename()).split("\\.")[0];
        newContract.setTitle(title);

        var documentContent = extractDocumentContent(documentFile);
        newContract.setLawText(documentContent);

        indexRepository.save(newContract);

        return title;
    }

    @Override
    public ContractParsedDataDTO parseContract(MultipartFile documentFile) {
        var parsed = new ContractParsedDataDTO();

        String documentContent = extractDocumentContent(documentFile);

        var dc1 = documentContent.replaceAll("\r", "");
        var lines = dc1.split("\n");

        for(int i = 0; i < lines.length; i++) {
            if(lines[i].equals("Agencija za objavljivanje zakona ")) {
                if(i+4 < lines.length && lines[i+4].contains("Uprava za:")){
                    parsed.setGovernmentName(lines[i+4].replace("Uprava za: ", "").trim());
                }

                if(i+5 < lines.length && lines[i+5].contains("Nivo Uprave:")){
                    parsed.setGovernmentLevel(lines[i+5].replace("Nivo Uprave: ", "").trim());
                }

                if(i+6 < lines.length){
                    parsed.setGovernmentAddress(lines[i+6].trim());
                }
            }

            if(i-1 >= 0 && lines[i].equals("Potpisanik ugovora za agenciju ")) {
                parsed.setEmployeeFullName(lines[i-1].trim());
            }
        }

        return parsed;
    }

    private String extractDocumentContent(MultipartFile multipartFile) {
        String mimeType = detectMimeType(multipartFile);
        String documentContent;

        if(mimeType.equals("application/pdf")){
            try (var pdfFile = multipartFile.getInputStream()) {
                var pdDocument = PDDocument.load(pdfFile);
                var textStripper = new PDFTextStripper();
                documentContent = textStripper.getText(pdDocument);
                pdDocument.close();
            } catch (IOException e) {
                throw new LoadingException("Error while trying to load PDF file content.");
            }
        }else{
            throw new LoadingException("File format are not supported.");
        }

        return documentContent;
    }

    private String detectLanguage(String text) {
        var detectedLanguage = languageDetector.detect(text).getLanguage().toUpperCase();
        if (detectedLanguage.equals("HR")) {
            detectedLanguage = "SR";
        }

        return detectedLanguage;
    }

    private String detectMimeType(MultipartFile file) {
        var contentAnalyzer = new Tika();

        String trueMimeType;
        String specifiedMimeType;
        try {
            trueMimeType = contentAnalyzer.detect(file.getBytes());
            specifiedMimeType =
                Files.probeContentType(Path.of(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (IOException e) {
            throw new StorageException("Failed to detect mime type for file.");
        }

        if (!trueMimeType.equals(specifiedMimeType) &&
            !(trueMimeType.contains("zip") && specifiedMimeType.contains("zip"))) {
            throw new StorageException("True mime type is different from specified one, aborting.");
        }

        return trueMimeType;
    }
}