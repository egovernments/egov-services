package org.egov.swm.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DumpingGround {

    @JsonProperty("code")
    private String code;

    @JsonProperty("tenantId")
    @Length(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @NotNull
    private String tenantId;

    @JsonProperty("name")
    @Length(min = 1, max = 128, message = "Value of name shall be between 1 and 128")
    @NotNull
    private String name;

    @JsonProperty("ulbs")
    @Length(min = 1)
    @NotNull
    private List<Tenant> ulbs = new ArrayList<>();

    @NotNull
    @JsonProperty("siteDetails")
    private SiteDetails siteDetails;

    @JsonProperty("isProcessingSite")
    private Boolean isProcessingSite;

    @JsonProperty("distanceFromProcessingSite")
    private Double distanceFromProcessingSite;

    @JsonProperty("processingSite")
    private ProcessingSite processingSite;

}
