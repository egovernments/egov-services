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

/** 
* 
* Author		Date			eGov-JIRA ticket	Commit message
* ---------------------------------------------------------------------------
* yosadhara		31st Oct 2017						Initial commit for Register service 
* Prasad		08th Nov 2017						Added isSummon value for getUniqueCode method calling
* Yosadhara		28th Nov 2017						Made isActive value as true if it is null
*/
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
	
	/**
	 * This method is to create register
	 * 
	 * @param registerRequest
	 * @return RegisterResponse
	 * @throws Exception
	 */
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
	
	/**
	 * This method is to update register
	 * 
	 * @param registerRequest
	 * @return RegisterResponse
	 * @throws Exception
	 */
	public RegisterResponse updateRegister(RegisterRequest registerRequest) throws Exception {

		kafkaTemplate.send(propertiesManager.getUpdateRegisterTopic(), registerRequest);

		return new RegisterResponse(
				responseInfoFactory.getResponseInfo(registerRequest.getRequestInfo(), HttpStatus.CREATED),
				registerRequest.getRegisters());
	}
	
	/**
	 * This method is to search register based on search criteria
	 * 
	 * @param registerSearchCriteria
	 * @param requestInfo
	 * @return RegisterResponse
	 */
	public RegisterResponse searchRegister(RegisterSearchCriteria registerSearchCriteria, RequestInfo requestInfo) {
		
		if (registerSearchCriteria.getIsActive() == null) {
			registerSearchCriteria.setIsActive(true);
		}
		List<Register> registers = registerRepository.search(registerSearchCriteria);
		return new RegisterResponse(responseInfoFactory.getResponseInfo(requestInfo, HttpStatus.CREATED), registers);
	}
}
