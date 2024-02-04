package com.example.ddmdemo.indexmodel;


import com.example.ddmdemo.dto.ContractParsedDataDTO;
import com.example.ddmdemo.model.TypeOfDoc;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.core.geo.GeoPoint; // ???

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = IndexUnit.INDEX_NAME)
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class IndexUnit {

    public static final String INDEX_NAME = "contracts_and_laws_index";

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String employeeName; // onaj koji je potpisao ugovor

    @Field(type = FieldType.Text, store = true, analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String employerSurname;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String governmentName;

    @Field(type = FieldType.Text, store = true)
    private String governmentLevel;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String contractText;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String lawText;

    @Field(type = FieldType.Text, store = true, index = false)
    private String fileId;

    @Field(type = FieldType.Text, store = true)
    private String title;

    @Field(type = FieldType.Text, store = true, analyzer = "serbian_simple", searchAnalyzer = "serbian_simple")
    private String address;

    @Field(store = true)
    private GeoPoint location;

    @Field(type = FieldType.Text, store = true)
    private TypeOfDoc typeOfDoc;

    public IndexUnit(ContractParsedDataDTO contractParsedDataDTO){
        var fullNameParts = contractParsedDataDTO.getEmployeeFullName().split(" ");
        if(fullNameParts.length > 1){
            this.employeeName = fullNameParts[0];
            this.employerSurname = fullNameParts[1];
        } else {
            this.employeeName = fullNameParts[0];
            this.employerSurname = fullNameParts[0];
        }

        this.governmentName = contractParsedDataDTO.getGovernmentName();
        this.governmentLevel = contractParsedDataDTO.getGovernmentLevel();
        this.address = contractParsedDataDTO.getGovernmentAddress();
        this.typeOfDoc = TypeOfDoc.CONTRACT;
    }

}
