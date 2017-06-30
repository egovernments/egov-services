package org.egov.calculator.models;

import java.util.List;
import java.util.Map;

import org.egov.models.TaxPeriod;
import org.egov.models.TaxRates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaxCalculationModel {

    private List<List<Map<String, Double>>> factors;

    private List<List<Map<String, TaxPeriod>>> taxPeriods;

    private List<List<List <TaxRates>>> taxRates;

    private List<List<Double>> guidanceValues;
    
    private List<TaxResponse> taxResponses;
    
    private TaxResponse taxResponse;

   
}
