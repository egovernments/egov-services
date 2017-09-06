/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *      accountability and the service delivery of the government  organizations.
 *  
 *       Copyright (C) <2015>  eGovernments Foundation
 *  
 *       The updated version of eGov suite of products as by eGovernments Foundation
 *       is available at http://www.egovernments.org
 *  
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       any later version.
 *  
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *  
 *       You should have received a copy of the GNU General Public License
 *       along with this program. If not, see http://www.gnu.org/licenses/ or
 *       http://www.gnu.org/licenses/gpl.html .
 *  
 *       In addition to the terms of the GPL license to be adhered to in using this
 *       program, the following additional terms are to be complied with:
 *  
 *           1) All versions of this program, verbatim or modified must carry this
 *              Legal Notice.
 *  
 *           2) Any misrepresentation of the origin of the material is prohibited. It
 *              is required that all modified versions of this material be marked in
 *              reasonable ways as different from the original version.
 *  
 *           3) This license does not grant any rights to any user of the program
 *              with regards to rights under trademark law for use of the trade names
 *              or trademarks of eGovernments Foundation.
 *  
 *     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.tl.workflow.consumer;

import java.util.HashMap;
import java.util.List;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.workflow.repository.MessageQueueRepository;
import org.egov.tl.workflow.repository.contract.Department;
import org.egov.tl.workflow.repository.contract.Designation;
import org.egov.tl.workflow.repository.contract.Employee;
import org.egov.tl.workflow.service.DepartmentService;
import org.egov.tl.workflow.service.DesignationService;
import org.egov.tl.workflow.service.EmployeeService;
import org.egov.tl.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WorkflowListener {

	@Value("${egov.services.tl-services.tradelicense.workflow.populated.topic}")
	private String workflowEnrichedTopic;

	@Value("${egov.services.tl-services.tradelicense.workflow.populated.key}")
	private String workflowEnrichedKey;

	@Value("${default.citizen.workflow.initiator.department.name}")
	private String citizenWorkflowInitiatorDepartment;

	@Value("${default.citizen.workflow.initiator.designation.name}")
	private String citizenWorkflowInitiatorDesignation;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MessageQueueRepository messageQueueRepository;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DesignationService designationService;

	@KafkaListener(id = "${egov.services.tl-services.tradelicense.validated.id}", topics = "${egov.services.tl-services.tradelicense.validated.topic}", group = "${egov.services.tl-services.tradelicense.validated.group}")
	public void process(final HashMap<String, Object> tlRequestMap) {

		HashMap<String, Object> tlWorkflowEnrichedMap = new HashMap<>();
		TradeLicenseRequest request;
		
		System.out.println("tlRequestMap" + tlRequestMap );
		
		if (tlRequestMap.get("tradelicense-new-create") != null) {

			request = objectMapper.convertValue(tlRequestMap.get("tradelicense-new-create"), TradeLicenseRequest.class);

			for (final TradeLicenseContract tradeLicense : request.getLicenses()) {

				// Need to enable this if license is creating from citizen
				// service

				/*
				 * if (tradeLicense != null && tradeLicense.getWorkFlowDetails()
				 * != null && tradeLicense.getApplication().getWorkFlowDetails().getAssignee() ==
				 * null) populateAssigneeForCitizen(tradeLicense);
				 */

				workflowService.enrichWorkflow(tradeLicense.getApplication(), request.getRequestInfo());

			}

			tlWorkflowEnrichedMap.put("tradelicense-new-create", request);

		} else {

			request = objectMapper.convertValue(tlRequestMap.get("tradelicense-new-update"), TradeLicenseRequest.class);

			for (final TradeLicenseContract tradeLicense : request.getLicenses()) {

				workflowService.enrichWorkflow(tradeLicense.getApplication(), request.getRequestInfo());

			}

			tlWorkflowEnrichedMap.put("tradelicense-new-update", request);

		}

		messageQueueRepository.save(tlWorkflowEnrichedMap);

	}

	private void populateAssigneeForCitizen(TradeLicenseContract tradeLicense, RequestInfo requestInfo) {

		String departmentId = null, designationId = null;

		List<Department> departments = departmentService.getByName(citizenWorkflowInitiatorDepartment,
				tradeLicense.getTenantId(), requestInfo);

		List<Designation> designations = designationService.getByName(citizenWorkflowInitiatorDesignation,
				tradeLicense.getTenantId(), requestInfo);

		if (departments != null && !departments.isEmpty() && departments.get(0).getId() != null
				&& !departments.get(0).getId().isEmpty())
			departmentId = departments.get(0).getId();

		if (designations != null && !designations.isEmpty() && designations.get(0).getId() != null)
			designationId = designations.get(0).getId().toString();

		List<Employee> employees = employeeService.getByDeptIdAndDesgId(departmentId, designationId,
				tradeLicense.getTenantId(), requestInfo);

		if (employees != null && !employees.isEmpty() && employees.get(0).getId() != null
				&& employees.get(0).getAssignments() != null && !employees.get(0).getAssignments().isEmpty())
			tradeLicense.getApplication().getWorkFlowDetails().setAssignee(employees.get(0).getAssignments().get(0).getPosition());

	}

}
