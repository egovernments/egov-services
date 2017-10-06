package org.egov.notification.model;

import java.util.List;

import org.egov.models.TaxCalculation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
/**
 * 
 * @author Pavan Kumar Kamma
 * 
 *
 */
public class TaxCalculationResponse {

	List<TaxCalculation> taxCalculationList;
}
