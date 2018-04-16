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

package org.egov.eis.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.model.bulk.Department;
import org.egov.eis.repository.MdmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DepartmentService {

	@Autowired
	private MdmsRepository mdmsRepository;

	@Autowired
	private ObjectMapper mapper;

	public Department getDepartment(final String tenantId, final String code, final RequestInfo requestInfo) {

		JSONArray responseJSONArray;

		responseJSONArray = mdmsRepository.getByCriteria(tenantId, "common-masters", "Department", "code", code,
				requestInfo);

		if (responseJSONArray != null && responseJSONArray.size() > 0)
			return mapper.convertValue(responseJSONArray.get(0), Department.class);
		else {
			log.error("Given Department is invalid: " + code);
			return null;
		}

	}

	public List<Department> getAll(final String tenantId, final RequestInfo requestInfo) {

		final List<Department> departments = new ArrayList<>();

		JSONArray responseJSONArray;

		responseJSONArray = mdmsRepository.getByCriteria(tenantId, "common-masters", "Department", null, null,
				requestInfo);

		if (responseJSONArray != null && responseJSONArray.size() > 0)
			for (final Object obj : responseJSONArray)
				departments.add(mapper.convertValue(obj, Department.class));

		return departments;

	}

}