package org.egov.asset.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.service.AssetCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assetCategories")
public class AssetCategoryController {
	private static final Logger logger = LoggerFactory.getLogger(AssetCategoryController.class);
	
	@Autowired
	private AssetCategoryService  assetCategoryService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	
	@PostMapping("/_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody RequestInfo requestInfo ,@ModelAttribute AssetCategoryCriteria assetCategoryCriteria,BindingResult bindingResult){
		
		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		List<AssetCategory> assetCategories=assetCategoryService.search(assetCategoryCriteria);
		AssetCategoryResponse response=new AssetCategoryResponse();
		response.setAssetCategory(assetCategories);
		
		return new ResponseEntity<AssetCategoryResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid AssetCategoryRequest assetCategoryRequest,BindingResult bindingResult){
		
		logger.info("AssetCategory create::"+assetCategoryRequest);
		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		Boolean isAsync=applicationProperties.getAssetCategoryAsync();
		AssetCategoryResponse response=null;
		
		if(isAsync) {
			response=assetCategoryService.createAsync(applicationProperties.getCreateAssetCategoryTopicName(),"save-aasetcategory",assetCategoryRequest);
		} else {
			response=assetCategoryService.create(assetCategoryRequest);
		}
		
		return new ResponseEntity<AssetCategoryResponse>(response, HttpStatus.CREATED);
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