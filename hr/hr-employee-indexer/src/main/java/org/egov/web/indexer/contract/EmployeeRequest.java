package org.egov.web.indexer.contract;

import org.egov.web.indexer.model.Employee;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Service
public class EmployeeRequest {

	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo;

	@JsonProperty("Employee")
	private Employee employee;

}


