package org.egov.infra.mdms.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.infra.mdms.service.MDMSService;
import org.egov.infra.mdms.utils.MDMSRequestValidator;
import org.egov.infra.mdms.utils.ResponseInfoFactory;
import org.egov.mdms.model.MDMSCreateErrorResponse;
import org.egov.mdms.model.MDMSCreateRequest;
import org.egov.mdms.model.MdmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@RestController
@Slf4j
@RequestMapping(value = "/v1")
public class MDMSController {

	@Autowired
	private MDMSService mdmsService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private MDMSRequestValidator mDMSRequestValidator;
	

	@PostMapping("_create")
	@ResponseBody
	private ResponseEntity<?> create(@RequestBody @Valid MDMSCreateRequest mDMSCreateRequest) throws Exception {
		log.info("MDMSController mDMSCreateRequest:" + mDMSCreateRequest);
		List<String> keys = new ArrayList<>();
		List<String> isRequestPayload = new ArrayList<>();
		ArrayList<Object> validationError = mDMSRequestValidator.validateRequest(mDMSCreateRequest, keys, true,isRequestPayload);
		Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
		}.getType();
		Gson gson = new Gson();
		Object errorData = gson.fromJson(validationError.toString(), type);
		if (!validationError.isEmpty()) {
			MDMSCreateErrorResponse mDMSCreateErrorResponse = new MDMSCreateErrorResponse();
			mDMSCreateErrorResponse.setResponseInfo(
					responseInfoFactory.createResponseInfoFromRequestInfo(mDMSCreateRequest.getRequestInfo(), false));
			if(!isRequestPayload.isEmpty())
			mDMSCreateErrorResponse.setMessage("There Are duplicate Records Are Exist In Request. The keys for this records are: " + keys);
			else 
			mDMSCreateErrorResponse.setMessage("This record already exists. The keys for this record are: " + keys);
			mDMSCreateErrorResponse.setData(errorData);
			return new ResponseEntity<>(mDMSCreateErrorResponse, HttpStatus.BAD_REQUEST);
		}
		Map<String, Map<String, JSONArray>> response = mdmsService.gitPush(mDMSCreateRequest, true); 
		MdmsResponse mdmsResponse = new MdmsResponse();
		mdmsResponse.setMdmsRes(response);
		mdmsResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(mDMSCreateRequest.getRequestInfo(), true)); 

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("_update")
	@ResponseBody
	private ResponseEntity<?> update(@RequestBody @Valid MDMSCreateRequest mDMSCreateRequest) throws Exception {
		log.info("MDMSController mDMSCreateRequest:" + mDMSCreateRequest);
		List<String> keys = new ArrayList<>();
		List<String> isRequestPayload = new ArrayList<>();
		ArrayList<Object> validationError = mDMSRequestValidator.validateRequest(mDMSCreateRequest, keys, false,isRequestPayload);
		Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
		}.getType();
		Gson gson = new Gson();
		Object errorData = gson.fromJson(validationError.toString(), type);
		if (!validationError.isEmpty()) {
			MDMSCreateErrorResponse mDMSCreateErrorResponse = new MDMSCreateErrorResponse();
			mDMSCreateErrorResponse.setResponseInfo(
					responseInfoFactory.createResponseInfoFromRequestInfo(mDMSCreateRequest.getRequestInfo(), false));
			mDMSCreateErrorResponse.setMessage(
					"You have attempted to update a key that defines this record or the record does not exist. "
							+ "To create a new entry, please use the Create screen. The keys for this record are: "
							+ keys);
			mDMSCreateErrorResponse.setData(errorData);
			return new ResponseEntity<>(mDMSCreateErrorResponse, HttpStatus.BAD_REQUEST);
		}
		Map<String, Map<String, JSONArray>> response = mdmsService.gitPush(mDMSCreateRequest, false);
		MdmsResponse mdmsResponse = new MdmsResponse();
		mdmsResponse.setMdmsRes(response);
		mdmsResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(mDMSCreateRequest.getRequestInfo(), true));

		return new ResponseEntity<>(mdmsResponse, HttpStatus.OK);
	}

	@PostMapping("config/_search")
	@ResponseBody
	private ResponseEntity<?> configSearch(@RequestBody RequestInfo requestInfo,
			@RequestParam("tenantId") String tenantId, @RequestParam(value = "module" ,required = true) String module,
			@RequestParam(value = "master", required = false) String master) throws Exception {
		log.info("Search criteria: " + tenantId + "," + module + "," + master);
		try {
			List<Object> response = mdmsService.getConfigs(tenantId, module, master);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error ", e);
			throw e;
		}

	}

}
