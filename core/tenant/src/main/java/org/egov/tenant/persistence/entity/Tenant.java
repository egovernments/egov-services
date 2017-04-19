package org.egov.tenant.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import org.egov.tenant.domain.model.TenantType;

import java.util.Date;

@Getter
@Builder
public class Tenant {

    public static String ID = "id",
        CODE = "code",
        DESCRIPTION = "description",
        LOGO_ID = "logoid",
        IMAGE_ID = "imageid",
        DOMAIN_URL = "domainurl",
        TYPE = "type",
        CREATED_BY = "createdby",
        CREATED_DATE = "createddate",
        LAST_MODIFIED_BY = "lastmodifiedby",
        LAST_MODIFIED_DATE = "lastmodifieddate";

    private Long id;
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private TenantType type;
    private Long createdBy;
    private Date createdDate;
    private Long lastModifiedBy;
    private Date lastModifiedDate;

    public org.egov.tenant.domain.model.Tenant toDomain() {
        return org.egov.tenant.domain.model.Tenant.builder()
                .id(id)
                .code(code)
                .description(description)
                .imageId(imageId)
                .logoId(logoId)
                .domainUrl(domainUrl)
                .type(type)
                .build();
    }
}
