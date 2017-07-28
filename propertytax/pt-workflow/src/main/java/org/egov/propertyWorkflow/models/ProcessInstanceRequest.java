package org.egov.propertyWorkflow.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ProcessInstanceRequest class
 * 
 * @author Yosadhara
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessInstanceRequest {

	private RequestInfo requestInfo;

	private ProcessInstance processInstance;

	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
}
