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

package org.egov.collection.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.collection.domain.model.AuthenticatedUser;
import org.egov.collection.domain.model.ServiceCategorySearchCriteria;
import org.egov.collection.domain.service.ServiceCategoryService;
import org.egov.collection.web.contract.BusinessCategoryRequest;
import org.egov.collection.web.contract.BusinessCategoryResponse;
import org.egov.collection.web.contract.RequestInfo;
import org.egov.collection.web.contract.ResponseInfo;
import org.egov.collection.web.contract.SearchRequestInfo;
import org.egov.collection.web.contract.ServiceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/businessCategory" })
public class BusinessCategoryController {

	@Autowired
	private ServiceCategoryService serviceCategoryService;

	@PostMapping(value = "/_create")
	@ResponseStatus(HttpStatus.CREATED)
	public BusinessCategoryResponse createServiceCategory(
			@RequestBody BusinessCategoryRequest businessCategoryRequest) {
		AuthenticatedUser authenticatedUser = businessCategoryRequest.toAuthenticatedUser();
		org.egov.collection.persistence.entity.ServiceCategory serviceCategory = getServiceCategory(
				businessCategoryRequest, authenticatedUser);
		ServiceCategory newServiceCategory = new ServiceCategory(serviceCategoryService.create(serviceCategory));
		businessCategoryRequest.getRequestInfo().setAction("POST");
		ResponseInfo responseInfo = getResponseInfo(businessCategoryRequest);
		return new BusinessCategoryResponse(responseInfo, Collections.singletonList(newServiceCategory));
	}

	@PostMapping(value = "/{businessCategoryCode}/_update")
	@ResponseStatus(HttpStatus.OK)
	public BusinessCategoryResponse updateServiceCategory(@RequestBody BusinessCategoryRequest businessCategoryRequest,
			@PathVariable String businessCategoryCode) {
		org.egov.collection.persistence.entity.ServiceCategory serviceCategory = serviceCategoryService
				.findByCodeAndTenantId(businessCategoryCode,
						businessCategoryRequest.getBusinessCategoryInfo().getTenantId());
		AuthenticatedUser authenticatedUser = businessCategoryRequest.toAuthenticatedUser();
		org.egov.collection.persistence.entity.ServiceCategory serviceCategoryEntity = getUpdatedServiceCategory(
				businessCategoryRequest, serviceCategory, authenticatedUser);
		ServiceCategory newServiceCategory = new ServiceCategory(serviceCategoryService.update(serviceCategoryEntity));
		businessCategoryRequest.getRequestInfo().setAction("PUT");
		ResponseInfo responseInfo = getResponseInfo(businessCategoryRequest);
		return new BusinessCategoryResponse(responseInfo, Collections.singletonList(newServiceCategory));
	}

	@PostMapping(value = "/_search")
	@ResponseStatus(HttpStatus.OK)
	public BusinessCategoryResponse getAllBusinessCategories(@RequestBody final SearchRequestInfo requestInfo,
			@RequestParam final String tenantId, @RequestParam(required = false) final String businessCategoryName,
			@RequestParam(required = false) final Boolean isactive,
			@RequestParam(required = false) final List<Long> ids, @RequestParam(required = false) final String sort,
			@RequestParam(required = false) final String fields) {
		ServiceCategorySearchCriteria serviceCategorySearchCriteria = ServiceCategorySearchCriteria.builder()
				.businessCategoryName(businessCategoryName).tenantId(tenantId).isactive(isactive).ids(ids).build();
		List<ServiceCategory> contractServiceCategory = new ArrayList<>();
		if (fields != null) {
			String[] arrayFields = fields.split(",");
			List<String> listFields = Arrays.asList(arrayFields);
			List<org.egov.collection.domain.model.ServiceCategory> service = serviceCategoryService
					.findAll(serviceCategorySearchCriteria, sort);
			for (org.egov.collection.domain.model.ServiceCategory m : service) {
				contractServiceCategory.add(new ServiceCategory(m, listFields));
			}
			getResponseInfo(requestInfo);
			return new BusinessCategoryResponse(getResponseInfo(requestInfo), contractServiceCategory);
		}
		List<ServiceCategory> serviceCategories = serviceCategoryService.findAll(serviceCategorySearchCriteria, sort)
				.stream().map(ServiceCategory::new).collect(Collectors.toList());
		return new BusinessCategoryResponse(getResponseInfo(requestInfo), serviceCategories);
	}

	private ResponseInfo getResponseInfo(SearchRequestInfo requestInfo) {
		return ResponseInfo.builder().apiId(requestInfo.getRequestInfo().getApiId())
				.ver(requestInfo.getRequestInfo().getVer()).resMsgId("placeholder").status("true")
				.msgId(requestInfo.getRequestInfo().getMsgId()).ts(new Date().toString()).build();

	}

	private org.egov.collection.persistence.entity.ServiceCategory getServiceCategory(
			BusinessCategoryRequest businessCategoryRequest, AuthenticatedUser authenticatedUser) {
		org.egov.collection.persistence.entity.ServiceCategory serviceCategory = org.egov.collection.persistence.entity.ServiceCategory
				.builder().id(businessCategoryRequest.getBusinessCategoryInfo().getId())
				.name(businessCategoryRequest.getBusinessCategoryInfo().getName())
				.code(businessCategoryRequest.getBusinessCategoryInfo().getCode())
				.isactive(businessCategoryRequest.getBusinessCategoryInfo().getIsactive())
				.tenantId(businessCategoryRequest.getBusinessCategoryInfo().getTenantId()).build();
		serviceCategory.setCreatedBy(authenticatedUser.getId());
		serviceCategory.setCreatedDate(new Date());
		serviceCategory.setLastModifiedBy(authenticatedUser.getId());
		serviceCategory.setLastModifiedDate(new Date());
		return serviceCategory;

	}

	private org.egov.collection.persistence.entity.ServiceCategory getUpdatedServiceCategory(
			BusinessCategoryRequest businessCategoryRequest,
			org.egov.collection.persistence.entity.ServiceCategory category, AuthenticatedUser authenticatedUser) {
		category.setName(businessCategoryRequest.getBusinessCategoryInfo().getName());
		category.setCode(businessCategoryRequest.getBusinessCategoryInfo().getCode());
		category.setIsactive(businessCategoryRequest.getBusinessCategoryInfo().getIsactive());
		if (category.getCreatedBy() == null)
			category.setCreatedBy(authenticatedUser.getId());
		if (category.getCreatedDate() == null)
			category.setCreatedDate(new Date());
		category.setLastModifiedDate(new Date());
		category.setLastModifiedBy(authenticatedUser.getId());
		return category;
	}

	private ResponseInfo getResponseInfo(BusinessCategoryRequest businessCategoryRequest) {
		RequestInfo requestInfo = businessCategoryRequest.getRequestInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer()).resMsgId("placeholder")
				.status("true").msgId(requestInfo.getMsgId()).ts(new Date().toString()).build();
	}
}
