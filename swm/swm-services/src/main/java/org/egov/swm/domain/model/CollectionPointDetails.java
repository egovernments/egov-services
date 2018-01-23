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
public class CollectionPointDetails {

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("collectionPoint")
    private String collectionPoint = null;

    @Length(min = 1, max = 128, message = "Value of tenantId shall be between 1 and 128")
    @JsonProperty("tenantId")
    private String tenantId = null;

    @NotNull
    @JsonProperty("collectionType")
    private CollectionType collectionType = null;

    @NotNull
    @JsonProperty("garbageEstimate")
    @Digits(fraction = 2, integer = 3, message = "garbageEstimate shall be with 2 decimal points")
    @DecimalMin(value = "0", message = "Garbage estimate shall be greater then 0 and greater then or equel to 100 Tons")
    @DecimalMax(value = "100", message = "Garbage estimate shall be greater then 0 and greater then or equel to 100 Tons")
    private Double garbageEstimate = null;

    @Length(min = 0, max = 300, message = "Value of description shall be greater then 0 and greater then or equel to 300 ")
    @JsonProperty("description")
    private String description = null;

}
