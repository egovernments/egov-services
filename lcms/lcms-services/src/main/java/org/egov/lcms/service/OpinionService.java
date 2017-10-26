package org.egov.lcms.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.models.OpinionRequest;
import org.egov.lcms.models.OpinionResponse;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.springframework.stereotype.Service;

@Service
public class OpinionService {

	public OpinionResponse createOpinion(OpinionRequest opinionRequest) throws Exception {
		return new OpinionResponse();
	}

	public OpinionResponse updateOpinion(OpinionRequest opinionRequest) {
		return new OpinionResponse();
	}

	public OpinionResponse searchOpinion(RequestInfo requestInfo, OpinionSearchCriteria opinionRequest) {
		return new OpinionResponse();
	}
}