package org.egov.swm.domain.model;

import java.util.List;

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
    @Length(min = 1, max = 128)
    @NotNull
    private String tenantId;

    @JsonProperty("name")
    @Length(min = 1, max = 128)
    @NotNull
    private String name;

    @NotNull
    @JsonProperty("distanceFromDumpingGround")
    private Double distanceFromDumpingGround;

    @NotNull
    @JsonProperty("siteDetails")
    private SiteDetails siteDetails;

    @NotNull
    @JsonProperty("dumpingGrounds")
    private List<DumpingGround> dumpingGrounds;

    @JsonProperty("auditDetails")
    @Valid
    private AuditDetails auditDetails;

}
