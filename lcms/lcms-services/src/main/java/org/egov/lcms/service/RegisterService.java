package org.egov.lcms.service;

import java.util.List;
import org.egov.common.contract.request.RequestInfo;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.Register;
import org.egov.lcms.models.RegisterRequest;
import org.egov.lcms.models.RegisterResponse;
import org.egov.lcms.models.RegisterSearchCriteria;
import org.egov.lcms.repository.RegisterRepository;
import org.egov.lcms.util.UniqueCodeGeneration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	UniqueCodeGeneration uniqueCodeGeneration;

	@Autowired
	RegisterRepository registerRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public RegisterResponse createRegister(RegisterRequest registerRequest) throws Exception {

		String code = null;
		for (Register register : registerRequest.getRegisters()) {

			code = uniqueCodeGeneration.getUniqueCode(register.getTenantId(), registerRequest.getRequestInfo(),
					propertiesManager.getRegisterUlbFormat(), propertiesManager.getRegisterUlbName(), Boolean.FALSE,
					null,Boolean.FALSE);
			register.setCode(code);
		}

		kafkaTemplate.send(propertiesManager.getCreateRegisterTopic(), registerRequest);

		return new RegisterResponse(
				responseInfoFactory.getResponseInfo(registerRequest.getRequestInfo(), HttpStatus.CREATED),
				registerRequest.getRegisters());
	}

	public RegisterResponse updateRegister(RegisterRequest registerRequest) throws Exception {

		kafkaTemplate.send(propertiesManager.getUpdateRegisterTopic(), registerRequest);

		return new RegisterResponse(
				responseInfoFactory.getResponseInfo(registerRequest.getRequestInfo(), HttpStatus.CREATED),
				registerRequest.getRegisters());
	}

	public RegisterResponse searchRegister(RegisterSearchCriteria registerSearchCriteria, RequestInfo requestInfo) {
		
		if (registerSearchCriteria.getIsActive() == null) {
			registerSearchCriteria.setIsActive(true);
		}
		List<Register> registers = registerRepository.search(registerSearchCriteria);
		return new RegisterResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), registers);
	}
}
