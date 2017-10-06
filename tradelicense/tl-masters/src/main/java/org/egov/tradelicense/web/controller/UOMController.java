package org.egov.tradelicense.web.controller;

import javax.validation.Valid;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.UOMRequest;
import org.egov.tl.commons.web.response.UOMResponse;
import org.egov.tradelicense.domain.services.UOMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to trade license uom master
 * 
 * @author Pavan Kumar Kamma
 *
 */

@RestController
@RequestMapping(path = "/uom/v1")
public class UOMController {

	@Autowired
	UOMService uomService;

	/**
	 * Description : This api for creating UOM master
	 * @param UOMRequest
	 * @return UOMResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public UOMResponse createUomMaster(@Valid @RequestBody UOMRequest uomRequest) throws Exception {

		return uomService.createUomMaster(uomRequest);
	}

	/**
	 * Description : This api for updating UOM master
	 * 
	 * @param UOMRequest
	 * @return UOMResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public UOMResponse updateUomMaster(@Valid @RequestBody UOMRequest uomRequest) throws Exception {

		return uomService.updateUomMaster(uomRequest);
	}

	/**
	 * Description : This api for searching UOM master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param active
	 * @param pageSize
	 * @param offSet
	 * @return UOMResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public UOMResponse getUomMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String[] codes,
			@RequestParam(required = false) String active, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return uomService.getUomMaster(requestInfo.getRequestInfo(), tenantId, ids, name, codes, active, pageSize,
				offSet);
	}
}