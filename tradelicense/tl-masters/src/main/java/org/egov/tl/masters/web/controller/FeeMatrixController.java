package org.egov.tl.masters.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.FeeMatrixContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.FeeMatrixResponse;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.service.FeeMatrixService;
import org.egov.tradelicense.domain.exception.CustomBindException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to trade license feematrix master
 * 
 * @author Pavan Kumar Kamma
 *
 */
@RestController
@RequestMapping(path = "/feematrix/v1")
public class FeeMatrixController {

	@Autowired
	FeeMatrixService feeMatrixService;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public FeeMatrixResponse createFeeMatrix(@Valid @RequestBody FeeMatrixRequest feeMatrixRequest,
			BindingResult errors) throws Exception {

		RequestInfo requestInfo = feeMatrixRequest.getRequestInfo();
		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}

		ModelMapper model = new ModelMapper();
		FeeMatrixResponse feeMatrixResponse = new FeeMatrixResponse();
		feeMatrixResponse.setResponseInfo(getResponseInfo(requestInfo));
		List<FeeMatrix> feeMatrices = new ArrayList<FeeMatrix>();

		for (FeeMatrixContract feeMatrixContract : feeMatrixRequest.getFeeMatrices()) {

			FeeMatrix feeMatrix = new FeeMatrix();
			model.map(feeMatrixContract, feeMatrix);
			AuditDetails auditDetails = setAuditDetails(requestInfo, Boolean.TRUE);
			feeMatrix.setAuditDetails(auditDetails);
			feeMatrices.add(feeMatrix);
		}
		feeMatrices = feeMatrixService.createFeeMatrixMaster(feeMatrices, requestInfo);

		List<FeeMatrixContract> feeMatrixContract = new ArrayList<FeeMatrixContract>();

		for (FeeMatrix feeMatrix : feeMatrices) {

			FeeMatrixContract contract = new FeeMatrixContract();
			model.map(feeMatrix, contract);
			feeMatrixContract.add(contract);
		}
		feeMatrixResponse.setFeeMatrices(feeMatrixContract);

		return feeMatrixResponse;
	}

	

	// get responseinfo from requestinfo
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
	}

	private AuditDetails setAuditDetails(RequestInfo requestInfo, Boolean create) {
		AuditDetails auditDetails = new AuditDetails();
		if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {

			if (create == Boolean.TRUE) {
				auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.setCreatedTime(new Date().getTime());
				auditDetails.setLastModifiedTime(new Date().getTime());
			} else {
				auditDetails.setLastModifiedTime(new Date().getTime());
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}

		}

		return auditDetails;
	}
}