package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitTax {
	
	@Size(min=1,max=16)
    private String floorNumber;
	
	@Size(min=1,max=8)
    private String unitNo;

    @NotNull
    private Double assessableArea;

    private Double structureFactor;

    private Double usageFactor;

    private Double subUsageFactor;

    @NotNull
    private CommonTaxDetails unitTaxes;

}
