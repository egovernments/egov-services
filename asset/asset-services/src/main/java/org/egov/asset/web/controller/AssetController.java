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

import javax.validation.Valid;

import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.contract.DisposalResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.service.AssetCurrentAmountService;
import org.egov.asset.service.AssetService;
import org.egov.asset.service.DisposalService;
import org.egov.asset.service.RevaluationService;
import org.egov.asset.web.validator.AssetValidator;
import org.egov.common.contract.response.ResponseInfo;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetController {

	private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

	@Autowired
	private AssetService assetService;

	@Autowired
	private AssetValidator assetValidator;

	@Autowired
	private RevaluationService revaluationService;

	@Autowired
	private AssetCurrentAmountService assetCurrentAmountService;

	@Autowired
	private DisposalService disposalService;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final AssetCriteria assetCriteria, final BindingResult bindingResult) {
		logger.info("assetCriteria::" + assetCriteria + "requestInfoWrapper::" + requestInfoWrapper);

		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		final AssetResponse assetResponse = assetService.getAssets(assetCriteria, requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(assetResponse, HttpStatus.OK);
	}

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final AssetRequest assetRequest,
			final BindingResult bindingResult) {

		logger.info("create asset:" + assetRequest);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		// TODO Input field validation, it will be a part of phase-2
		assetValidator.validateAsset(assetRequest);

		final AssetResponse assetResponse = assetService.createAsync(assetRequest);
		return new ResponseEntity<>(assetResponse, HttpStatus.CREATED);
	}

	@PostMapping("_update/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("code") final String code,
			@RequestBody final AssetRequest assetRequest, final BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		if (!code.equals(assetRequest.getAsset().getCode()))
			throw new RuntimeException("Invalid asset code");

		final AssetResponse assetResponse = assetService.updateAsync(assetRequest);

		return new ResponseEntity<>(assetResponse, HttpStatus.OK);
	}

	@PostMapping("reevaluate/_create")
	@ResponseBody
	public ResponseEntity<?> reevaluate(@RequestBody @Valid final RevaluationRequest revaluationRequest,
			final BindingResult bindingResult) {

		logger.info("create reevaluate:" + revaluationRequest);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		// TODO Input field validation, need to be done.

		final RevaluationResponse revaluationResponse = revaluationService.createAsync(revaluationRequest);

		return new ResponseEntity<>(revaluationResponse, HttpStatus.CREATED);
	}

	@PostMapping("reevaluate/_search")
	@ResponseBody
	public ResponseEntity<?> reevaluateSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute final RevaluationCriteria revaluationCriteria, final BindingResult bindingResult) {

		logger.info("reevaluateSearch revaluationCriteria:" + revaluationCriteria);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		// TODO Input field validation, need to be done.

		final RevaluationResponse revaluationResponse = revaluationService.search(revaluationCriteria);

		return new ResponseEntity<RevaluationResponse>(revaluationResponse, HttpStatus.OK);
	}

	@PostMapping("dispose/_create")
	@ResponseBody
	public ResponseEntity<?> dispose(@RequestBody @Valid final DisposalRequest disposalRequest,
			final BindingResult bindingResult) {

		logger.info("create dispose:" + disposalRequest);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		assetValidator.validateAssetForDispose(disposalRequest.getDisposal());

		final DisposalResponse disposalResponse = disposalService.createAsync(disposalRequest);
		logger.info("dispose disposalResponse:" + disposalResponse);

		return new ResponseEntity<DisposalResponse>(disposalResponse, HttpStatus.CREATED);
	}

	@PostMapping("dispose/_search")
	@ResponseBody
	public ResponseEntity<?> disposalSearch(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final DisposalCriteria disposalCriteria, final BindingResult bindingResult) {

		logger.info("disposalSearch disposalCriteria:" + disposalCriteria);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		// TODO Input field validation, need to be done.

		final DisposalResponse disposalResponse = disposalService.search(disposalCriteria,
				requestInfoWrapper.getRequestInfo());

		return new ResponseEntity<DisposalResponse>(disposalResponse, HttpStatus.OK);
	}

	@PostMapping("currentvalue/_search")
	@ResponseBody
	public ResponseEntity<?> getAssetCurrentValue(@RequestParam(name = "assetId", required = true) final Long assetId,
			@RequestParam(name = "tenantId", required = true) final String tenantId,
			@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper, final BindingResult bindingResult) {

		logger.info("getAssetCurrentValue assetId:" + assetId + ",tenantId:" + tenantId);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		final AssetCurrentValueResponse assetCurrentValueResponse = assetCurrentAmountService.getCurrentAmount(assetId,
				tenantId, requestInfoWrapper.getRequestInfo());

		logger.info("getAssetCurrentValue assetCurrentValueResponse:" + assetCurrentValueResponse);
		return new ResponseEntity<AssetCurrentValueResponse>(assetCurrentValueResponse, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(final BindingResult errors) {
		final ErrorResponse errRes = new ErrorResponse();

		final ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);

		final Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors())
			for (final FieldError errs : errors.getFieldErrors())
				error.getFields().put(errs.getField(), errs.getRejectedValue());
		errRes.setError(error);
		return errRes;
	}
}