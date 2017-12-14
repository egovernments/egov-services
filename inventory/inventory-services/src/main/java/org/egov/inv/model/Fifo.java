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

	@NotNull
	private BigDecimal quantityIssued;

	private Long issueDate;
}
