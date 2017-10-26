package org.egov.lams.common.web.contract;

import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EstateSearchCriteria {

	@NotNull
	private String tenantId;
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String estateRegisterId;
	
	private String registerName;
	
	private String subRegisterName;
	
	private String propertyType;
	
	private String surveyNo;
	
	private String gattNo;
	
	private Long location;
	
	private Long regionalOffice;
	
	private Set<String> sort;
	
}
