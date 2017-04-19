package org.egov.tenant.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tenant {
    private Long id;
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private String type;
    private City city;

    public Tenant(org.egov.tenant.domain.model.Tenant domain) {
        this.id = domain.getId();
        this.code = domain.getCode();
        this.description = domain.getDescription();
        this.logoId = domain.getLogoId();
        this.imageId = domain.getImageId();
        this.domainUrl = domain.getDomainUrl();
        this.type = domain.getType().toString();
        this.city = new City(domain.getCity());
    }

    @JsonIgnore
    public org.egov.tenant.domain.model.Tenant toDomain() {
        org.egov.tenant.domain.model.City city = this.city.toDomain();

        return org.egov.tenant.domain.model.Tenant.builder()
                .code(code)
                .description(description)
                .logoId(logoId)
                .imageId(imageId)
                .domainUrl(domainUrl)
                .type(type)
                .city(city)
                .build();
    }
}
