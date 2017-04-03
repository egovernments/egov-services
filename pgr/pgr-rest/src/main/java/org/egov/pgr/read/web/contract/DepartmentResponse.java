package org.egov.pgr.read.web.contract;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentResponse {
	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("Department")
	private List<Department> department;

}
