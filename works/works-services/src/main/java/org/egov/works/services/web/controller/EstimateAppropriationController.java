package org.egov.works.services.web.controller;

import org.egov.works.services.domain.service.EstimateAppropriationService;
import org.egov.works.services.web.contract.EstimateAppropriationRequest;
import org.egov.works.services.web.contract.EstimateAppropriationResponse;
import org.egov.works.services.web.contract.EstimateAppropriationSearchContract;
import org.egov.works.services.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("estimateappropriations")
public class EstimateAppropriationController {

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.OK)
    public EstimateAppropriationResponse create(
            @RequestBody EstimateAppropriationRequest estimateAppropriationRequest) {
        return estimateAppropriationService.create(estimateAppropriationRequest);
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.OK)
    public EstimateAppropriationResponse update(
            @RequestBody EstimateAppropriationRequest estimateAppropriationRequest) {
        return estimateAppropriationService.update(estimateAppropriationRequest);
    }

    @PostMapping("/_search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> search(
            @ModelAttribute EstimateAppropriationSearchContract estimateAppropriationSearchContract,
            @RequestBody RequestInfo requestInfo, BindingResult errors,
            @RequestParam(required = true) String tenantId) {
        EstimateAppropriationResponse appropriationResponse = estimateAppropriationService
                .search(estimateAppropriationSearchContract, requestInfo);
        return new ResponseEntity<>(appropriationResponse, HttpStatus.OK);
    }

}
