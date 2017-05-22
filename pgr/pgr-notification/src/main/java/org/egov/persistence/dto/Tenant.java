package org.egov.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tenant {
    private static final String CITY_NAME_NOT_PRESENT_MESSAGE = "MISSING CITY NAME";
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private String type;
    private City city;

    public String getCityName() {
        return city == null ? CITY_NAME_NOT_PRESENT_MESSAGE : city.getName();
    }
}