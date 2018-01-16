package org.egov.inv.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FifoResponse {
	
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;
	
	private  BigDecimal unitRate;
	
	private BigDecimal stock;
	
	private BigDecimal value;

}
