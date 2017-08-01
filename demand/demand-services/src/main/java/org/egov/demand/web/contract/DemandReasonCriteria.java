package org.egov.demand.web.contract;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
public class DemandReasonCriteria {
	private Long id;
	private String moduleName;
	private String taxCategory;
	private String taxReason;
	private String taxPeriod;
	 @DateTimeFormat(pattern="dd/MM/yyyy")
	private Date fromDate;
	 @DateTimeFormat(pattern="dd/MM/yyyy")
	private Date toDate;
	private String tenantId;
	private String installmentType;
}
