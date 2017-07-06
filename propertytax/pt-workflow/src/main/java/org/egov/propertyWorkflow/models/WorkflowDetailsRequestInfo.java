package org.egov.propertyWorkflow.models;

import org.egov.models.WorkFlowDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * WorkflowDetailsRequestInfo class
 * 
 * @author Yosadhara
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkflowDetailsRequestInfo {

	private String tenantId;

	private RequestInfo requestInfo;

	private WorkFlowDetails workflowDetails;
}