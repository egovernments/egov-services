package org.egov.swm.domain.model;

import javax.validation.Valid;
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
public class ProcessingSite {

    @JsonProperty("code")
    private String code;

    @JsonProperty("tenantId")
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @NotNull
    private String tenantId;

    @JsonProperty("name")
    @Length(min = 1, max = 256, message = "Value of name shall be between 1 and 256")
    @NotNull
    private String name;

    @NotNull
    @JsonProperty("siteDetails")
    private SiteDetails siteDetails;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails;

}
