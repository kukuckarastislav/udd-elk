package com.example.ddmdemo.dto;

import org.springframework.web.multipart.MultipartFile;

public record AddLawDTO(MultipartFile documentFile) {
}
