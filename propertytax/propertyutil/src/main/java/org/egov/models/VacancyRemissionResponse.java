package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyRemissionResponse {
	
	private ResponseInfo responseInfo;
	
	private VacancyRemission vacancyRemission;
}
