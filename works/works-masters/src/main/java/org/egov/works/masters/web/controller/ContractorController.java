package org.egov.works.masters.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.masters.domain.service.ContractorService;
import org.egov.works.masters.domain.validator.ContractorValidator;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorRequest;
import org.egov.works.masters.web.contract.ContractorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contractors")
public class ContractorController {
    
    @Autowired
    private MasterUtils masterUtils;
    
    @Autowired
    private ContractorService contractorService;
    
    @Autowired
    private ContractorValidator contractorValidator;
    
    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody ContractorRequest contractorRequest, @RequestParam String tenantId) {
        contractorValidator.validateContractor(contractorRequest);
        final List<Contractor> contractors = contractorService.create(contractorRequest);
        final ContractorResponse response = new ContractorResponse();
        response.setContractors(contractors);
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(contractorRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@Valid @RequestBody ContractorRequest contractorRequest, @RequestParam String tenantId) {
        contractorValidator.validateContractor(contractorRequest);
        final List<Contractor> contractors = contractorService.update(contractorRequest);
        final ContractorResponse response = new ContractorResponse();
        response.setContractors(contractors);
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(contractorRequest.getRequestInfo(), true));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
