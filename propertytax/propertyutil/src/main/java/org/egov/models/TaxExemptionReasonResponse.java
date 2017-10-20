package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Yosadhara
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaxExemptionReasonResponse {
	
	private ResponseInfo ResponseInfo;

	private List<TaxExemptionReason> taxExemptionReasons;
}
