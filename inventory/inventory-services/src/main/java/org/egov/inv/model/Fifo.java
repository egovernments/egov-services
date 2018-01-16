package org.egov.inv.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fifo {
	
	@NotNull
	private Store store;
 
	@NotNull
	private Material material;

	@NotNull
	private String tenantId;
    
	@NotNull
	private Uom uom;

	private BigDecimal userQuantityIssued;

	@NotNull
	private Long issueDate;
	
	private String mrnNumber;
	
	private String purpose;
	
	private String issueDetailId;
	
}
