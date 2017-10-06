package org.egov.models;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * departmentRequest
 * 
 * @author narendra
 *
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@Valid
	private List<Department> departments;
}
