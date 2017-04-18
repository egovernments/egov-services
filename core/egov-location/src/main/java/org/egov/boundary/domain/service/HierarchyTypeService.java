package org.egov.boundary.domain.service;

import java.util.ArrayList;

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

import java.util.List;

import org.egov.boundary.persistence.entity.HierarchyType;
import org.egov.boundary.persistence.repository.HierarchyTypeRepository;
import org.egov.boundary.web.contract.HierarchyTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Service for the HierarchyType
 *
 * @author nayeem
 */
@Service
@Transactional(readOnly = true)
public class HierarchyTypeService {

	private HierarchyTypeRepository hierarchyTypeRepository;

	@Autowired
	public HierarchyTypeService(HierarchyTypeRepository hierarchyTypeRepository) {
		this.hierarchyTypeRepository = hierarchyTypeRepository;
	}

	@Transactional
	public HierarchyType createHierarchyType(HierarchyType hierarchyType) {
		return hierarchyTypeRepository.save(hierarchyType);
	}

	@Transactional
	public HierarchyType updateHierarchyType(HierarchyType hierarchyType) {
		return hierarchyTypeRepository.save(hierarchyType);
	}

	public HierarchyType getHierarchyTypeByNameAndTenantId(String name, String tenantId) {
		return hierarchyTypeRepository.findByNameAndTenantId(name, tenantId);
	}

	@Transactional
	public HierarchyType create(HierarchyType hierarchyType) {
		return hierarchyTypeRepository.save(hierarchyType);

	}

	public HierarchyType findByCodeAndTenantId(String code, String tenantId) {
		return hierarchyTypeRepository.findByCodeAndTenantId(code, tenantId);

	}

	public HierarchyType findByIdAndTenantId(Long id, String tenantId) {
		return hierarchyTypeRepository.findByIdAndTenantId(id, tenantId);

	}

	public List<HierarchyType> getAllHierarchyTypes(HierarchyTypeRequest hierarchyTypeRequest) {
		List<HierarchyType> hierarchyTypes = new ArrayList<HierarchyType>();
		if (hierarchyTypeRequest.getHierarchyType() != null
				&& hierarchyTypeRequest.getHierarchyType().getTenantId() != null
				&& !hierarchyTypeRequest.getHierarchyType().getTenantId().isEmpty()) {
			if (hierarchyTypeRequest.getHierarchyType().getId() != null) {
				hierarchyTypes.add(hierarchyTypeRepository.findByIdAndTenantId(hierarchyTypeRequest.getHierarchyType().getId(),hierarchyTypeRequest.getHierarchyType().getTenantId()));

			} else {
				if (!StringUtils.isEmpty(hierarchyTypeRequest.getHierarchyType().getCode())) {
					hierarchyTypes.add(findByCodeAndTenantId(hierarchyTypeRequest.getHierarchyType().getCode(),hierarchyTypeRequest.getHierarchyType().getTenantId()));

				} else {
					hierarchyTypes.addAll(hierarchyTypeRepository.findAllByTenantId(hierarchyTypeRequest.getHierarchyType().getTenantId()));
				}
			}
		}
		return hierarchyTypes;

	}
}
