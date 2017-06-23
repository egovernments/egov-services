package org.egov.pgr.notification.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    private String grade;
    private String id;
    private String name;
    private String localName;
    private String districtCode;
    private String districtName;
    private String regionName;
    private Double longitude;
    private Double latitude;
    private String code;
    private String ulbName;
}