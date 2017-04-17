package org.egov.tenant.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tenant {
    private Long id;
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private String type;

    public Tenant(org.egov.tenant.domain.model.Tenant domain) {
        this.id = domain.getId();
        this.code = domain.getCode();
        this.description = domain.getDescription();
        this.logoId = domain.getLogoId();
        this.imageId = domain.getImageId();
        this.domainUrl = domain.getDomainUrl();
        this.type = domain.getType().toString();
    }

    public org.egov.tenant.domain.model.Tenant toDomain() {
        return null;
    }
}
