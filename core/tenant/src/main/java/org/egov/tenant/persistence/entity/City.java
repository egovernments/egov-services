package org.egov.tenant.persistence.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class City {

    public static String
        ID = "id",
        NAME = "name",
        LOCAL_NAME = "localname",
        DISTRICT_CODE = "districtcode",
        DISTRICT_NAME = "districtname",
        REGION_NAME = "regionname",
        LONGITUDE = "longitude",
        LATITUDE = "latitude",
        TENANT_CODE = "tenantcode",
        CREATED_BY = "createdby",
        CREATED_DATE = "createddate",
        LAST_MODIFIED_BY = "lastmodifiedby",
        LAST_MODIFIED_DATE = "lastmodifieddate";

    private Long id;
    private String name;
    private String localName;
    private String districtCode;
    private String districtName;
    private String regionName;
    private Double longitude;
    private Double latitude;
    private String tenantCode;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;

    public org.egov.tenant.domain.model.City toDomain() {
        return org.egov.tenant.domain.model.City.builder()
            .id(id)
            .name(name)
            .localName(localName)
            .districtCode(districtCode)
            .districtName(districtName)
            .regionName(regionName)
            .longitude(longitude)
            .latitude(latitude)
            .tenantCode(tenantCode)
            .createdBy(createdBy)
            .createdDate(createdDate)
            .lastModifiedBy(lastModifiedBy)
            .lastModifiedDate(lastModifiedDate)
            .build();
    }
}
