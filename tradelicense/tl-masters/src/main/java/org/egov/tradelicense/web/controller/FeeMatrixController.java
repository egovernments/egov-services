package org.egov.tradelicense.web.controller;

import javax.validation.Valid;

import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.FeeMatrixResponse;
import org.egov.tradelicense.domain.services.FeeMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	/**
	 * Description : This api for creating feeMatrix master
	 * 
	 * @param tenantId
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public FeeMatrixResponse createFeeMatrixMaster(@Valid @RequestBody FeeMatrixRequest feeMatrixRequest)
			throws Exception {

		return feeMatrixService.createFeeMatrixMaster(feeMatrixRequest);
	}

	/**
	 * Description : This api for updating feeMatrix master
	 * 
	 * @param FeeMatrixRequest
	 * @return FeeMatrixResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public FeeMatrixResponse updateFeeMatrixMaster(@Valid @RequestBody FeeMatrixRequest feeMatrixRequest)
			throws Exception {

		return feeMatrixService.updateFeeMatrixMaster(feeMatrixRequest);
	}

	/**
	 * Description : This api for searching feeMatrix master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param categoryId
	 * @param subcategoryId
	 * @param financialYear
	 * @param applicationType
	 * @param businessNature
	 * @param pageSize
	 * @param offSet
	 * @return FeeMatrixResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public FeeMatrixResponse getFeeMatrixMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) Integer categoryId, @RequestParam(required = false) Integer subcategoryId,
			@RequestParam(required = false) String financialYear,
			@RequestParam(required = false) String applicationType,
			@RequestParam(required = false) String businessNature,
			@RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return feeMatrixService.getFeeMatrixMaster(requestInfo.getRequestInfo(),
				tenantId, ids,
				categoryId,
				subcategoryId,
				financialYear,
				applicationType,
				businessNature,
				pageSize,
				offSet);
	}
}