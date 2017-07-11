package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.model.DraftResult;
import org.egov.pgrrest.read.domain.model.DraftSearchCriteria;
import org.egov.pgrrest.read.domain.model.NewDraft;
import org.egov.pgrrest.read.domain.model.UpdateDraft;
import org.egov.pgrrest.read.domain.service.DraftService;
import org.egov.pgrrest.read.web.contract.*;
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
    public DraftCreateResponse saveDraft(@RequestBody DraftCreateRequest draftCreateRequest) {
        NewDraft newDraftModel = draftCreateRequest.toDomain();
        long draftId = draftService.save(newDraftModel);
        return new DraftCreateResponse(draftId);
    }

    @PostMapping("/v1/_search")
    public DraftSearchResponse searchDraft(@RequestBody RequestInfoBody requestInfoBody,
                                           @RequestParam(value = "serviceCode", required = false) String serviceCode,
                                           @RequestParam String tenantId) {
        final DraftSearchCriteria searchCriteria = DraftSearchCriteria.builder()
            .userId(requestInfoBody.getUserId())
            .serviceCode(serviceCode)
            .tenantId(tenantId)
            .build();
        DraftResult draftSearchResponse =
            draftService.getDrafts(searchCriteria);
        final List<Draft> contractDrafts = convertToContract(draftSearchResponse);
        return DraftSearchResponse.builder()
            .responseInfo(null)
            .drafts(contractDrafts)
            .build();
    }

    private List<Draft> convertToContract(DraftResult draftSearchResponse) {
        return draftSearchResponse.getDrafts().stream()
            .map(Draft::new)
            .collect(Collectors.toList());
    }

    @PostMapping("/v1/_update")
    public DraftResponse updateDraft(@RequestBody DraftUpdateRequest draftUpdateRequest) {
        UpdateDraft draftUpdateRequestModel = draftUpdateRequest.toDomain();
        draftService.update(draftUpdateRequestModel);
        return DraftResponse.builder().responseInfo(null).successful(true).build();
    }

    @PostMapping("/v1/_delete")
    public DraftResponse deleteDraft(@RequestBody DraftDeleteRequest draftDeleteRequest) {
        List<Long> draftIdList = draftDeleteRequest.getIds();
        draftService.delete(draftIdList);
        return DraftResponse.builder().responseInfo(null).successful(true).build();
    }
}
