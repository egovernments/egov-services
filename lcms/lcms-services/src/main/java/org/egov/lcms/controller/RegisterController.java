package org.egov.lcms.controller;

import javax.validation.Valid;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.RegisterRequest;
import org.egov.lcms.models.RegisterResponse;
import org.egov.lcms.models.RegisterSearchCriteria;
import org.egov.lcms.models.RequestInfoWrapper;
import org.egov.lcms.service.RegisterService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/legalcase/register/")
public class RegisterController {

	@Autowired
	RegisterService registerService;

	@Autowired
	ResponseFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@RequestMapping(path = "_create")
	public ResponseEntity<?> createRegister(@RequestBody @Valid RegisterRequest registerRequest) throws Exception {

		RegisterResponse registerResponse = registerService.createRegister(registerRequest);
		return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_update")
	public ResponseEntity<?> updateRegister(@RequestBody @Valid RegisterRequest registerRequest) throws Exception {

		RegisterResponse registerResponse = registerService.updateRegister(registerRequest);
		return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
	}

	@RequestMapping(path = "_search")
	public ResponseEntity<?> searchRegister(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid RegisterSearchCriteria registerSearchCriteria, BindingResult bindingResult) throws Exception {
		if (bindingResult.hasErrors()) {
			throw new CustomException(propertiesManager.getInvalidTenantCode(),
					propertiesManager.getExceptionMessage());
		}
		RegisterResponse registerResponse = registerService.searchRegister(registerSearchCriteria, requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
	}
}
