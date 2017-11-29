package org.egov.works.measurementbook.domain.repository;

import org.egov.works.measurementbook.web.contract.MeasurementBook;
import org.egov.works.measurementbook.web.contract.MeasurementBookSearchContract;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MeasurementBookRepository {

    @Autowired
    private MeasurementBookJdbcRepository measurementBookJdbcRepository;

    public List<MeasurementBook> searchMeasurementBooks(final MeasurementBookSearchContract measurementBookSearchContract, final RequestInfo requestInfo) {
        return measurementBookJdbcRepository.searchMeasurementBooks(measurementBookSearchContract,requestInfo);
    }
}
