package org.egov.web.contract;

import lombok.*;

@Getter
@Setter
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
}