package org.egov.pgr.notification.persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tenant {
    private static final String CITY_NAME_NOT_PRESENT_MESSAGE = "MISSING CITY NAME";
    private static final String ULB_GRADE_NOT_PRESENT_MESSAGE = "MISSING ULB Grade";
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

    public String getUlbGrade() {
        return city == null ? ULB_GRADE_NOT_PRESENT_MESSAGE : city.getGrade();
    }
}