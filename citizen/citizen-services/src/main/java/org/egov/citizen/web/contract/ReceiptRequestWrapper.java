package org.egov.citizen.web.contract;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReceiptRequestWrapper {

	@JsonProperty("PGRequest")
	@NotNull
	private ReceiptRequest receiptRequest;
}
