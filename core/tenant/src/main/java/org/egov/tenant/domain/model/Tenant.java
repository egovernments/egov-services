package org.egov.tenant.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Tenant {

    private Long id;
    private String code;
    private String name;
    private double longitude;
    private double latitude;
    private String description;
    private Long logoId;
    private Long backgroundId;
    private String domainUrl;
    private String regionName;
    private String districtCode;
    private String districtName;
    private String grade;
    private boolean active;
    private String localName;
}
