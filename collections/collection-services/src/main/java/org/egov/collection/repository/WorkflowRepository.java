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

package org.egov.collection.repository;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.DepartmentSearchCriteria;
import org.egov.collection.model.DesignationSearchCriteria;
import org.egov.collection.model.PositionSearchCriteriaWrapper;
import org.egov.collection.model.UserSearchCriteria;
import org.egov.collection.model.UserSearchCriteriaWrapper;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.producer.CollectionProducer;
import org.egov.collection.web.contract.ProcessInstance;
import org.egov.collection.web.contract.ProcessInstanceRequest;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.egov.collection.web.contract.factory.RequestInfoWrapper;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;



@Repository
public class WorkflowRepository {
	public static final Logger logger = LoggerFactory
			.getLogger(ReceiptRepository.class);
	
	@Autowired
	private CollectionProducer collectionProducer;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	public Object getDepartments(DepartmentSearchCriteria departmentSearchCriteria){
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.GET_DEPT_URI;
		String searchCriteria="?tenantId="+departmentSearchCriteria.getTenantId();
		
		builder.append(baseUri).append(searchCriteria);
		
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(departmentSearchCriteria.getRequestInfo());

		Object response = null;
		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper, Object.class);
		}catch(Exception e){
			logger.error("Couldn't fetch departments. Exception!"+e.getCause());
		}
		return response;
	}
	
	public Object getDesignations(DesignationSearchCriteria designationSearchCriteria){
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.GET_DESIG_URI;
		String searchCriteria="?tenantId=default";
		String searchOnDept="&departmentId="+designationSearchCriteria.getDepartmentId(); // FIXME: hr module is yet to implement this
		
		builder.append(baseUri).append(searchCriteria);
		
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(designationSearchCriteria.getRequestInfo());

		Object response = null;
		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper, Object.class);
		}catch(Exception e){
			logger.error("Couldn't fetch departments. Exception!"+e.getCause());
		}
		return response;
	}
	
	public Object getUsers(UserSearchCriteriaWrapper userSeachCriteriaWrapper){
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.GET_USERS_URI;
		UserSearchCriteria userSearchCriteria = new UserSearchCriteria();
		userSearchCriteria = userSeachCriteriaWrapper.getUserSearchCriteria();
		String searchCriteria="?tenantId="+userSearchCriteria.getTenantId();
		String searchOnDept="&departmentId="+userSearchCriteria.getDepartmentId();
		String searchOnDesig="&designationId="+userSearchCriteria.getDesignationId();
		
		builder.append(baseUri).append(searchCriteria);
		if(null != userSearchCriteria.getDepartmentId() && null != userSearchCriteria.getDesignationId()){
			builder.append(searchOnDept).append(searchOnDesig);
		}else{
			if(null != userSearchCriteria.getDepartmentId())
				builder.append(searchOnDept);
			else if(null != userSearchCriteria.getDesignationId())
				builder.append(searchOnDesig);
		}
				
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(userSeachCriteriaWrapper.getRequestInfo());
		logger.info("URI: "+builder.toString());
		Object response = null;
		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper, Object.class);
		}catch(Exception e){
			logger.error("Couldn't fetch departments. Exception!"+e.getCause());
		}
		return response;
	}
	
	public Object getPositionForUser(PositionSearchCriteriaWrapper positionSearchCriteriaWrapper){
		StringBuilder builder = new StringBuilder();
		String baseUri = CollectionServiceConstants.GET_POSITION_URI;
		String uriAppend = CollectionServiceConstants.GET_POSITION_URI_APPEND;
		String searchCriteria="?tenantId="+positionSearchCriteriaWrapper.getPositionSearchCriteria().getTenantId();
		
		builder.append(baseUri)
			   .append(positionSearchCriteriaWrapper.getPositionSearchCriteria().getEmployeeId())
			   .append(uriAppend)
			   .append(searchCriteria);
				
				
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(positionSearchCriteriaWrapper.getRequestInfo());
		
		logger.info("URI: "+builder.toString());
		
		Object response = null;
		try{
			response = restTemplate.postForObject(builder.toString(), requestInfoWrapper, Object.class);
		}catch(Exception e){
			logger.error("Couldn't fetch departments. Exception!"+e.getCause());
		}
		
		return response;

	}
	
	public ProcessInstanceResponse startWorkflow(WorkflowDetails workflowDetails){
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		StringBuilder uri = new StringBuilder();
		String basePath = applicationProperties.getWorkflowServiceHostName();
		String searchPath = applicationProperties.getWorkflowServiceSearchPath();
		uri.append(basePath).append(searchPath);
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		processInstanceRequest = getProcessInstanceRequest(workflowDetails);
		
		try{
            processInstanceResponse = restTemplate.postForObject(uri.toString(), processInstanceRequest,
                    ProcessInstanceResponse.class);
		}catch(Exception e){
			logger.error("Exception caused while hitting the workflow service: ", e.getCause());
			processInstanceResponse = null;
			return processInstanceResponse;
		}
		
		logger.info("ProcessInstanceResponse: "+processInstanceResponse);
		return processInstanceResponse;
	}
	
    private ProcessInstanceRequest getProcessInstanceRequest(final WorkflowDetails workflowDetails) {

        final RequestInfo requestInfo = workflowDetails.getRequestInfo();
        final ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        final ProcessInstance processInstance = new ProcessInstance();

     //   assignee.setId(workFlowDetails.getAssignee());
        processInstance.setBusinessKey(applicationProperties.getWorkflowServiceBusinessKey());
        processInstance.setType(applicationProperties.getWorkflowServiceBusinessKey());
    //    processInstance.setAssignee(assignee);
        processInstance.setComments(workflowDetails.getComments());
        processInstance.setInitiatorPosition(workflowDetails.getInitiatorPosition());
        processInstance.setTenantId(workflowDetails.getTenantId());
        processInstance.setDetails("Receipt Create : " + workflowDetails.getReceiptNumber());
        processInstanceRequest.setProcessInstance(processInstance);
        processInstanceRequest.setRequestInfo(requestInfo);

        return processInstanceRequest;
    }

}
