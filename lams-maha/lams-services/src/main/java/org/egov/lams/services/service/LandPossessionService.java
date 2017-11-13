package org.egov.lams.services.service;

import java.util.Calendar;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.LandPossession;
import org.egov.lams.common.web.contract.LandPossessionRequest;
import org.egov.lams.common.web.contract.LandPossessionResponse;
import org.egov.lams.common.web.contract.LandPossessionSearchCriteria;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.persistence.repository.LandPossessionRepository;
import org.egov.lams.services.util.SequenceGenUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
@Service
public class LandPossessionService {
	@Autowired
	private ResponseFactory responseInfoFactory;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private SequenceGenUtil sequenceGenService;

	@Autowired
	private LandPossessionRepository landPossessionRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private MasterDataService mdmsService;

	public LandPossessionResponse create(LandPossessionRequest landPossessionRequest) {

		landPossessionRequest.getLandPossession().stream().forEach(landPossession -> mdmsService
				.getLandPossessionMaster(landPossessionRequest.getRequestInfo(), landPossession));
		
		landPossessionRequest.getLandPossession().stream().forEach(landPossession -> {
			landPossession.setId(sequenceGenService.getIds(1, "seq_eg_lams_landpossession").get(0));
		});
		landPossessionRequest.getLandPossession().stream().forEach(landPossession -> {
			landPossession.setPossessionNumber(sequenceGen());
		});

		kafkaTemplate.send(propertiesManager.getCreateLandPossessionKafkaTopic(), landPossessionRequest);
		return getLandPossessionResponse(landPossessionRequest.getLandPossession(),
				landPossessionRequest.getRequestInfo());
	}

	public LandPossessionResponse update(LandPossessionRequest landPossessionRequest) {
		
		landPossessionRequest.getLandPossession().stream().forEach(landPossession -> mdmsService
				.getLandPossessionMaster(landPossessionRequest.getRequestInfo(), landPossession));
		
		kafkaTemplate.send(propertiesManager.getUpdateLandPossessionKafkaTopic(), landPossessionRequest);
		return getLandPossessionResponse(landPossessionRequest.getLandPossession(),
				landPossessionRequest.getRequestInfo());
	}

	public LandPossessionResponse search(LandPossessionSearchCriteria landPossessionSearchCriteria,
			RequestInfo requestInfo) {

		return landPossessionRepository.search(landPossessionSearchCriteria);
	}

	private LandPossessionResponse getLandPossessionResponse(List<LandPossession> landPossession,
			RequestInfo requestInfo) {
		LandPossessionResponse landPossessionResponse = new LandPossessionResponse();
		landPossessionResponse.setLandPossession(landPossession);
		landPossessionResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return landPossessionResponse;
	}

	private String sequenceGen() {
		String id = sequenceGenService.getIds(1, "seq_eg_lams_landpossession").get(0);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		String idgen = String.format("%05d", id);
		String seqId = year + idgen;
		return seqId;
	}
}
