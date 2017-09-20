package org.egov.wcms.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.MeterStatus;
import org.egov.wcms.service.MeterStatusService;
import org.egov.wcms.util.ValidatorUtils;
import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.egov.wcms.web.contract.MeterStatusReq;
import org.egov.wcms.web.contract.MeterStatusRes;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
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
@RequestMapping("/meterStatus")
public class MeterStatusController {
    public static final Logger logger = LoggerFactory.getLogger(MeterStatusController.class);

    @Autowired
    private ValidatorUtils validatorUtils;

    @Autowired
    private MeterStatusService meterStatusService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errHandler;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> createMeterStatus(@RequestBody @Valid final MeterStatusReq meterStatusRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("MeterStatusRequest :" + meterStatusRequest);
        final List<ErrorResponse> errorResponses = validatorUtils.validateMeterStatusRequest(meterStatusRequest, false);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        final List<MeterStatus> listOfMeterStatus = meterStatusService.pushCreateToQueue(meterStatusRequest);
        return getSuccessResponse(listOfMeterStatus, "Created", meterStatusRequest.getRequestInfo());
    }

    @PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> updateMeterStatus(@RequestBody @Valid final MeterStatusReq meterStatusRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = validatorUtils.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("MeterStatusRequest :" + meterStatusRequest);
        final List<ErrorResponse> errorResponses = validatorUtils.validateMeterStatusRequest(meterStatusRequest, true);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        final List<MeterStatus> listOfMeterStatus = meterStatusService.pushUpdateToQueue(meterStatusRequest);
        return getSuccessResponse(listOfMeterStatus, null, meterStatusRequest.getRequestInfo());
    }

    @PostMapping(value = "/_search")
    @ResponseBody
    public ResponseEntity<?> searchMeterStatus(@ModelAttribute @Valid final MeterStatusGetRequest meterStatusGetRequest,
            final BindingResult modelAttributeBindingResult,
            @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
        final List<MeterStatus> meterStatuses = meterStatusService.getMeterStatus(meterStatusGetRequest);
        return getSuccessResponse(meterStatuses, null, requestInfo);
    }

    private ResponseEntity<?> getSuccessResponse(final List<MeterStatus> meterStatusList, final String mode,
            final RequestInfo requestInfo) {
        final MeterStatusRes meterStatusResponse = new MeterStatusRes();
        meterStatusResponse.setMeterStatus(meterStatusList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if (StringUtils.isNotBlank(mode))
            responseInfo.setStatus(HttpStatus.CREATED.toString());
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        meterStatusResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(meterStatusResponse, HttpStatus.OK);

    }

}
