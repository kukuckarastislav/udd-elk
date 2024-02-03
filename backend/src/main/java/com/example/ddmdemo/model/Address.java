package com.example.ddmdemo.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String street;
    private String number;
    private String city;
    private String country;
    private String postalCode;


    @Override
    public String toString() {
        return "Address{" +
            "street='" + street + '\'' +
            ", number='" + number + '\'' +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            ", postalCode='" + postalCode + '\'' +
            '}';
    }
}
