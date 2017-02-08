package org.egov.workflow.transform;

import org.egov.workflow.contract.WorkFlowRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class WorkflowRequestDeserializer extends JsonDeserializer<WorkFlowRequest> {

    public WorkflowRequestDeserializer() {
        super(WorkFlowRequest.class);
    }
}
