package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.model.DraftCreateRequest;
import org.egov.pgrrest.read.domain.model.DraftCreateResponse;
import org.egov.pgrrest.read.domain.service.DraftService;
import org.egov.pgrrest.read.web.contract.DraftResponse;
import org.egov.pgrrest.read.web.contract.DraftSearchResponse;
import org.egov.pgrrest.read.web.contract.DraftUpdateRequest;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/draft")
public class DraftController {

    private DraftService draftService;

    public DraftController(DraftService draftService) {
        this.draftService = draftService;
    }

    @PostMapping("/v1/_create")
    @ResponseStatus(HttpStatus.OK)
    public org.egov.pgrrest.read.web.contract.DraftCreateResponse saveDraft(@RequestBody org.egov.pgrrest.read.web.contract.DraftCreateRequest draftCreateRequest) {
        DraftCreateRequest draftCreateRequestModel = draftCreateRequest.toDomain();
        DraftCreateResponse draftCreateResponse = draftService.save(draftCreateRequestModel);
        return new org.egov.pgrrest.read.web.contract.DraftCreateResponse(draftCreateResponse);
    }

    @PostMapping("/v1/_search")
    @ResponseStatus(HttpStatus.OK)
    public DraftSearchResponse searchDraft(@RequestBody RequestInfoBody requestInfoBody,
                                           @RequestParam(value = "serviceCode", required = false) String serviceCode,
                                           @RequestParam String tenantId) {
        org.egov.pgrrest.read.domain.model.DraftSearchResponse draftSearchResponse = draftService.getDrafts(requestInfoBody.getRequestInfo().getUserInfo().getId(), serviceCode, tenantId);
        return DraftSearchResponse.builder().responseInfo(null).drafts(draftSearchResponse.getDraftResponses().stream().map(org.egov.pgrrest.read.web.contract.Draft::new).collect(Collectors.toList())).build();
    }

    @PostMapping("/v1/_update")
    @ResponseStatus(HttpStatus.OK)
    public DraftResponse updateDraft(@RequestBody DraftUpdateRequest draftUpdateRequest) {
        org.egov.pgrrest.read.domain.model.DraftUpdateRequest draftUpdateRequestModel = draftUpdateRequest.toDomain();
        draftService.update(draftUpdateRequestModel);
        return DraftResponse.builder().responseInfo(null).successful(true).build();
    }

    @PostMapping("/v1/_delete")
    @ResponseStatus(HttpStatus.OK)
    public DraftResponse deleteDraft(@RequestParam List<Long> draftIdList) {
        draftService.delete(draftIdList);
        return DraftResponse.builder().responseInfo(null).successful(true).build();
    }
}
