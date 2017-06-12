package org.egov.demand.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectedReceipts {

	@JsonProperty("receiptNumber")
	private String receiptNumber = null;

	@JsonProperty("receiptAmount")
	private Double receiptAmount = null;

	@JsonProperty("receiptDate")
	private LocalDate receiptDate = null;

	@JsonProperty("status")
	private String status = null;
}
