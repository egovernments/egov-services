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

package org.egov.collection.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.Department;
import org.egov.collection.model.DepartmentSearchCriteria;
import org.egov.collection.model.EmployeeInfo;
import org.egov.collection.model.PositionSearchCriteriaWrapper;
import org.egov.collection.model.UserSearchCriteriaWrapper;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.repository.WorkflowRepository;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;


@Service
public class WorkflowService {

	public static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);

	
	@Autowired
	private WorkflowRepository workflowRepository;
	
	@Autowired
	private CollectionProducer collectionProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	public List<Department> getDepartments(DepartmentSearchCriteria departmentSearchCriteria){
		logger.info("DepartmentSearchCriteria:"+departmentSearchCriteria.toString());
		List<Department> departments = new ArrayList<>();
		Object response = workflowRepository.getDepartments(departmentSearchCriteria);
		try{
			departments.addAll(JsonPath.read(response, "$.Department"));
		}catch(Exception e){
			logger.error("Zero departments returned from the service: "+e.getCause());
			departments = null;
			return departments;
		}
		logger.info("Departments received: "+departments.toString());

		return departments;
	}
	
/*	public List<Department> getDesignations(RequestInfo requestInfo){
		List<Department> departments = new ArrayList<>();
		Object response = workflowRepository.getDepartments(requestInfo);
		try{
			departments.addAll(JsonPath.read(response, "$.Department"));
		}catch(Exception e){
			logger.error("Zero departments returned from the service: "+e.getCause());
			departments = null;
			return departments;
		}
		logger.info("Departments received: "+departments.toString());

		return departments;
	} */

	public List<EmployeeInfo> getUsers(UserSearchCriteriaWrapper userSeachCriteriaWrapper){
		logger.info("UserSearchCriteria:"+userSeachCriteriaWrapper.toString());
		List<EmployeeInfo> users = new ArrayList<>();
		Object response = workflowRepository.getUsers(userSeachCriteriaWrapper);
		try{
			users.addAll(JsonPath.read(response, "$.Employee"));
		}catch(Exception e){
			logger.error("Zero users returned from the service: "+e.getCause());
			users = null;
			return users;
		}
		logger.info("Users received: "+users.toString());

		return users;
	}
	
	public String getPositionForUser(PositionSearchCriteriaWrapper positionSearchCriteriaWrapper){
		logger.info("PositionSearchCriteria:"+positionSearchCriteriaWrapper.toString());

		String position = null;
		Object response = workflowRepository.getPositionForUser(positionSearchCriteriaWrapper);
		try{
			position = JsonPath.read(response, "$.Position[0].name");
		}catch(Exception e){
			logger.error("No position returned from the service: "+e.getCause());
			position = null;
			return position;
		}
		logger.info("Position fetched is: "+position);
		return position;
	}
	
	public WorkflowDetails pushToQueue(WorkflowDetails workflowDetails) {		
		try{
			collectionProducer.producer(applicationProperties.getKafkaStartWorkflowTopic(),
					applicationProperties.getKafkaStartWorkflowTopicKey(), workflowDetails);
			
		}catch(Exception e){
			logger.error("Pushing to Queue FAILED! ", e.getMessage());
			return null;
		}
		return workflowDetails;
	}
	
	public ProcessInstanceResponse startWorkflow(WorkflowDetails workflowDetails){
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		try{
			processInstanceResponse = workflowRepository.startWorkflow(workflowDetails);
		}catch(Exception e){
			logger.error("ProcessInstance id couldn't be fetched from workflow svc", e.getCause());
		}
		if(null == processInstanceResponse){
			logger.error("Repository returned null processInstanceResponse");
			return processInstanceResponse;

		}
		logger.info("Proccess Instance Id received is: "+processInstanceResponse.getProcessInstance().getId());
		return processInstanceResponse;
	}
	
	
}

