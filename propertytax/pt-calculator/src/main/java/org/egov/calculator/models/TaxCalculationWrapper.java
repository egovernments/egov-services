package org.egov.calculator.models;

import java.util.List;

import org.egov.models.Property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxCalculationWrapper {

    private Property property;

    private TaxCalculationModel taxCalculationModel;

    private TaxResponse taxResponse;

    private List<TaxResponse> taxResponses;

    private String text;
}
