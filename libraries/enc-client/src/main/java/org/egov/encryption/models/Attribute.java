package org.egov.encryption.models;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {

    private String jsonPath;
    private String maskingTechnique;

    @Override
    public String toString() {
        return "JsonPath : " + jsonPath + ", MaskingTechnique : " + maskingTechnique;
    }
}