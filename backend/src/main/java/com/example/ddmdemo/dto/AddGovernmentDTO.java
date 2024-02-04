package com.example.ddmdemo.dto;

import com.example.ddmdemo.model.Address;
import com.example.ddmdemo.model.GovLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddGovernmentDTO {
    String name;
    GovLevel govLevel;
    String password;

    String street;
    String number;
    String city;
    String country;
    String postalCode;

    int numberOfEmployees;
}
