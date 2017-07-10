package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.DraftCreateRequest;
import org.egov.pgrrest.read.domain.model.DraftCreateResponse;
import org.egov.pgrrest.read.domain.model.DraftSearchResponse;
import org.egov.pgrrest.read.domain.model.DraftUpdateRequest;
import org.egov.pgrrest.read.persistence.repository.DraftRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DraftService {
    private DraftRepository draftRepository;

    public DraftService(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public DraftCreateResponse save(DraftCreateRequest draftCreateRequest) {
        DraftCreateResponse draftCreateResponse = draftRepository.saveDraft(draftCreateRequest);
        return DraftCreateResponse.builder().id(draftCreateResponse.getId()).build();
    }

    public DraftSearchResponse getDrafts(Long userId, String serviceCode, String tenantId) {
        return draftRepository.getDrafts(userId, serviceCode, tenantId);
    }

    public void update(DraftUpdateRequest draftUpdateRequest) {
        draftRepository.updateDraft(draftUpdateRequest);
    }

    public void delete(List<Long> draftIdList) {
        draftRepository.deleteDraft(draftIdList);
    }
}
