package org.egov.tenant.web.contract;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

@Data
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
    private String twitterUrl;
    private String facebookUrl;
    private String emailId;
    private City city;
    private String address;
    private String contactNumber;
    private String helpLineNumber;

}