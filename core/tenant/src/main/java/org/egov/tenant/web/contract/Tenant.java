package org.egov.tenant.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.util.ObjectUtils.isEmpty;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tenant {
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private String type;
    private City city;

    public Tenant(org.egov.tenant.domain.model.Tenant tenant, City city) {
        this.code = tenant.getCode();
        this.description = tenant.getDescription();
        this.logoId = tenant.getLogoId();
        this.imageId = tenant.getImageId();
        this.domainUrl = tenant.getDomainUrl();
        this.type = tenant.getType().toString();
        this.city = city;
    }

    @JsonIgnore
    public org.egov.tenant.domain.model.Tenant toDomain() {
        org.egov.tenant.domain.model.City city;

        if (!isEmpty(this.city)) {
            city = this.city.toDomain();
        } else {
            city = null;
        }

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
