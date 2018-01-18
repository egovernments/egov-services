package org.egov.pa.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class KPIGetRequest {
	
	private String kpiName; 
	private String kpiCode;
	private String tenantId;
	private Long departmentId;
	private String categoryId;
	private String pageSize;
	private String pageNumber;
	private String sort;
	private String finYear;
	
	
	
	
	

}
