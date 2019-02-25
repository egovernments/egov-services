package org.egov.encryption.models;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {

    private Long id;
    private String name;
    private String jsonPath;
    private String maskingTechnique;

}
