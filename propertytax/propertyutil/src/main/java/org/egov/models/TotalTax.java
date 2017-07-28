package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TotalTax {
	
	@JsonProperty("totalTax")
	private Double totalTax=0.0d;
	
	@JsonProperty("headWiseTaxes")
	private List<HeadWiseTax> headWiseTaxes;
	

}
