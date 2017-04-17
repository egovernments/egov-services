package org.egov.tenant.persistence.entity;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class City {

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

    public enum Fields {
        ID("id"),
        NAME("name"),
        LOCAL_NAME("localname"),
        DISTRICT_CODE("districtcode"),
        DISTRICT_NAME("districtname"),
        REGION_NAME("regionname"),
        LONGITUDE("longitude"),
        LATITUDE("latitude"),
        TENANT_CODE("tenantcode"),
        CREATED_BY("createdby"),
        CREATED_DATE("createddate"),
        LAST_MODIFIED_BY("lastmodifiedby"),
        LAST_MODIFIED_DATE("lastmodifieddate");

        private String value;

        Fields(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
