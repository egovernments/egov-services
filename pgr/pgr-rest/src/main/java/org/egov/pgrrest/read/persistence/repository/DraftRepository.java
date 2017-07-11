package org.egov.pgrrest.read.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.pgrrest.read.domain.exception.DraftReadException;
import org.egov.pgrrest.read.domain.exception.MalformedDraftException;
import org.egov.pgrrest.read.domain.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DraftRepository {
    private DraftJpaRepository draftJpaRepository;

    private ObjectMapper objectMapper;

    public DraftRepository(DraftJpaRepository draftJpaRepository, ObjectMapper objectMapper) {
        this.draftJpaRepository = draftJpaRepository;
        this.objectMapper = objectMapper;
    }

    public long saveDraft(NewDraft newDraft) {
        final HashMap<String, Object> draft = newDraft.getDraft();
        String createDraft = convertToString(draft);
        org.egov.pgrrest.read.persistence.entity.Draft draftEntity = org.egov.pgrrest.read.persistence.entity.Draft
            .builder()
            .serviceCode(newDraft.getServiceCode())
            .tenantId(newDraft.getTenantId())
            .userId(newDraft.getUserId())
            .draft(createDraft)
            .build();
        draftJpaRepository.save(draftEntity);
        return draftEntity.getId();
    }

    public DraftResult getDrafts(DraftSearchCriteria searchCriteria) {
        List<org.egov.pgrrest.read.persistence.entity.Draft> entityDrafts = fetchDrafts(searchCriteria);
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<HashMap<String, Object>>() {
        };

        List<Draft> drafts = entityDrafts.stream().map(draft -> {
            try {
                HashMap<String, Object> readValue = objectMapper.readValue(draft.getDraft(), typeReference);
                return new Draft(draft.getId(), readValue);
            } catch (IOException e) {
                throw new DraftReadException(e);
            }
        }).collect(Collectors.toList());

        return DraftResult.builder().drafts(drafts).build();
    }

    public void delete(List<Long> draftIdList) {
        draftJpaRepository.deleteByIdList(draftIdList);
    }

    public void delete(long draftId) {
        draftJpaRepository.delete(draftId);
    }

    public void updateDraft(UpdateDraft draftUpdateRequest) {
        org.egov.pgrrest.read.persistence.entity.Draft draftFromDB = draftJpaRepository
            .findOne(draftUpdateRequest.getId());
        if (draftFromDB != null) {
            String updateDraft = convertToString(draftUpdateRequest.getDraft());
            draftFromDB.setDraft(updateDraft);
            draftJpaRepository.save(draftFromDB);
        }
    }

    private List<org.egov.pgrrest.read.persistence.entity.Draft> fetchDrafts(DraftSearchCriteria searchCriteria) {
        if (searchCriteria.getServiceCode() == null) {
            return draftJpaRepository
                .findByUserIdAndTenantId(searchCriteria.getUserId(), searchCriteria.getTenantId());
        } else {
            return draftJpaRepository
                .findByUserIdAndServiceCodeAndTenantId(searchCriteria.getUserId(), searchCriteria.getServiceCode(),
                    searchCriteria.getTenantId());
        }
    }

    private String convertToString(HashMap<String, Object> object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new MalformedDraftException(e);
        }
    }
}