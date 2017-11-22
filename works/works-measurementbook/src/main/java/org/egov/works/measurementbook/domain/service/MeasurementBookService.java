package org.egov.works.measurementbook.domain.service;

import org.egov.works.measurementbook.web.contract.MeasurementBookRequest;
import org.egov.works.measurementbook.web.contract.MeasurementBookResponse;
import org.egov.works.measurementbook.web.contract.MeasurementBookSearchContract;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MeasurementBookService {

	public MeasurementBookResponse create(MeasurementBookRequest measurementBookRequest) {
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		return measurementBookResponse;
	}

	public MeasurementBookResponse update(MeasurementBookRequest measurementBookRequest) {
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		return measurementBookResponse;
	}

	public MeasurementBookResponse search(MeasurementBookSearchContract measurementBookSearchContract,
			RequestInfo requestInfo) {
		MeasurementBookResponse measurementBookResponse = new MeasurementBookResponse();
		return measurementBookResponse;
	}
}
