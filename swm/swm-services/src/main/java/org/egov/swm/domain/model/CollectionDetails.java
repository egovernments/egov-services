package org.egov.swm.domain.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
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
    @Digits(fraction = 2, integer = 4, message = "wetWasteCollected shall be with 2 decimal points")
    @DecimalMin(value = "1", message = "wetWasteCollected shall be between 1 and 1000 Tons")
    @DecimalMax(value = "1000", message = "wetWasteCollected shall be between 1 and 1000 Tons")
    @JsonProperty("wetWasteCollected")
    private Double wetWasteCollected = null;

    @NotNull
    @Digits(fraction = 2, integer = 4, message = "dryWasteCollected shall be with 2 decimal points")
    @DecimalMin(value = "1", message = "dryWasteCollected shall be between 1 and 1000 Tons")
    @DecimalMax(value = "1000", message = "dryWasteCollected shall be between 1 and 1000 Tons")
    @JsonProperty("dryWasteCollected")
    private Double dryWasteCollected = null;

}
