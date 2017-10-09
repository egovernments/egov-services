package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxPeriod {
	private Long id;

	@Size(min = 4, max = 128)
	@NotNull
	private String tenantId;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private String fromDate;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private String toDate;

	@Size(min = 4, max = 64)
	@NotNull
	private String code;

	@NotNull
	private String periodType;

	@Size(min = 4, max = 64)
	private String financialYear;

	private AuditDetails auditDetails;

}
