package org.egov.tradelicense.web.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tenant {
    private String code;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private String type;
    private City city;
}