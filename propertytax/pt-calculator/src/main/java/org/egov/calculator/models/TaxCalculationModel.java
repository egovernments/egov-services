package org.egov.calculator.models;

import java.util.List;
import java.util.Map;

import org.egov.models.TaxPeriod;
import org.egov.models.TaxRates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxCalculationModel {

    private List<Map<String, Double>> factors;

    private List<Map<String, TaxPeriod>> taxPeriods;

    private List<Map<String, TaxRates>> taxRates;

    private List<Double> guidanceValues;
    
    private List<TaxResponse> taxResponses;

}
