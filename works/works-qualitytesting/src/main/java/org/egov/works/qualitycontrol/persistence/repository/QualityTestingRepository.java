package org.egov.works.qualitycontrol.persistence.repository;

import org.egov.works.qualitycontrol.web.contract.QualityTesting;
import org.egov.works.qualitycontrol.web.contract.QualityTestingSearchContract;
import org.egov.works.qualitycontrol.web.contract.RequestInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QualityTestingRepository {

    private QualityTestingJdbcRepository qualityTestingJdbcRepository;

    public List<QualityTesting> searchQualityTesting(final QualityTestingSearchContract qualityTestingSearchContract, final RequestInfo requestInfo) {
        return qualityTestingJdbcRepository.search(qualityTestingSearchContract,requestInfo);
    }
}
