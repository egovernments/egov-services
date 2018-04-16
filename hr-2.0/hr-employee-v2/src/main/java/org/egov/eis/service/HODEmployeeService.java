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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.EmployeeDocument;
import org.egov.eis.model.EmployeeInfo;
import org.egov.eis.model.User;
import org.egov.eis.repository.HODEmployeeRepository;
import org.egov.eis.service.helper.EmployeeHelper;
import org.egov.eis.service.helper.EmployeeUserMapper;
import org.egov.eis.web.contract.EmployeeCriteria;
import org.egov.eis.web.contract.HODEmployeeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HODEmployeeService {

	public static final Logger LOGGER = LoggerFactory.getLogger(HODEmployeeService.class);

	@Autowired
	private HODEmployeeRepository hodEmployeeRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeHelper employeeHelper;

	@Autowired
	private EmployeeUserMapper employeeUserMapper;

	@Autowired
	PropertiesManager propertiesManager;

	@SuppressWarnings("unchecked")
	public List<EmployeeInfo> getHODEmployees(HODEmployeeCriteria hodEmployeeCriteria, RequestInfo requestInfo) {
		List<Long> listOfIds = hodEmployeeRepository.findIdsForCriteria(hodEmployeeCriteria);

		if (listOfIds.isEmpty())
			return Collections.EMPTY_LIST;

		List<EmployeeInfo> employeeInfoList = hodEmployeeRepository.findForCriteria(hodEmployeeCriteria, listOfIds);

		if (employeeInfoList.isEmpty())
			return employeeInfoList;

		List<Long> ids = employeeInfoList.stream().map(employeeInfo -> employeeInfo.getId())
				.collect(Collectors.toList());

		EmployeeCriteria employeeCriteria = EmployeeCriteria.builder()
				.id(ids).tenantId(hodEmployeeCriteria.getTenantId()).build();
		List<User> usersList = userService.getUsers(employeeCriteria, requestInfo);
		LOGGER.debug("userService: " + usersList);
		employeeUserMapper.mapUsersWithEmployees(employeeInfoList, usersList);

		if (!ids.isEmpty()) {
			List<EmployeeDocument> employeeDocuments = hodEmployeeRepository.getDocumentsForListOfHODEmployeeIds(ids,
					hodEmployeeCriteria.getTenantId());
			employeeHelper.mapDocumentsWithEmployees(employeeInfoList, employeeDocuments);
		}

		return employeeInfoList;
	}
}