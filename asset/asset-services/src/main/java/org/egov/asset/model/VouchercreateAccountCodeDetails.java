package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VouchercreateAccountCodeDetails {

	@JsonProperty("glcode")
	private String glcode;

	@JsonProperty("debitAmount")
	private Double debitAmount = 0.0;

	@JsonProperty("creditAmount")
	private Double creditAmount = 0.0;

	@JsonProperty("function")
	private Function function;

	@JsonProperty("subledgerDetails")
	private List<LedgerDetail> subledgerDetails = new ArrayList<>();

}
