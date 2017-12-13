package org.egov.works.services.web.controller;

import javax.validation.Valid;

import org.egov.works.services.domain.service.OfflineStatusService;
import org.egov.works.services.web.contract.OfflineStatusRequest;
import org.egov.works.services.web.contract.OfflineStatusResponse;
import org.egov.works.services.web.contract.OfflineStatusSearchContract;
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
@RequestMapping("/offlinestatuses")
public class OfflineStatusController {

    @Autowired
    private OfflineStatusService offlineStatusService;

    @PostMapping("/_create")
    @ResponseStatus(HttpStatus.OK)
    public OfflineStatusResponse create(@Valid @RequestBody OfflineStatusRequest offlineStatusRequest) {
        return offlineStatusService.create(offlineStatusRequest);
    }

    @PostMapping("/_update")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@Valid @RequestBody OfflineStatusRequest offlineStatusRequest, BindingResult errors) {
        return offlineStatusService.update(offlineStatusRequest);
    }

    @PostMapping("/_search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> search(@ModelAttribute @Valid OfflineStatusSearchContract offlineStatusSearchContract,
            @RequestBody RequestInfo requestInfo, @RequestParam String tenantId) {
        OfflineStatusResponse response = offlineStatusService.search(offlineStatusSearchContract, requestInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
