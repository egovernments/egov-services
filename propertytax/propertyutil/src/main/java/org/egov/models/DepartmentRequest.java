package org.egov.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;
	
	private List<Department> departments;
}
