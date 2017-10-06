package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.CalculationFactorTypeEnum;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculationFactor {

	private Long id;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	@NotNull
	@Size(min = 4, max = 64)
	private String factorCode;

	@NotNull
	private CalculationFactorTypeEnum factorType;

	@NotNull
	private Double factorValue;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private String fromDate;

	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private String toDate;

	private AuditDetails auditDetails;
}
