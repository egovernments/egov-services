package org.egov.lams.services.service;

import org.egov.lams.common.web.request.LandRegisterRequest;
import org.egov.lams.common.web.response.LandRegisterResponse;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.util.SequenceGenUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LandRegisterService {

	@Autowired
	private ResponseFactory responseInfoFactory;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private SequenceGenUtil sequenceGenService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public ResponseEntity<?> createAsync(LandRegisterRequest landRegisterRequest) {

		log.debug("Land Register : " + landRegisterRequest.getLandRegisters());

		landRegisterRequest.getLandRegisters().stream().forEach(landRegister -> {
			landRegister.setId(sequenceGenService.getIds(1, "seq_eglams_landregister").get(0));

			landRegister.getDocuments().stream().forEach(documents -> {
				documents.setId(sequenceGenService.getIds(1, "seq_eglams_document").get(0));
			});
		});

		System.err.println("landRegisterRequest.getLandRegisters(): "
				+ landRegisterRequest.getLandRegisters().get(0).getTenantId());

		kafkaTemplate.send(propertiesManager.getCreateLandRegisterKafkaTopic(), landRegisterRequest);
		return getLandRegisterResponse(landRegisterRequest);
	}

	public ResponseEntity<?> updateAsync(LandRegisterRequest landRegisterRequest) {

		log.debug("Land Register : " + landRegisterRequest.getLandRegisters());

		System.err.println("landRegisterRequest.getLandRegisters(): " + landRegisterRequest.getLandRegisters());

		kafkaTemplate.send(propertiesManager.getUpdateLandRegisterKafkaTopic(), landRegisterRequest);
		return getLandRegisterResponse(landRegisterRequest);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResponseEntity<?> getLandRegisterResponse(LandRegisterRequest landRegisterRequest) {
		LandRegisterResponse landRegisterResponse = new LandRegisterResponse();
		landRegisterResponse.setLandRegisters(landRegisterRequest.getLandRegisters());
		landRegisterResponse.setResponseInfo(
				responseInfoFactory.getResponseInfo(landRegisterRequest.getRequestInfo(), HttpStatus.OK));
		return new ResponseEntity(landRegisterResponse, HttpStatus.OK);
	}
}
