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
public class BinDetails {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("collectionPoint")
    private String collectionPoint = null;

    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @Valid
    @JsonProperty("asset")
    private Asset asset = null;
}
