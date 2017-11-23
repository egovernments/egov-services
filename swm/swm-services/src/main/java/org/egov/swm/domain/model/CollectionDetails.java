package org.egov.swm.domain.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class CollectionDetails {

    @Size(min = 1, max = 256)
    @JsonProperty("id")
    private String id = null;

    @Size(min = 1, max = 256)
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("collectionType")
    private CollectionType collectionType = null;

    private String sourceSegregation;
    
    @NotNull
    @DecimalMin("0")
    @JsonProperty("wetWasteCollected")
    private Double wetWasteCollected = null;

    @NotNull
    @DecimalMin("0")
    @JsonProperty("dryWasteCollected")
    private Double dryWasteCollected = null;

}
