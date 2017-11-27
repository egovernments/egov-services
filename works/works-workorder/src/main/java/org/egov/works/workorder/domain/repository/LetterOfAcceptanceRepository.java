package org.egov.works.workorder.domain.repository;

import org.egov.works.workorder.web.contract.LetterOfAcceptance;
import org.egov.works.workorder.web.contract.LetterOfAcceptanceSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LetterOfAcceptanceRepository {

    @Autowired
    private LetterOfAcceptanceJdbcRepository letterOfAcceptanceJdbcRepository;

    public List<LetterOfAcceptance> searchLOAs(final LetterOfAcceptanceSearchContract letterOfAcceptanceSearchCriteria,
                                                     final RequestInfo requestInfo) {
       return letterOfAcceptanceJdbcRepository.searchLOAs(letterOfAcceptanceSearchCriteria,requestInfo);
    }
}
