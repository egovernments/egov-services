package org.egov.tenant.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {

    private String name;
    private String localName;
    private String districtCode;
    private String districtName;
    private String regionName;
    private String ulbGrade;
    private Double longitude;
    private Double latitude;
    private String shapeFileLocation;
    private String captcha;
    private String code;
    
}