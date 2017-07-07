package org.egov.lams.contract;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstallmentSearchCriteria {
	private Date fromDate = null;
	private Date toDate = null;
	private Date currentDate = null;
	private Date installmentYear = null;
	private String module = null;
	private String installmentNumber = null;
	private String description = null;
	private String installmentType = null;
	private String financialYear = null;
	private String tenantId;

}
