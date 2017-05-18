package org.egov.lams.web.contract;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class DemandReasonCriteria {
	private Long id;
	private String moduleName;
	private String taxCategory;
	private String taxReason;
	private String taxPeriod;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date fromDate;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date toDate;
	private String tenantId;
	private String installmentType;
}
