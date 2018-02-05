package org.egov.swm.domain.model;

import java.util.ArrayList;
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
public class SourceSegregation {

    @Length(min = 1, max = 256, message = "Value of code shall be between 1 and 256")
    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("dumpingGround")
    private DumpingGround dumpingGround = null;

    @NotNull
    @JsonProperty("ulb")
    private Tenant ulb = null;

    @NotNull
    @JsonProperty("sourceSegregationDate")
    private Long sourceSegregationDate = null;

    @NotNull
    @Valid
    @JsonProperty("collectionDetails")
    private List<CollectionDetails> collectionDetails = new ArrayList<>();

    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
