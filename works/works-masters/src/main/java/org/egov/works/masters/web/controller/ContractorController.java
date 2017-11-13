package org.egov.works.masters.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.masters.domain.service.ContractorService;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorRequest;
import org.egov.works.masters.web.contract.ContractorResponse;
import org.egov.works.masters.web.contract.ContractorSearchCriteria;
import org.egov.works.masters.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contractors")
public class ContractorController {
    
    @Autowired
    private MasterUtils masterUtils;
    
    @Autowired
    private ContractorService contractorService;
    
    @PostMapping("/_create")
    public ResponseEntity<?> create(@Valid @RequestBody ContractorRequest contractorRequest, @RequestParam String tenantId) {
        return contractorService.create(contractorRequest, tenantId);
    }
    
    @PostMapping("/_update")
    public ResponseEntity<?> update(@Valid @RequestBody ContractorRequest contractorRequest, @RequestParam String tenantId) {
        return contractorService.update(contractorRequest, tenantId);
    }
    
    @PostMapping("/_search")
    public ResponseEntity<?> search(@ModelAttribute @Valid ContractorSearchCriteria contractorSearchCriteria,
            @RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam String tenantId) {
        final List<Contractor> contractors = contractorService.search(contractorSearchCriteria);
        final ContractorResponse response = new ContractorResponse();
        response.setContractors(contractors);
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(requestInfo, true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
