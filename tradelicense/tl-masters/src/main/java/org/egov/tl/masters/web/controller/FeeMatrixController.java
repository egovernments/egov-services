package org.egov.tl.masters.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.FeeMatrixContract;
import org.egov.tl.commons.web.contract.FeeMatrixSearchContract;
import org.egov.tl.commons.web.contract.FeeMatrixSearchCriteriaContract;
import org.egov.tl.commons.web.contract.FeeMatrixSearchResponse;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.FeeMatrixResponse;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixSearch;
import org.egov.tl.masters.domain.model.FeeMatrixSearchCriteria;
import org.egov.tl.masters.domain.service.FeeMatrixService;
import org.egov.tradelicense.domain.exception.CustomBindException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
			AuditDetails auditDetails = setAuditDetails(feeMatrixContract.getAuditDetails(), requestInfo, Boolean.TRUE);
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

	/**
	 * Description : This api for updating feeMatrix master
	 * 
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 * @throws Exception
	 */

	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public FeeMatrixResponse updateFeeMatrixMaster(@Valid @RequestBody FeeMatrixRequest feeMatrixRequest,
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
			AuditDetails auditDetails = setAuditDetails(feeMatrixContract.getAuditDetails(), requestInfo, Boolean.FALSE);
			feeMatrix.setAuditDetails(auditDetails);
			feeMatrices.add(feeMatrix);
		}
		feeMatrices = feeMatrixService.updateFeeMatrixMaster(feeMatrices, requestInfo);

		List<FeeMatrixContract> feeMatrixContract = new ArrayList<FeeMatrixContract>();

		for (FeeMatrix feeMatrix : feeMatrices) {

			FeeMatrixContract contract = new FeeMatrixContract();
			model.map(feeMatrix, contract);
			feeMatrixContract.add(contract);
		}

		feeMatrixResponse.setFeeMatrices(feeMatrixContract);

		return feeMatrixResponse;
	}

	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public FeeMatrixSearchResponse getFeeMatrix(
			@Valid @ModelAttribute FeeMatrixSearchCriteriaContract feeMatrixSerachContract,
			@RequestBody final RequestInfoWrapper requestInfoWrapper, final BindingResult errors) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (errors.hasErrors()) {

			throw new CustomBindException(errors, requestInfo);
		}

		ModelMapper modelMapper = new ModelMapper();
		FeeMatrixSearchCriteria feeMatrixSearchCriteria = modelMapper.map(feeMatrixSerachContract,
				FeeMatrixSearchCriteria.class);
		List<FeeMatrixSearch> feeMatrixList = feeMatrixService.search(feeMatrixSearchCriteria, requestInfo);
		List<FeeMatrixSearchContract> feeMatrixSearchList = new ArrayList<FeeMatrixSearchContract>();
		for (FeeMatrixSearch feeMatrixSearch : feeMatrixList) {
			FeeMatrixSearchContract feeMatrixContract = modelMapper.map(feeMatrixSearch, FeeMatrixSearchContract.class);
			feeMatrixSearchList.add(feeMatrixContract);
		}

		ResponseInfo responseInfo = getResponseInfo(requestInfo);
		FeeMatrixSearchResponse feeMatrixSearchResponse = new FeeMatrixSearchResponse();
		feeMatrixSearchResponse.setFeeMatrices(feeMatrixSearchList);
		feeMatrixSearchResponse.setResponseInfo(responseInfo);
		return feeMatrixSearchResponse;
	}

	// get responseinfo from requestinfo
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
	}

	private AuditDetails setAuditDetails(AuditDetails auditDetails, RequestInfo requestInfo, Boolean create) {
		
		if(auditDetails == null){
			auditDetails = new AuditDetails();
		}
		
		if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {

			if (create == Boolean.TRUE) {
				auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.setCreatedTime(new Date().getTime());
			} else {
				auditDetails.setLastModifiedTime(new Date().getTime());
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}
		}

		return auditDetails;
	}
}