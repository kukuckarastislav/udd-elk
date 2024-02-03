package com.example.ddmdemo.model;

import com.example.ddmdemo.dto.AddGovernmentDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Government {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Unique
    private String name;

    private GovLevel govLevel;

    @Embedded
    private Address address;

    private int numberOfEmployees; // koji ce koristiti softver


    public Government(AddGovernmentDTO addGovernmentDTO) {
        this.name = addGovernmentDTO.getName();
        this.govLevel = addGovernmentDTO.getGovLevel();
        this.address = addGovernmentDTO.getAddress();
        this.numberOfEmployees = addGovernmentDTO.getNumberOfEmployees();
    }
}
