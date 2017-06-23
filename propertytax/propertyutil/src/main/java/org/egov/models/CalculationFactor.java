package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationFactor {

	private Long id;	
		 
	@NotNull
	@Size(min=4,max=128)
	private String	tenantId;	

	@NotNull
	@Size(min=4,max=16)
	private String	factorCode;
		 
	@NotNull
	private String	factorType;
	
	@NotNull	
	private Double	factorValue;	
	
	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private String	fromDate;	
	
	@NotNull
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private String	toDate;	
		
	private AuditDetails auditDetails;
}
