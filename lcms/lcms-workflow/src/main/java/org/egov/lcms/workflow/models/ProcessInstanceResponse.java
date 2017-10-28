package org.egov.lcms.workflow.models;

import java.util.ArrayList;
import java.util.List;

import org.egov.models.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ProcessInstanceResponse class
 * 
 * @author Yosadhara
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessInstanceResponse {

	private ResponseInfo responseInfo;

	private ProcessInstance processInstance;

	private List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

	private Page page;
}