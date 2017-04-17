package org.egov.tenant.domain.model;


import lombok.*;

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
    private TenantType type;
    @Setter
    private City city;
}
