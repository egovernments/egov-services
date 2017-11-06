package org.egov.lams.services.service;

import java.util.Calendar;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.EstateSearchCriteria;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.common.web.response.EstateRegisterResponse;
import org.egov.lams.services.config.PropertiesManager;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.persistence.repository.EstateRegisterRepository;
import org.egov.lams.services.util.SequenceGenUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstateRegisterService {

	public static final String WF_ACTION_APPROVE = "Approve";
	public static final String WF_ACTION_REJECT = "Reject";
	public static final String WF_ACTION_CANCEL = "Cancel";
	
	@Autowired
	private ResponseFactory responseInfoFactory;

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private SequenceGenUtil sequenceGenService;
	
	@Autowired
	private EstateRegisterRepository estateRegisterRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	public EstateRegisterResponse getEstates(EstateSearchCriteria searchTaxHead, RequestInfo requestInfo) {
		List<EstateRegister> estateRegister = estateRegisterRepository.findForCriteria(searchTaxHead);
		return getEstateRegisterResponse(estateRegister, requestInfo);
	}

	public EstateRegisterResponse createAsync(EstateRegisterRequest estateRegisterRequest) {
		
		/*List<Long> registerIds = sequenceGenService.getIds(estateRegisterRequest.getLandRegisters().size(),
				propertiesManager.getCreateEstateSequence());*/
		
		estateRegisterRequest.getLandRegisters().stream().forEach(landRegister -> {
			landRegister.setId(sequenceGenService.getIds(1, propertiesManager.getCreateEstateSequence()).get(0));
			landRegister.getFloors().stream().forEach(floor -> {
				floor.setId(sequenceGenService.getIds(1, propertiesManager.getCreateEstateFloorsSequence()).get(0));
				floor.getUnits().stream().forEach(unit -> {
					unit.setId(sequenceGenService.getIds(1, propertiesManager.getCreateEstateUnitsSequence()).get(0));
				});
			});
		});
		
		/*int index = 0;
		int floorIndex;
		int unitIndex;
		for (EstateRegister estateRegister : estateRegisterRequest.getLandRegisters()) {
			estateRegister.setId(registerIds.get(index++));
			List<Long> floorIds = sequenceGenService.getIds(estateRegister.getFloors().size(),
					propertiesManager.getCreateEstateFloorsSequence());
			floorIndex = 0;
			for (FloorDetail floor : estateRegister.getFloors()) {
				floor.setId(floorIds.get(floorIndex++));
				List<Long> unitsIds = sequenceGenService.getIds(floor.getUnits().size(), propertiesManager.getCreateEstateUnitsSequence());
				unitIndex = 0;
				for (UnitDetail unit : floor.getUnits())
					unit.setId(unitsIds.get(unitIndex++));
			}
		}*/
		kafkaTemplate.send(propertiesManager.getStartEstateWorkflowTopic(), estateRegisterRequest);
		return getEstateRegisterResponse(estateRegisterRequest.getLandRegisters(),
				estateRegisterRequest.getRequestInfo());
	}
	
	public EstateRegisterResponse updateAsync(EstateRegisterRequest estateRegisterRequest) {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		estateRegisterRequest.getLandRegisters().stream().forEach(landRegister -> {
			if (landRegister.getWorkFlowDetails().getAction().equals(WF_ACTION_APPROVE))
				landRegister.setEstateNumber("E" + year + String.format("%05d",
						sequenceGenService.getIds(1, propertiesManager.getCreateEstateSequence()).get(0)));
		});
		kafkaTemplate.send(propertiesManager.getUpdateEstateWorkflowTopic(), estateRegisterRequest);
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
