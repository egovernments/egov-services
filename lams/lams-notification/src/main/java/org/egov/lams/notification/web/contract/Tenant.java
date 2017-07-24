package org.egov.lams.notification.web.contract;


import org.egov.lams.notification.model.City;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tenant {
    private String code;
    private String name;
    private String description;
    private String logoId;
    private String imageId;
    private String domainUrl;
    private String type;
    private City city;
}