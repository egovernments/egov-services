package org.egov.collection.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.collection.web.contract.ProcessInstanceRequest;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CommonWorkFlowRepository {

    private RestTemplate restTemplate;

    private String startWorkFlowUrl;

    //private String updateWorkFlowUrl;

    //private String endWorkFlowUrl;

    public CommonWorkFlowRepository(final RestTemplate restTemplate,@Value("${egov.services.workflow_service.hostname}")final String commonWorkFlowServiceHost,
                @Value("${egov.services.workflow_service.startpath}") final String startWorkFlowUrl) {
        this.restTemplate = restTemplate;
        this.startWorkFlowUrl = commonWorkFlowServiceHost + startWorkFlowUrl;
       // this.updateWorkFlowUrl = commonWorkFlowServiceHost + updateWorkFlowUrl;
       // this.endWorkFlowUrl = commonWorkFlowServiceHost + endWorkFlowUrl;
    }

    public ProcessInstanceResponse startWorkFlow(final ProcessInstanceRequest processInstanceRequest) {
        ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
        final HttpEntity<ProcessInstanceRequest> request = new HttpEntity<>(processInstanceRequest);
        log.info("ProcessInstanceRequest: " + request.toString());
        log.info("Start workflow URI: "+startWorkFlowUrl.toString());
        try{
            processInstanceResponse = restTemplate.postForObject(startWorkFlowUrl, request,
                    ProcessInstanceResponse.class);
        }catch(Exception e){
            log.error("Exception caused while hitting the workflow service: ", e.getCause());
            processInstanceResponse = null;
            return processInstanceResponse;
        }

        log.info("ProcessInstanceResponse: "+processInstanceResponse);
        return processInstanceResponse;
    }
}
