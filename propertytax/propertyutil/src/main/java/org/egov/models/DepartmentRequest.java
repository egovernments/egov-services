package org.egov.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * departmentRequest
 * @author narendra
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {
	
	private RequestInfo requestInfo;
	
	private List<Department> departments;
}
