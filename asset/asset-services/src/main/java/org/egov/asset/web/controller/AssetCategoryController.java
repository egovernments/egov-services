package org.egov.asset.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.AssetCategoryResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.model.AttributeDefination;
import org.egov.asset.model.enums.AssetCategoryType;
import org.egov.asset.model.enums.DepreciationMethod;
import org.egov.asset.service.AssetCategoryService;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<?> search(@RequestBody RequestInfoWrapper requestInfoWrapper ,@ModelAttribute AssetCategoryCriteria assetCategoryCriteria,BindingResult bindingResult){
		
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
			response=assetCategoryService.createAsync(assetCategoryRequest);
		} else {
			response=assetCategoryService.create(assetCategoryRequest);
		}
		
		return new ResponseEntity<AssetCategoryResponse>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/_get")
	@ResponseBody
	public ResponseEntity<?> get(){
		
		AssetCategoryRequest assetCategoryRequest = new AssetCategoryRequest();
		
		AssetCategory assetCategory = new AssetCategory();
		assetCategory.setAccumulatedDepreciationAccount(1L);
		assetCategory.setAssetAccount(2L);
		assetCategory.setAssetCategoryType(AssetCategoryType.IMMOVABLE);
		assetCategory.setCode("000001");
		assetCategory.setDepreciationExpenseAccount(3L);
		assetCategory.setDepreciationMethod(DepreciationMethod.STRAIGHT_LINE_METHOD);
		assetCategory.setName("asset name");
		assetCategory.setParent(2L);
		assetCategory.setRevaluationReserveAccount(5L);
		assetCategory.setTenantId("ap.kurnool");
		assetCategory.setUnitOfMeasurement(10L);
		
		AttributeDefination customFieldsDefination1 = new AttributeDefination();
		customFieldsDefination1.setName("Description");
		customFieldsDefination1.setType("SingleValueList");
		customFieldsDefination1.setValues("ABC,BCD");
		
		AttributeDefination customFieldsDefination3 = new AttributeDefination();
		customFieldsDefination3.setName("Details");
		customFieldsDefination3.setType("text");
		
		//table 
		
		AttributeDefination customFieldsDefination2 = new AttributeDefination();
		customFieldsDefination2.setName("Floor Details");
		customFieldsDefination2.setType("Table");

		AttributeDefination column1 = new AttributeDefination();
		column1.setName("column1");
		column1.setType("string");
		
		AttributeDefination column2 = new AttributeDefination();
		column2.setName("column2");
		column2.setType("number");
		
		
		
		AttributeDefination column3 = new AttributeDefination();
		column3.setName("column2");
		column3.setType("number");
		
		
		
		List<AttributeDefination>customFieldsDefinations=new ArrayList<AttributeDefination>();
		customFieldsDefinations.add(column1);
		customFieldsDefinations.add(column2);
		customFieldsDefinations.add(column3);
		
		customFieldsDefination2.setColumns(customFieldsDefinations);
		
		//customFieldsDefination2.setColumns(customFieldsDefinations);
		
		
		AttributeDefination customFieldsDefination4 = new AttributeDefination();
		customFieldsDefination4.setName("Amenities Details");
		customFieldsDefination4.setType("Table");

		AttributeDefination column11 = new AttributeDefination();
		column11.setName("column11");
		column11.setType("string");
		
		AttributeDefination column22 = new AttributeDefination();
		column22.setName("column22");
		column22.setType("number");
		
		//column11.setColumns(column22);
		
		AttributeDefination column33 = new AttributeDefination();
		column33.setName("column23");
		column33.setType("number");
		
		//column22.setColumns(column33);
		
		List<AttributeDefination>customFieldsDefinations2=new ArrayList<AttributeDefination>();
		customFieldsDefinations2.add(column11);
		customFieldsDefinations2.add(column22);
		customFieldsDefinations2.add(column33);
		
		customFieldsDefination4.setColumns(customFieldsDefinations2);
		
			
		//customFieldsDefination4.setColumns(customFieldsDefinations2);
		
		List<AttributeDefination> fieldsDefinations= new ArrayList<AttributeDefination>();
		fieldsDefinations.add(customFieldsDefination1);
		fieldsDefinations.add(customFieldsDefination2);
		fieldsDefinations.add(customFieldsDefination3);
		fieldsDefinations.add(customFieldsDefination4);
	
		assetCategory.setAssetFieldsDefination(fieldsDefinations);
		
		assetCategoryRequest.setAssetCategory(assetCategory);
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setUserInfo(new User());
		assetCategoryRequest.setRequestInfo(requestInfo);
		
		return new ResponseEntity<AssetCategoryRequest>(assetCategoryRequest, HttpStatus.OK);
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