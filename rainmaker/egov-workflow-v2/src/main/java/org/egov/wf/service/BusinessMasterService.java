package org.egov.wf.service;

import org.egov.wf.config.WorkflowConfig;
import org.egov.wf.producer.Producer;
import org.egov.wf.web.models.BusinessService;
import org.egov.wf.web.models.BusinessServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessMasterService {

    private Producer producer;

    private WorkflowConfig config;

    private EnrichmentService enrichmentService;


    @Autowired
    public BusinessMasterService(Producer producer, WorkflowConfig config, EnrichmentService enrichmentService) {
        this.producer = producer;
        this.config = config;
        this.enrichmentService = enrichmentService;
    }





    public List<BusinessService> create(BusinessServiceRequest request){
       enrichmentService.enrichBusinessService(request);
       producer.push(config.getSaveBusinessServiceTopic(),request);
        System.out.println(request.toString());
       return request.getBusinessServices();
    }



}
