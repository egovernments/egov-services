package org.egov.tenant.domain.model;

import lombok.*;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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

    public boolean isValid() {
        return !isNameAbsent();
    }

    public boolean isNameAbsent() {
        return isEmpty(name);
    }
}
