package org.egov.lcms.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LegalCaseSearchResponse {

	private ResponseInfo responseInfo;
	
	private List<LegalCase> legalCases;
	
}
