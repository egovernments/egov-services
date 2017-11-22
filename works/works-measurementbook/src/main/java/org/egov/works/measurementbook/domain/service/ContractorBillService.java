package org.egov.works.measurementbook.domain.service;

import org.egov.works.measurementbook.web.contract.ContractorBillRequest;
import org.egov.works.measurementbook.web.contract.ContractorBillResponse;
import org.egov.works.measurementbook.web.contract.ContractorBillSearchContract;
import org.egov.works.measurementbook.web.contract.RequestInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ContractorBillService {

	public ContractorBillResponse create(ContractorBillRequest contractorBillRequest) {
		ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
		return contractorBillResponse;
	}

	public ContractorBillResponse update(ContractorBillRequest contractorBillRequest) {
		ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
		return contractorBillResponse;
	}

	public ContractorBillResponse search(ContractorBillSearchContract contractorBillSearchContract,
			RequestInfo requestInfo) {
		ContractorBillResponse contractorBillResponse = new ContractorBillResponse();
		return contractorBillResponse;
	}
}
