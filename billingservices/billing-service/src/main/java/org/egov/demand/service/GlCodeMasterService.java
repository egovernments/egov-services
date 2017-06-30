package org.egov.demand.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.config.ApplicationProperties;
import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.model.GlCodeMasterCriteria;
import org.egov.demand.model.GlCodeMaster;
import org.egov.demand.repository.GlCodeMasterRepository;
import org.egov.demand.util.SequenceGenService;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.GlCodeMasterRequest;
import org.egov.demand.web.contract.GlCodeMasterResponse;
import org.egov.demand.web.contract.factory.ResponseFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GlCodeMasterService {

	private static final Logger logger = LoggerFactory.getLogger(GlCodeMasterService.class);
	
	@Autowired
	private ResponseFactory responseInfoFactory;
	
	@Autowired
	private GlCodeMasterRepository glCodeMasterRepository;

	@Autowired
	private SequenceGenService sequenceGenService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;
	
	public GlCodeMasterResponse getGlCodes(GlCodeMasterCriteria searchGlCode, RequestInfo requestInfo) {
		logger.info("GlCodeMasterService getTaxHeads");
		List<GlCodeMaster> glCodeMaster= glCodeMasterRepository.findForCriteria(searchGlCode);
		return getGlCodeMasterResponse(glCodeMaster,requestInfo);
	}

	public void create(GlCodeMasterRequest glCodeMasterRequest) {
		glCodeMasterRepository.create(glCodeMasterRequest);
	}
	
	public GlCodeMasterResponse createAsync(GlCodeMasterRequest glCodeMasterRequest) {
		List<GlCodeMaster> glCodeMaster = glCodeMasterRequest.getGlCodeMasters();

		List<String> glCodeMasterIds = sequenceGenService.getIds(glCodeMaster.size(),
				applicationProperties.getGlCodeMasterseqName());
		
		int id=0;
		for (GlCodeMaster master: glCodeMaster) {
			master.setId(glCodeMasterIds.get(id));
		}
		glCodeMasterRequest.setGlCodeMasters(glCodeMaster);

		logger.info("taxHeadMasterRequest createAsync::" + glCodeMasterRequest);

		kafkaTemplate.send(applicationProperties.getCreateGlCodeMasterTopicName(),
				applicationProperties.getCreateGlCodeMasterTopicKey(), glCodeMasterRequest);
		// kafkaTemplate.send(applicationProperties.getCreateGlCodeMasterTopicName(),
		// taxHeadMasterRequest);

		return getGlCodeMasterResponse(glCodeMaster, glCodeMasterRequest.getRequestInfo());
	}
	
	private GlCodeMasterResponse getGlCodeMasterResponse(List<GlCodeMaster> glCodeMaster, RequestInfo requestInfo) {
		GlCodeMasterResponse glCodeMasterResponse = new GlCodeMasterResponse();
		glCodeMasterResponse.setGlCodeMasters(glCodeMaster);
		glCodeMasterResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return glCodeMasterResponse;
	}
	
	
}
