package org.egov.models;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitTax {

    private String floorNumber;

    private Integer unitNo;

    @NotNull
    private Double assessableArea;

    private Double structureFactor;

    private Double usageFactor;

    private Double subUsageFactor;

    @NotNull
    private CommonTaxDetails unitTaxes;

}
