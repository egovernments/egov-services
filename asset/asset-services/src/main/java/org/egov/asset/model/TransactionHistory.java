package org.egov.asset.model;

import java.math.BigDecimal;

import org.egov.asset.model.enums.TransactionType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class TransactionHistory {
	
	@JsonProperty("transactionDate")
	private Long transactionDate;
	
	@JsonProperty("transactionAmount")
	private BigDecimal transactionAmount;
	
	@JsonProperty("valueBeforeTransaction")
	private BigDecimal valueBeforeTransaction;
	
	@JsonProperty("valueAfterTransaction")
	private BigDecimal valueAfterTransaction;
	
	@JsonProperty("transactionType")
	private TransactionType transactionType;
	
}
