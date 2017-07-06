package org.egov.propertyWorkflow.models;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.Page;
import org.egov.models.ResponseInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ProcessInstanceResponse class
 * 
 * @author Yosadhara
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessInstanceResponse {

	private ResponseInfo responseInfo;

	private ProcessInstance processInstance;

	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

	private Page page;
}