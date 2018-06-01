package org.egov.pt.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.producer.Producer;
import org.egov.pt.repository.DraftRepository;
import org.egov.pt.util.ResponseInfoFactory;
import org.egov.pt.web.models.Draft;
import org.egov.pt.web.models.DraftRequest;
import org.egov.pt.web.models.DraftResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DraftsService {
	
	@Autowired
	private Producer producer;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private PropertyConfiguration propertyConfiguration;
	
	@Autowired
	private DraftRepository draftRepository;

	public DraftResponse createDraft(DraftRequest draftRequest) {
		producer.push(propertyConfiguration.getSaveDraftsTopic(), draftRequest);
		List<Draft> drafts = new ArrayList<>();
		drafts.add(draftRequest.getDraft());
		return DraftResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(draftRequest.getRequestInfo(), true))
				.drafts(drafts).build();
		
	}
	
	public DraftResponse updateDraft(DraftRequest draftRequest) {
		producer.push(propertyConfiguration.getUpdateDraftsTopic(), draftRequest);
		List<Draft> drafts = new ArrayList<>();
		drafts.add(draftRequest.getDraft());
		return DraftResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(draftRequest.getRequestInfo(), true))
				.drafts(drafts).build();
		
	}
	
	public DraftResponse searchDrafts(RequestInfo requestInfo, String userId, String tenantId) {
		List<Draft> drafts = new ArrayList<>();
		try {
			drafts = draftRepository.getDrafts(userId, tenantId);
		}catch(Exception e) {
			log.info("Exception while fetching drafts: "+e);
			if(null == drafts)
				drafts = new ArrayList<>();
		}
		return DraftResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true))
				.drafts(drafts).build();
		
	}
}
