package com.example.ddmdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractParsedDataDTO {

    String employeeFullName;
    String governmentName;
    String governmentLevel;
    String governmentAddress;
}
