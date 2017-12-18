package org.egov.lams.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eviction {

	@JsonProperty("evictionProceedingNumber")
	private String evictionProceedingNumber;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("evictionProceedingDate")
	private Date evictionProceedingDate;

	@JsonProperty("reasonForEviction")
	private String reasonForEviction;
	
	@JsonProperty("courtReferenceNumber")
	private String courtReferenceNumber;
}
