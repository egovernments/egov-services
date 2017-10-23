package org.egov.lams.services.service;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.FloorDetail;
import org.egov.lams.common.web.contract.UnitDetail;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.common.web.request.EstateRegisterResponse;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.util.SequenceGenUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstateRegisterService {

	@Autowired
	private ResponseFactory responseInfoFactory;

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private SequenceGenUtil sequenceGenService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public EstateRegisterResponse createAsync(EstateRegisterRequest estateRegisterRequest) {
		
		List<Long> registerIds = sequenceGenService.getIds(estateRegisterRequest.getLandRegisters().size(),
				"seq_eglams_estateregistration");
		int index = 0;
		int floorIndex;
		int unitIndex;
		for (EstateRegister estateRegister : estateRegisterRequest.getLandRegisters()) {
			estateRegister.setId(registerIds.get(index++));
			List<Long> floorIds = sequenceGenService.getIds(estateRegister.getFloors().size(),
					"seq_eglams_floordetails");
			floorIndex = 0;
			for (FloorDetail floor : estateRegister.getFloors()) {
				floor.setId(floorIds.get(floorIndex++));
				List<Long> unitsIds = sequenceGenService.getIds(floor.getUnits().size(), "seq_eglams_unitdetails");
				unitIndex = 0;
				for (UnitDetail unit : floor.getUnits())
					unit.setId(unitsIds.get(unitIndex++));
			}
		}
		kafkaTemplate.send(propertiesManager.getCreateEstateKafkaTopic(), estateRegisterRequest);
		return getEstateRegisterResponse(estateRegisterRequest.getLandRegisters(),
				estateRegisterRequest.getRequestInfo());
	}

	private EstateRegisterResponse getEstateRegisterResponse(List<EstateRegister> estateRegister,
			RequestInfo requestInfo) {
		EstateRegisterResponse estateRegisterResponse = new EstateRegisterResponse();
		estateRegisterResponse.setLandRegisters(estateRegister);
		estateRegisterResponse.setResponseInfo(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.OK));
		return estateRegisterResponse;
	}
}
