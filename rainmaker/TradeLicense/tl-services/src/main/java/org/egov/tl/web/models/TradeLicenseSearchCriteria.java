package org.egov.tl.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeLicenseSearchCriteria {


    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("pageNumber")
    private int pageNumber;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ids")
    private List<String> ids;

    @JsonProperty("ownerids")
    private List<String> ownerids;

    @JsonProperty("applicationNumber")
    private String applicationNumber;

    @JsonProperty("licenseNumber")
    private String licenseNumber;

    @JsonProperty("oldLicenseNumber")
    private String oldLicenseNumber;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("offset")
    private String offset;



}
