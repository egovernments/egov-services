package org.egov.swm.domain.model;

import javax.validation.Valid;

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

	@Valid
	@JsonProperty("purchaseDate")
	private Long purchaseDate = null;

	@JsonProperty("price")
	private Double price = null;

	@Length(min = 0, max = 256)
	@JsonProperty("sourceOfPurchase")
	private String sourceOfPurchase = null;

}
