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

    public DraftCreateResponse saveDraft(DraftCreateRequest draftCreateRequest) {
        org.egov.pgrrest.read.persistence.entity.Draft draftEntity = createDraft(draftCreateRequest);
        return DraftCreateResponse.builder().id(draftEntity.getId()).build();
    }

    public DraftSearchResponse getDrafts(Long userId, String serviceCode, String tenantId) {
        List<org.egov.pgrrest.read.persistence.entity.Draft> draftsFromDb = getDraftsFromDb(userId, serviceCode, tenantId);
        TypeReference<HashMap<String, Object>> typereference = new TypeReference<HashMap<String, Object>>() {
        };

        List<Draft> drafts = draftsFromDb.stream().map(draft -> {
            try {
                HashMap<String, Object> readValue = objectMapper.readValue(draft.getDraft(), typereference);
                return new Draft(draft.getId(), readValue);
            } catch (IOException e) {
                throw new DraftReadException(e);
            }
        }).collect(Collectors.toList());

        return DraftSearchResponse.builder().draftResponses(drafts).build();
    }


    public void deleteDraft(List<Long> draftIdList) {
        draftJpaRepository.deleteByIdList(draftIdList);
    }

    public void updateDraft(DraftUpdateRequest draftUpdateRequest) {
        org.egov.pgrrest.read.persistence.entity.Draft draftFromDB = draftJpaRepository.findOne(draftUpdateRequest.getId());
        String updateDraft = convertToString(draftUpdateRequest.getDraft());
        if (draftFromDB != null) {
            draftFromDB.setDraft(updateDraft);
            draftJpaRepository.save(draftFromDB);
        }
    }

    private org.egov.pgrrest.read.persistence.entity.Draft createDraft(DraftCreateRequest draftCreateRequest) {
        String createDraft = convertToString(draftCreateRequest.getDraft());
        org.egov.pgrrest.read.persistence.entity.Draft draft = org.egov.pgrrest.read.persistence.entity.Draft.builder().serviceCode(draftCreateRequest.getServiceCode()).tenantId(draftCreateRequest.getTenantId()).userId(draftCreateRequest.getUserId()).draft(createDraft).build();
        draftJpaRepository.save(draft);
        return draft;
    }

    private List<org.egov.pgrrest.read.persistence.entity.Draft> getDraftsFromDb(Long userId, String serviceCode, String tenantId) {
        if (null == serviceCode) {
            return draftJpaRepository.findByUserIdAndTenantId(userId, tenantId);
        } else
            return draftJpaRepository.findByUserIdAndServiceCodeAndTenantId(userId, serviceCode, tenantId);
    }

    private String convertToString(Object object) {
        String draftMapper = null;
        try {
            draftMapper = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new MalformedDraftException(e);
        }
        return draftMapper;
    }
}