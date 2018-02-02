package org.egov.swm.domain.model;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class CollectionPoint {

    @JsonProperty("code")
    private String code = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Length(min = 1, max = 256, message = "Value of name shall be between 1 and 256")
    @JsonProperty("name")
    private String name = null;

    @NotNull
    @JsonProperty("location")
    private Boundary location = null;

    @Valid
    @Size(min = 1)
    @NotNull
    @JsonProperty("binDetails")
    private List<BinDetails> binDetails = null;

    @Valid
    @Size(min = 1)
    @NotNull
    @JsonProperty("collectionPointDetails")
    private List<CollectionPointDetails> collectionPointDetails = null;

    private Boolean isSelected;

    @Valid
    @JsonProperty("auditDetails")
    private AuditDetails auditDetails = null;

}
