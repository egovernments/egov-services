package org.egov.wcms.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.CommonDataModel;
import org.egov.wcms.model.Gapcode;
import org.egov.wcms.model.enums.ApplicationType;
import org.egov.wcms.model.enums.GapcodeLastMonths;
import org.egov.wcms.model.enums.GapcodeLogic;
import org.egov.wcms.service.GapcodeService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.CommonEnumResponse;
import org.egov.wcms.web.contract.GapcodeGetRequest;
import org.egov.wcms.web.contract.GapcodeRequest;
import org.egov.wcms.web.contract.GapcodeResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
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
@Slf4j
@RequestMapping("/gapcode")
public class GapcodeController {

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private ValidatorUtils validatorUtils;

	@Autowired 
	private GapcodeService gapcodeServie;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;


	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> createGapcode(@RequestBody @Valid final GapcodeRequest gapcodeRequest,
			final BindingResult errors){

		if (errors.hasErrors()) {
			final ErrorResponse errRes = validatorUtils.populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		log.info("gapcodeRequest::" + gapcodeRequest);

		final List<ErrorResponse> errorResponses = validatorUtils.validateGapcodeRequest(gapcodeRequest,false);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		final List<Gapcode> gapcodeList = gapcodeServie.createGapcode(applicationProperties.getCreateGapcodeTopicName(),
				"gapcode-create", gapcodeRequest);

		return getSuccessResponse(gapcodeList, "Created", gapcodeRequest.getRequestInfo());
	}

	@PostMapping("_update")
	public ResponseEntity<?> updateGapcode(@RequestBody @Valid GapcodeRequest gapcodeRequest, final BindingResult errors){

		if (errors.hasErrors()) {
			final ErrorResponse errRes = validatorUtils.populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		log.info("gapcodeRequest::" + gapcodeRequest);

		final List<ErrorResponse> errorResponses = validatorUtils.validateGapcodeRequest(gapcodeRequest,false);
		if (!errorResponses.isEmpty())
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

		final List<Gapcode> gapcodeList = gapcodeServie.updateGapcode(applicationProperties.getUpdateGapcodeTopicName(),
				"gapcode-update", gapcodeRequest);

		return getSuccessResponse(gapcodeList, "Updated", gapcodeRequest.getRequestInfo());


	}
	@PostMapping("_search")
	public ResponseEntity<?> searchGapcode(@ModelAttribute @Valid final GapcodeGetRequest gapcodeGetRequest,
			final BindingResult modelAttributeBindingResult,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			final BindingResult requestBodyBindingResult){

		final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (modelAttributeBindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

		if (requestBodyBindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

		List<Gapcode> gapCodeList = null;
		try {
			gapCodeList = gapcodeServie.getGapcodes(gapcodeGetRequest);
		} catch (final Exception exception) {
			log.error("Error while processing request " + gapcodeGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(gapCodeList, null, requestInfo);
	}

	@PostMapping("/formula/_search")
	@ResponseBody
	public ResponseEntity<?> searchGapcodeFormula(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult bindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (bindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

		return getSuccessResponse(gapcodeServie.getFormulaQuery(), requestInfoWrapper.getRequestInfo());
	}

	@PostMapping("/lastmonths/_search")
	@ResponseBody
	public ResponseEntity<?> searchGapcodeNoOfLastMonths(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult bindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (bindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

		final List<CommonDataModel> modelList = new ArrayList<>();
		for (final GapcodeLastMonths key : GapcodeLastMonths.values())
			modelList.add(new CommonDataModel(key.name(), key.getValue()));
		return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
	}
	
	@PostMapping("/logic/_search")
	@ResponseBody
	public ResponseEntity<?> searchGapcodeLogic(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult bindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (bindingResult.hasErrors())
			return errHandler.getErrorResponseEntityForMissingRequestInfo(bindingResult, requestInfo);

		final List<CommonDataModel> modelList = new ArrayList<>();
		for (final GapcodeLogic key : GapcodeLogic.values())
			modelList.add(new CommonDataModel(key.name(), key));
		return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
	}
	
	

	private ResponseEntity<?> getSuccessResponse(final List<CommonDataModel> modelList,
			final RequestInfo requestInfo) {
		final CommonEnumResponse response = new CommonEnumResponse();
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.CREATED.toString());
		response.setResponseInfo(responseInfo);
		response.setDataModelList(modelList);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	private ResponseEntity<?> getSuccessResponse(final List<Gapcode> gapcodeList, final String mode,
			final RequestInfo requestInfo) {
		final GapcodeResponse gapcodeResponse = new GapcodeResponse();
		gapcodeResponse.setGapcodeResponse(gapcodeList);
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		if (StringUtils.isNotBlank(mode))
			responseInfo.setStatus(HttpStatus.CREATED.toString());
		else
			responseInfo.setStatus(HttpStatus.OK.toString());
		gapcodeResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(gapcodeResponse, HttpStatus.OK);
	}
}
