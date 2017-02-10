package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;

public interface WorkflowRepository {

    WorkflowResponse triggerWorkflow(WorkflowRequest workflowRequest);

}
