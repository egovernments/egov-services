package org.egov.lams.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DemandDetails {
	private BigDecimal taxAmount;
	private BigDecimal collectionAmount;
	private BigDecimal rebateAmount;
	private String taxReason;
	private String taxReasonCode;
	private String taxPeriod;
	private String glCode;
	private Integer isActualDemand;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "IST")
	private Date periodStartDate;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "IST")
	private Date periodEndDate;
}
