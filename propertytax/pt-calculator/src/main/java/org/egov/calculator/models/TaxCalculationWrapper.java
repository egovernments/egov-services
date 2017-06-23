package org.egov.calculator.models;

import org.egov.models.Property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxCalculationWrapper {

    private Property property;
    
    private  TaxCalculationModel taxCalculationModel;
}
