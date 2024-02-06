package com.example.ddmdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDTO {
    private String employeeName;
    private String employerSurname;
    private String governmentName;
    private String governmentLevel;
    private String fullText;
    private String booleanQuery;
    private boolean lawDoc;
    private boolean contractDoc;
    private String typeOfSearch;
    private double radius;
    private String address;
}
