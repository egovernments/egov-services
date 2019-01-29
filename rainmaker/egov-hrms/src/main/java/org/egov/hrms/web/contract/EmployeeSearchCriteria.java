package org.egov.hrms.web.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class EmployeeSearchCriteria {
	
	public List<String> codes;
	
	public List<String> departments;
	
	public List<String> designations;
	
	public List<Integer> ids;
	
	public List<String> employeestatuses;
	
	public List<String> employeetypes;
	
	public List<String> uuids;
	
	public List<Integer> positions;
	
	public String tenantId;
	
	public Integer offset;
	
	public Integer limit;

}
