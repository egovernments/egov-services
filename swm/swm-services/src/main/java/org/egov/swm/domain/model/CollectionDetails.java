package org.egov.swm.domain.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
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
public class CollectionDetails {

    @Length(min = 1, max = 256, message = "Value of id shall be between 1 and 256")
    @JsonProperty("id")
    private String id = null;

    @Length(min = 1, max = 256, message = "Value of tenantId shall be between 1 and 256")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("collectionType")
    private CollectionType collectionType = null;

    private String sourceSegregation;

    @NotNull
    @Digits(fraction = 2, integer = 4, message = "EnteredWet Waste Collected shall be with 2 decimal points")
    @DecimalMin(value = "0", message = "Entered Wet Waste quantity shall be between 0 and 20000.")
    @DecimalMax(value = "20000", message = "Entered Wet Waste quantity shall be between 0 and 20000.")
    @JsonProperty("wetWasteCollected")
    private Double wetWasteCollected = null;

    @NotNull
    @Digits(fraction = 2, integer = 4, message = "Entered Dry Waste Collected shall be with 2 decimal points")
    @DecimalMin(value = "0", message = "Entered Dry Waste quantity shall be between 0 and 20000.")
    @DecimalMax(value = "20000", message = "Entered Dry Waste quantity shall be between 0 and 20000.")
    @JsonProperty("dryWasteCollected")
    private Double dryWasteCollected = null;

}
