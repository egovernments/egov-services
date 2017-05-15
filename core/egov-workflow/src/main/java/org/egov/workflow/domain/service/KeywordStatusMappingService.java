package org.egov.workflow.domain.service;

import org.egov.workflow.domain.model.ComplaintStatus;
import org.egov.workflow.domain.model.KeywordStatusMappingSearchCriteria;
import org.egov.workflow.persistence.repository.KeywordStatusMappingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordStatusMappingService {

    private KeywordStatusMappingRepository keywordStatusMappingRepository;

    public KeywordStatusMappingService(KeywordStatusMappingRepository keywordStatusMappingRepository) {
        this.keywordStatusMappingRepository = keywordStatusMappingRepository;
    }

    public List<ComplaintStatus> getStatusForKeyword(KeywordStatusMappingSearchCriteria searchCriteria){
        return keywordStatusMappingRepository.getAllStatusForKeyword(searchCriteria.getKeyword(),
            searchCriteria.getTenantId());
    }
}