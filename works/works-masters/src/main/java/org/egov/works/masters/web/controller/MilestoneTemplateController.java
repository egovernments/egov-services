package org.egov.works.masters.web.controller;

import org.egov.works.masters.domain.service.MilestoneTemplateService;
import org.egov.works.masters.utils.MasterUtils;
import org.egov.works.masters.web.contract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/milestonetemplates")
public class MilestoneTemplateController {

    @Autowired
    private MilestoneTemplateService milestoneTemplateService;

    @Autowired
    private MasterUtils masterUtils;

    @PostMapping("/_create")
    public ResponseEntity<?> create(@Valid @RequestBody MilestoneTemplateRequest milestoneTemplateRequest) {
        return milestoneTemplateService.create(milestoneTemplateRequest);
    }

    @PostMapping("/_search")
    public ResponseEntity<?> search(
            @ModelAttribute @Valid MilestoneTemplateSearchCriteria milestoneTemplateSearchCriteria,
            @RequestBody RequestInfo requestInfo, BindingResult errors, @RequestParam String tenantId) {
        final List<MilestoneTemplate> milestoneTemplates = milestoneTemplateService.search(milestoneTemplateSearchCriteria);
        final MilestoneTemplateResponse response = new MilestoneTemplateResponse();
        response.setMilestoneTemplates(milestoneTemplates);
        response.setResponseInfo(masterUtils.createResponseInfoFromRequestInfo(requestInfo, true));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/_update")
    public ResponseEntity<?> update(@Valid @RequestBody MilestoneTemplateRequest milestoneTemplateRequest) {
        return milestoneTemplateService.update(milestoneTemplateRequest);
    }
}
