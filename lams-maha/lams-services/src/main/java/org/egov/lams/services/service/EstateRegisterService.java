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

	@Autowired
	private MasterDataService mdmsService;

	public EstateRegisterResponse getEstates(EstateSearchCriteria searchEstateCriteria, RequestInfo requestInfo) {
		
		if(searchEstateCriteria.getRegisterName()!=null || searchEstateCriteria.getSubRegisterName()!=null || searchEstateCriteria.getPropertyType()!=null)
			mdmsService.getEstateMasterCodes(requestInfo, searchEstateCriteria);
		
		List<EstateRegister> estateRegister = estateRegisterRepository.findForCriteria(searchEstateCriteria);
		
		estateRegister.stream().forEach(reg-> mdmsService.getEstateMaster(requestInfo, reg));
		
		return getEstateRegisterResponse(estateRegister, requestInfo);
	}

	public EstateRegisterResponse createAsync(EstateRegisterRequest estateRegisterRequest) {

//		estateRegisterRequest.getLandRegisters().stream()
//				.forEach(reg -> mdmsService.getEstateMaster(estateRegisterRequest.getRequestInfo(), reg));

		estateRegisterRequest.getLandRegisters().stream().forEach(landRegister -> {
			landRegister.setId(sequenceGenService.getIds(1, propertiesManager.getCreateEstateSequence()).get(0));
			landRegister.getFloors().stream().forEach(floor -> {
				floor.setId(sequenceGenService.getIds(1, propertiesManager.getCreateEstateFloorsSequence()).get(0));
				floor.getUnits().stream().forEach(unit -> {
					unit.setId(sequenceGenService.getIds(1, propertiesManager.getCreateEstateUnitsSequence()).get(0));
				});
			});
		});

		kafkaTemplate.send(propertiesManager.getCreateEstateKafkaTopic(), estateRegisterRequest);
		return getEstateRegisterResponse(estateRegisterRequest.getLandRegisters(),
				estateRegisterRequest.getRequestInfo());
	}

	public EstateRegisterResponse updateAsync(EstateRegisterRequest estateRegisterRequest) {
		
//		estateRegisterRequest.getLandRegisters().stream().forEach(reg -> mdmsService.getEstateMaster(estateRegisterRequest.getRequestInfo(), reg));
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		estateRegisterRequest.getLandRegisters().stream().forEach(landRegister -> {
			if (landRegister.getWorkFlowDetails().getAction().equals(WF_ACTION_APPROVE))
				landRegister.setEstateNumber("E" + year + String.format("%05d",
						sequenceGenService.getIds(1, propertiesManager.getCreateEstateSequence()).get(0)));
		});
		kafkaTemplate.send(propertiesManager.getUpdateEstateKafkaTopic(), estateRegisterRequest);
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
