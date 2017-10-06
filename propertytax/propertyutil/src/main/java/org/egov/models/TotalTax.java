package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TotalTax {

	@JsonProperty("totalTax")
	private Double totalTax = 0.0d;

	@JsonProperty("headWiseTaxes")
	private List<HeadWiseTax> headWiseTaxes;

}
