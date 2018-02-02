package org.egov.swm.domain.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
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
public class PurchaseInfo {

    @NotNull
    @JsonProperty("purchaseDate")
    private Long purchaseDate = null;

    @NotNull
    @JsonProperty("price")
    @DecimalMin(value = "1", message = "price shall be between 1 and 10000000 Rs")
    @DecimalMax(value = "10000000", message = "price shall be between 1 and 10000000 Rs")
    private Double price = null;

    @Length(min = 0, max = 256, message = "Value of sourceOfPurchase shall be between 0 and 256")
    @JsonProperty("sourceOfPurchase")
    private String sourceOfPurchase = null;

}
