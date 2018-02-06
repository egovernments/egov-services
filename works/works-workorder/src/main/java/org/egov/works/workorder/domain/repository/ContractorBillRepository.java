package org.egov.works.workorder.domain.repository;

import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.works.workorder.web.contract.ContractorBillResponse;
import org.egov.works.workorder.web.contract.ContractorBillSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.egov.works.workorder.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ContractorBillRepository {

	private final LogAwareRestTemplate restTemplate;

	private final String contractorBillUrl;

	@Autowired
	public ContractorBillRepository(final LogAwareRestTemplate restTemplate,
                                    @Value("${egov.services.egov_works_measurementbook.hostname}") final String worksContractorBillHostname,
                                    @Value("${egov.services.egov_works_measurementbook.searchpath}") final String contractorBillSearchUrl) {

		this.restTemplate = restTemplate;
		this.contractorBillUrl = worksContractorBillHostname + contractorBillSearchUrl;
	}

	public ContractorBillResponse getByLoaNumbers(
			final ContractorBillSearchContract contractorBillSearchContract, final String tenantId,
			final RequestInfo requestInfo) {
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		StringBuilder url = new StringBuilder();
		url.append(contractorBillUrl).append(tenantId).append("&").append("letterOfAcceptanceNumbers=").append(contractorBillSearchContract.getLetterOfAcceptanceNumbers().get(0));

		return restTemplate.postForObject(url.toString(), requestInfoWrapper, ContractorBillResponse.class);

	}

}
