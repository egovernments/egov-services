package org.egov.works.masters.domain.service;

import java.util.List;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.works.commons.utils.CommonUtils;
import org.egov.works.masters.config.PropertiesManager;
import org.egov.works.masters.domain.repository.ContractorRepository;
import org.egov.works.masters.domain.validator.ContractorValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorRequest;
import org.egov.works.masters.web.contract.ContractorResponse;
import org.egov.works.masters.web.contract.ContractorSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @Autowired
    private ContractorRepository contractorRepository;
    
    @Transactional
    public ResponseEntity<?> create(ContractorRequest contractorRequest, String tenantId) {
        ContractorResponse response = new ContractorResponse();
        contractorValidator.validateContractor(contractorRequest,tenantId, true);
        CommonUtils commonUtils = new CommonUtils();
        for(final Contractor contractor : contractorRequest.getContractors()){
            contractor.setId(commonUtils.getUUID());
            contractor.setAuditDetails(masterUtils.getAuditDetails(contractorRequest.getRequestInfo(), false)); 
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterContractorCreateValidatedTopic(), contractorRequest);
        // TODO: To be enabled when account detail consumer is available
        // kafkaTemplate.send(propertiesManager.getCreateAccountDetailKeyTopic(),
        // contractorRequest);
        response.setContractors(contractorRequest.getContractors());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(contractorRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
        
    }
    
    @Transactional
    public ResponseEntity<?> update(ContractorRequest contractorRequest, String tenantId) {
        ContractorResponse response = new ContractorResponse();
        contractorValidator.validateContractor(contractorRequest,tenantId, false);
        for(final Contractor contractor : contractorRequest.getContractors()){
            contractor.setAuditDetails(masterUtils.getAuditDetails(contractorRequest.getRequestInfo(), true));
        }
        kafkaTemplate.send(propertiesManager.getWorksMasterContractorUpdateValidatedTopic(), contractorRequest);
        response.setContractors(contractorRequest.getContractors());
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(contractorRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    public List<Contractor> search(ContractorSearchCriteria contractorSearchCriteria) {
        return contractorRepository.getContractorByCriteria(contractorSearchCriteria);
    }
}
