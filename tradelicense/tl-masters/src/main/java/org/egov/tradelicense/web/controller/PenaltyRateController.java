package org.egov.tradelicense.web.controller;

import javax.validation.Valid;

import org.egov.tl.commons.web.requests.PenaltyRateRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.PenaltyRateResponse;
import org.egov.tradelicense.domain.services.PenaltyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to trade license penaltyrate
 * master
 * 
 * @author Pavan Kumar Kamma
 *
 */
@RestController
@RequestMapping(path = "/penaltyrate/v1")
public class PenaltyRateController {

	@Autowired
	PenaltyRateService penaltyRateService;

	/**
	 * Description : This api for creating penaltyRate master
	 * 
	 * @param tenantId
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public PenaltyRateResponse createPenaltyRateMaster(@RequestParam(required = true) String tenantId,
			@Valid @RequestBody PenaltyRateRequest penaltyRateRequest) throws Exception {

		return penaltyRateService.createPenaltyRateMaster(tenantId, penaltyRateRequest);
	}

	/**
	 * Description : This api for updating penaltyRate master
	 * 
	 * @param PenaltyRateRequest
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public PenaltyRateResponse updatePenaltyRateMaster(@Valid @RequestBody PenaltyRateRequest penaltyRateRequest)
			throws Exception {

		return penaltyRateService.updatePenaltyRateMaster(penaltyRateRequest);
	}

	/**
	 * Description : This api for searching penaltyRate master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return PenaltyRateResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public PenaltyRateResponse getPenaltyRateMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String applicationType, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return penaltyRateService.getPenaltyRateMaster(requestInfo.getRequestInfo(), tenantId, ids, applicationType,
				pageSize, offSet);
	}
}