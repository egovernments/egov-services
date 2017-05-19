/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.domain.model.ServiceCategorySearchCriteria;
import org.egov.collection.persistence.entity.ServiceCategory;
import org.egov.collection.persistence.repository.ServiceCategoryRepository;
import org.egov.collection.persistence.specification.ServiceCategorySpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class ServiceCategoryService {
	@Autowired
	private final ServiceCategoryRepository serviceCategoryRepository;

	public ServiceCategoryService(final ServiceCategoryRepository serviceCategoryRepository) {
		this.serviceCategoryRepository = serviceCategoryRepository;
	}

	public org.egov.collection.domain.model.ServiceCategory create(final ServiceCategory serviceCategory) {

		return serviceCategoryRepository.create(serviceCategory);
	}

	public org.egov.collection.domain.model.ServiceCategory update(final ServiceCategory serviceCategory) {

		return serviceCategoryRepository.update(serviceCategory);
	}

	public ServiceCategory findByCodeAndTenantId(String businessCategoryCode, String tenantId) {
		return serviceCategoryRepository.findByCodeAndTenantId(businessCategoryCode, tenantId);
	}

	public List<org.egov.collection.domain.model.ServiceCategory> findAll(
			ServiceCategorySearchCriteria serviceCategorySearchCriteria, String sort) {
		ServiceCategorySpecification businessCategorySpecification = new ServiceCategorySpecification(
				serviceCategorySearchCriteria);
		if (sort != null) {
			String[] nameAndOrder = sort.split(",");
			List<String> listNameAndString = Arrays.asList(nameAndOrder);
			List<String> listContainsDesc = listNameAndString.stream().filter(descOrder -> descOrder.contains(":desc"))
					.collect(Collectors.toList());
			List<Order> allAscOrders = new ArrayList<>();
			List<Order> allDescOrders = new ArrayList<>();
			for (String sDesc : listContainsDesc) {
				String field = StringUtils.substringBefore(sDesc, ":");
				Order order = new Order(Direction.DESC, field);
				allDescOrders.add(order);
			}
			List<String> listContainsAsc = listNameAndString.stream().filter(descOrder -> !descOrder.contains(":desc"))
					.collect(Collectors.toList());
			for (String sAsc : listContainsAsc) {
				Order order = new Order(Direction.ASC, sAsc);
				allAscOrders.add(order);
			}
			allAscOrders.addAll(allDescOrders);
			Sort sortAsPerFieldProvided = new Sort(allAscOrders);
			return serviceCategoryRepository.findAll(businessCategorySpecification, sortAsPerFieldProvided).stream()
					.map(ServiceCategory::toDomain).collect(Collectors.toList());
		}
		Sort sortAsPerName = new Sort(Direction.ASC, "name");
		return serviceCategoryRepository.findAll(businessCategorySpecification, sortAsPerName).stream()
				.map(ServiceCategory::toDomain).collect(Collectors.toList());
	}
}