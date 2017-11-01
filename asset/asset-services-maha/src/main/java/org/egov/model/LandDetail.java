package org.egov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandDetail {

	private String code;
	
	private Double area;
	
	private String surveyNo;
}
