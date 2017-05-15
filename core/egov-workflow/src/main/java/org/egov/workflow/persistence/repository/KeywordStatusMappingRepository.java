package org.egov.workflow.persistence.repository;

import org.egov.workflow.persistence.entity.ComplaintStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeywordStatusMappingRepository {

    private KeywordStatusMappingJpaRepository keywordStatusMappingJpaRepository;

    private ComplaintStatusJpaRepository complaintStatusJpaRepository;

    public KeywordStatusMappingRepository(KeywordStatusMappingJpaRepository keywordStatusMappingJpaRepository,
                                          ComplaintStatusJpaRepository complaintStatusJpaRepository) {
        this.keywordStatusMappingJpaRepository = keywordStatusMappingJpaRepository;
        this.complaintStatusJpaRepository = complaintStatusJpaRepository;
    }

    public List<org.egov.workflow.domain.model.ComplaintStatus> getAllStatusForKeyword(String keyWord, String tenantId){

        List<String> statusCodes =  keywordStatusMappingJpaRepository.findByKeywordAndTenantId(keyWord,tenantId)
            .stream()
            .map(status -> status.getId().getCode())
            .collect(Collectors.toList());

        return complaintStatusJpaRepository.findByCodeInAndTenantId(statusCodes,tenantId)
            .stream()
            .map(ComplaintStatus::toDomain)
            .collect(Collectors.toList());
    }

}
