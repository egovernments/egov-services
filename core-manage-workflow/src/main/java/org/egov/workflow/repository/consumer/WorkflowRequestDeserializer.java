package org.egov.workflow.repository.consumer;

import org.egov.workflow.repository.consumer.contract.WorkFlowRequest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class WorkflowRequestDeserializer extends JsonDeserializer<WorkFlowRequest> {

    public WorkflowRequestDeserializer() {
        super(WorkFlowRequest.class);
    }
}
