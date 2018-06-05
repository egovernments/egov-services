package org.egov.pt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pt.config.PropertyConfiguration;
import org.egov.pt.producer.Producer;
import org.egov.pt.repository.DraftRepository;
import org.egov.pt.util.PropertyUtil;
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
	
	@Autowired
	private PropertyUtil propertyUtil;

	public DraftResponse createDraft(DraftRequest draftRequest) {
		enrichDraftsForCreate(draftRequest);
		List<Draft> drafts = new ArrayList<>();
		drafts.add(draftRequest.getDraft());
		producer.push(propertyConfiguration.getSaveDraftsTopic(), draftRequest);
		return DraftResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(draftRequest.getRequestInfo(), true))
				.drafts(drafts).build();
	}
	
	public void enrichDraftsForCreate(DraftRequest draftRequest) {
		draftRequest.getDraft().setId(UUID.randomUUID().toString());
		draftRequest.getDraft().setAuditDetails(propertyUtil.getAuditDetails(draftRequest.getRequestInfo().getUserInfo().getId().toString(), true));
	}
	
	public DraftResponse updateDraft(DraftRequest draftRequest) {
		enrichDraftsForUpdate(draftRequest);
		List<Draft> drafts = new ArrayList<>();
		drafts.add(draftRequest.getDraft());
		producer.push(propertyConfiguration.getUpdateDraftsTopic(), draftRequest);
		return DraftResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(draftRequest.getRequestInfo(), true))
				.drafts(drafts).build();
		
	}
	
	public void enrichDraftsForUpdate(DraftRequest draftRequest) {
		draftRequest.getDraft().setAuditDetails(propertyUtil.getAuditDetails(draftRequest.getRequestInfo().getUserInfo().getId().toString(), false));
	}
	
	public DraftResponse searchDrafts(RequestInfo requestInfo, String userId, String tenantId) {
		List<Draft> drafts = null;
		try {
			drafts = draftRepository.getDrafts(userId, tenantId);
		}catch(Exception e) {
			log.info("Exception while fetching drafts: "+e);
			drafts = new ArrayList<>();
		}
		return DraftResponse.builder().responseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true))
				.drafts(drafts).build();
		
	}
}
