/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.asset.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.contract.ResponseInfo;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetController {

	private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

	@Autowired
	private AssetService assetService;
	
	@Autowired
	private ApplicationProperties applicationProperties;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper, @ModelAttribute AssetCriteria assetCriteria,BindingResult bindingResult) {
		logger.info("assetCriteria::"+assetCriteria);
		logger.info("requestInfoWrapper::"+requestInfoWrapper);
		
		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		List<Asset> assets = assetService.getAssets(assetCriteria);
		AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResponseInfo(new ResponseInfo());
		
		return new ResponseEntity<AssetResponse>(assetResponse, HttpStatus.OK);
	}

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid AssetRequest assetRequest,BindingResult bindingResult) {
		
		logger.info("create asset:"+ assetRequest);
		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		//TODO Input field validation, it will be a part of phash-2
		
		AssetResponse assetResponse=assetService.createAsync(applicationProperties.getCreateAssetTopicName(),"save-asset", assetRequest);
		return new ResponseEntity<AssetResponse>(assetResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("_update/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("code") String code, @RequestBody AssetRequest assetRequest, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		if(!code.equals(assetRequest.getAsset().getCode())){
			throw new RuntimeException("Invalid asset code");
		}
		//TODO Input field validation, it will be a part of phash-2
		
		AssetResponse assetResponse=assetService.updateAsync(applicationProperties.getUpdateAssetTopicName(),"update-asset",assetRequest);
		
		return new ResponseEntity<AssetResponse>(assetResponse, HttpStatus.OK);
	}
	
	
	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		//ResponseInfo responseInfo = new ResponseInfo();
		/*responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		responseInfo.setApi_id("");
		errRes.setResponseInfo(responseInfo);*/
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().put(errs.getField(), errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}
}