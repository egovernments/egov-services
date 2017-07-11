package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.read.domain.model.*;
import org.egov.pgrrest.read.persistence.repository.DraftRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DraftService {
    private DraftRepository draftRepository;

    public DraftService(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public long save(NewDraft newDraft) {
        return draftRepository.saveDraft(newDraft);
    }

    public DraftResult getDrafts(DraftSearchCriteria searchCriteria) {
        return draftRepository.getDrafts(searchCriteria);
    }

    public void update(UpdateDraft updateDraft) {
        draftRepository.updateDraft(updateDraft);
    }

    public void delete(List<Long> draftIdList) {
        draftRepository.delete(draftIdList);
    }

    public void delete(long draftId) {
        draftRepository.delete(draftId);
    }
}
