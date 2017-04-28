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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.RequestInfoWrapper;
import org.egov.asset.exception.Error;
import org.egov.asset.exception.ErrorResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.Attributes;
import org.egov.asset.model.Department;
import org.egov.asset.model.Location;
import org.egov.asset.model.enums.ModeOfAcquisition;
import org.egov.asset.service.AssetService;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<?> search(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper, @ModelAttribute @Valid AssetCriteria assetCriteria,BindingResult bindingResult) {
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
		
		AssetResponse assetResponse=assetService.createAsync(assetRequest);
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
	
	@GetMapping("_get")
	@ResponseBody
	public ResponseEntity<?> get() {
		
		AssetRequest assetRequest = new AssetRequest();
		assetRequest.setRequestInfo(new RequestInfo());
		
		Asset asset = new Asset();
		asset.setAccumulatedDepreciation(10.5);
		asset.setAssetCategory(new AssetCategory());
		asset.setAssetDetails("asset details");
		asset.setAssetRefrance(5L);
		asset.setCode("asset code");
		asset.setDateOfCreation(new Date());
		asset.setDepartment(new Department());
		asset.setDescription("description");
		asset.setGrossValue(10.68);
		asset.setLocationDetails(new Location());
		asset.setModeOfAcquisition(ModeOfAcquisition.ACQUIRED);
		
		List<Attributes> attributesList = new ArrayList<Attributes>(); 
		
		
		
		Attributes attributes = new Attributes();
		attributes.setKey("field1");
		attributes.setType("string");
		attributes.setValue("field1 value");
		
		Attributes multivalue = new Attributes();
		multivalue.setKey("multivalue");
		attributes.setType("multivaluelist");
		List<String> list1=new ArrayList<String>();
		list1.add("value1");
		list1.add("value2");
		multivalue.setValue(list1);
		
		Attributes table1 = new Attributes();
		table1.setKey("table1");
		table1.setType("Table");
		Map<String, String> map =new HashMap<String,String>();
		map.put("col1", "val1");
		map.put("col2", "val2");
		map.put("col3", "val3");
		table1.setValue(map);
		
		Attributes table2 = new Attributes();
		table2.setKey("table2");
		table2.setType("Table");
		Map<String, String> rows1 =new HashMap<String,String>();
		rows1.put("col11", "val11");
		rows1.put("col22", "val22");
		rows1.put("col33", "val33");
		
		Map<String, String> rows2 =new HashMap<String,String>();
		rows2.put("col11", "val11");
		rows2.put("col22", "val22");
		rows2.put("col33", "val33");
		
		List<Object> list=new ArrayList<Object>();
		list.add(rows1);
		list.add(rows2);
		table2.setValue(list);
		
		attributesList.add(attributes);
		attributesList.add(multivalue);
		attributesList.add(table1);
		attributesList.add(table2);
		
		
		
		//
		/*List<Attributes> attributesList2 = new ArrayList<Attributes>();
		Attributes attributes2 = new Attributes();
		attributes2.setKey("table1");
		attributes2.setType("Table");
		
		
		Attributes colun1 = new Attributes();
		colun1.setKey("column1");
		colun1.setType("string");
		colun1.setValue("value1");
		
		Attributes colun2 = new Attributes();
		colun2.setKey("column2");
		colun2.setType("number");
		colun1.setValue("value2");
		
		attributesList2.add(colun1);
		attributesList2.add(colun2);
		attributes2.setColumnValues(attributesList2);
		//
		
		List<Attributes> attributesList3 = new ArrayList<Attributes>();
		Attributes attributes3 = new Attributes();
		attributes3.setKey("table2");
		attributes3.setType("Table");
		
		
		Attributes colun21 = new Attributes();
		colun21.setKey("colun21");
		colun21.setType("string");
		colun21.setValue("value21");
		
		Attributes colun22 = new Attributes();
		colun22.setKey("column22");
		colun22.setType("number");
		colun22.setValue("value22");
		
		attributesList3.add(colun21);
		attributesList3.add(colun22);
		attributes3.setColumnValues(attributesList3);
		
		attributesList.add(attributes);
		attributesList.add(attributes2);
		attributesList.add(attributes3);
		asset.setAssetAttributes(attributesList);*/
		
	/*	Map<String , Object> map= new HashMap<String, Object>();
		
		map.put("lebel1", "label1value");
		Map<String , String> column1= new HashMap<String, String>();
		column1.put("column1", "column value1");
		column1.put("column2", "column value1");
		map.put("table1", column1);
		
		Map<String , String> column2= new HashMap<String, String>();
		column2.put("column11", "column value11");
		column2.put("column22", "column value12");
		map.put("table2", column2);
		
		asset.setFields(map);
		*/
		
		asset.setAssetAttributes(attributesList);
		assetRequest.setAsset(asset);
		
		return new ResponseEntity<AssetRequest>(assetRequest, HttpStatus.OK);
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