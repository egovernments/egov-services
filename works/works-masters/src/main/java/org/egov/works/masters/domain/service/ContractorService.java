package org.egov.works.masters.domain.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.validator.ContractorValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractorService {
    
    @Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private MasterUtils masterUtils;
    
    @Autowired
    private ContractorValidator contractorValidator;
    
    @Transactional
    public List<Contractor> create(ContractorRequest contractorRequest, String tenantId) {
        contractorValidator.validateContractor(contractorRequest,tenantId);
        CommonUtils commonUtils = new CommonUtils();
        for(final Contractor contractor : contractorRequest.getContractors()){
            contractor.setId(commonUtils.getUUID());
            contractor.setAuditDetails(masterUtils.getAuditDetails(contractorRequest.getRequestInfo(), false));
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterContractorCreateValidatedTopic(), contractorRequest);
        // TODO: To be enabled when account detail consumer is available
        // kafkaTemplate.send(propertiesManager.getCreateAccountDetailKeyTopic(),
        // contractorRequest);
        return contractorRequest.getContractors();
        
    }
    
    @Transactional
    public List<Contractor> update(ContractorRequest contractorRequest, String tenantId) {
        contractorValidator.validateContractor(contractorRequest,tenantId);
        for(final Contractor contractor : contractorRequest.getContractors()){
            contractor.setAuditDetails(masterUtils.getAuditDetails(contractorRequest.getRequestInfo(), true));
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterContractorUpdateValidatedTopic(), contractorRequest);
        return contractorRequest.getContractors();
    }
}
