package org.egov.swm.domain.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
public class PurchaseInfo {

    @JsonProperty("purchaseDate")
    private Long purchaseDate = null;

    @JsonProperty("price")
    @Min(value = 0)
    @Max(value = 10000000)
    private Double price = null;

    @Length(min = 0, max = 256)
    @JsonProperty("sourceOfPurchase")
    private String sourceOfPurchase = null;

}
