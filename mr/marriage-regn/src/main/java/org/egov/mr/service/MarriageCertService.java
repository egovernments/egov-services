package org.egov.mr.service;

import java.util.List;

import org.egov.mr.model.Page;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.repository.MarriageCertRepository;
import org.egov.mr.web.contract.MarriageCertCriteria;
import org.egov.mr.web.contract.ReissueCertResponse;
import org.egov.mr.web.contract.RequestInfo;
import org.egov.mr.web.contract.ResponseInfo;
import org.egov.mr.web.contract.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarriageCertService {

	@Autowired
	private MarriageCertRepository marriageCertRepository;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	public ReissueCertResponse getMarriageCerts(MarriageCertCriteria marriageCertCriteria, RequestInfo requestInfo) {

		return getSuccessResponseForSearch(marriageCertRepository.findForCriteria(marriageCertCriteria), requestInfo);
	}

	private ReissueCertResponse getSuccessResponseForSearch(List<ReissueCertAppl> marriageCertList,
			RequestInfo requestInfo) {
		ReissueCertResponse reissueCertResponse = new ReissueCertResponse();
		Page page = new Page();
		reissueCertResponse.setReissueApplications(marriageCertList);
		log.info("marriageCertList=" + marriageCertList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		reissueCertResponse.setResponseInfo(responseInfo);
		int totalresults = marriageCertList.size();
		page.setTotalResults(totalresults);
		reissueCertResponse.setPage(page);
		return reissueCertResponse;
	}
}
