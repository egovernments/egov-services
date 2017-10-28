package org.egov.lcms.workflow.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ProcessInstanceRequest class
 * 
 * @author Yosadhara
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessInstanceRequest {

	private RequestInfo requestInfo;

	private ProcessInstance processInstance;

	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
}
