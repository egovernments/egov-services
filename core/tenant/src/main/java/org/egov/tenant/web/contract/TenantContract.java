package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.tenant.domain.model.Tenant;

/**
 * Created by parvati on 11/4/17.
 */
@Getter
@AllArgsConstructor
public class TenantContract {

    private String code;
    private String name;
    private double longitude;
    private double latitude;
    private String domainurl;
    private String localName;
    private boolean active;
    private Long logoId;
    private Long backgroundImageId;
    private String grade;
    private String districtCode;
    private String districtName;
    private String regionName;

    public Tenant toDomain() {
        return null;
    }

}
