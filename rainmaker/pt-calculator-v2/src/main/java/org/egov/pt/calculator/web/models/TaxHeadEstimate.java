package org.egov.pt.calculator.web.models;

import org.egov.pt.calculator.web.models.demand.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxHeadEstimate {

	private String taxHeadCode;
	
	private Double estimateAmount;
	
	private Category category;
}
