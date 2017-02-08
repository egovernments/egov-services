package org.egov.workflow.repository.producer;

import org.egov.workflow.repository.producer.contract.WorkflowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
public class WorkflowProducer {

    private MessageChannel output;

    @Autowired
    WorkflowProducer(MessageChannel output) {
        this.output = output;
    }

    public void handle(WorkflowRequest smsRequest) {
        output.send(MessageBuilder.withPayload(smsRequest).build());
    }
}
