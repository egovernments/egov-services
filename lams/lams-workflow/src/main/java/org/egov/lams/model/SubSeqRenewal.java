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
public class SubSeqRenewal {

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("fromDate")
	private Date historyFromDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("toDate")
	private Date historyToDate;

	@JsonProperty("years")
	private Double years;

	@JsonProperty("rent")
	private Double historyRent;
	
	@JsonProperty("resolutionNumber")
	private String resolutionNumber;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("resolutionDate")
	private Date resolutionDate;

	private Long agreementId;
	
	private String tenantId;

}
