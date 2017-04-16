package org.egov.tenant.domain.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Tenant {

    private Long id;
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
}
