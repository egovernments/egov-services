package org.egov.commons.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.commons.model.Module;
import org.egov.commons.service.ModuleService;
import org.egov.commons.web.contract.ModuleGetRequest;
import org.egov.commons.web.contract.ModuleResponse;
import org.egov.commons.web.contract.RequestInfo;
import org.egov.commons.web.contract.ResponseInfo;
import org.egov.commons.web.contract.factory.ResponseInfoFactory;
import org.egov.commons.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modules")
public class ModuleController {
	private static final Logger logger = LoggerFactory.getLogger(ModuleController.class);
	@Autowired
	private ModuleService moduleService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid ModuleGetRequest moduleGetRequest,
			BindingResult bindingResult, @RequestBody RequestInfo requestInfo) {

		// validate header
		if(requestInfo.getApiId() == null || requestInfo.getVer() == null || requestInfo.getTs() == null ) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestInfo);
		}

		// validate input params
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(bindingResult, requestInfo);
		}

		// Call service
		List<Module> modulesList = null;
		try {
			modulesList = moduleService.getModules(moduleGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + moduleGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(modulesList, requestInfo);
	}
	
	/**
	 * Populate Response object and returnmodulesList
	 * 
	 * @param modulesList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<Module> modulesList, RequestInfo requestInfo) {
		ModuleResponse moduleRes = new ModuleResponse();
		moduleRes.setModules(modulesList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		moduleRes.setResponseInfo(responseInfo);
		return new ResponseEntity<ModuleResponse>(moduleRes, HttpStatus.OK);

	}
}
