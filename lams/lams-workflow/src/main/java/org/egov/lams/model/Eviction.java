package org.egov.lams.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eviction {

	@JsonProperty("renewalOrderNo")
	private String evictionProceedingNumber;

	@JsonProperty("renewalOrderDate")
	private Date evictionProceedingDate;

	@JsonProperty("reasonForRenewal")
	private String reasonForEviction;
	
	@JsonProperty("courtReferenceNumber")
	private String courtReferenceNumber;
}
