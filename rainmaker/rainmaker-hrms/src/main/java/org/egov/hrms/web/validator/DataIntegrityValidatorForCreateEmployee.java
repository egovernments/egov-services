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

package org.egov.hrms.web.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.hrms.config.PropertiesManager;
import org.egov.hrms.model.Employee;
import org.egov.hrms.repository.EmployeeRepository;
import org.egov.hrms.service.HRMastersService;
import org.egov.hrms.web.contract.EmployeeRequest;
import org.egov.hrms.web.contract.RequestInfoWrapper;
import org.egov.hrms.web.errorhandler.ErrorHandler;
import org.egov.hrms.web.errorhandler.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataIntegrityValidatorForCreateEmployee extends EmployeeCommonValidator implements Validator {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private HRMastersService hrMastersService;

    @Autowired
    private ErrorHandler errorHandler;
    
    @Autowired
    private PropertiesManager propertiesManager;
    
    @Autowired
    private RestTemplate restTemplate;

    /**
     * This Validator validates *just* Employee instances
     */
    @Override
    public boolean supports(Class<?> paramClass) {
        return EmployeeRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object targetObject, Errors errors) {
        if (!(targetObject instanceof EmployeeRequest))
            return;

        EmployeeRequest employeeRequest = (EmployeeRequest) targetObject;
        Employee employee = employeeRequest.getEmployee();
        validateEmployee(employeeRequest, errors);
        validatePrimaryPositions(employee.getAssignments(), employee.getId(), employee.getTenantId(), errors, "create");
        validateStatusOfEmployee(employeeRequest, errors);
        validateTypeOfEmployee(employeeRequest, errors);
    }

    // FIXME Validate data existence of Religion, Languages etc. for every data in separate methods
    private void validateExternalAPIData() {
    }

    protected void validateEmployee(EmployeeRequest employeeRequest, Errors errors) {
        Employee employee = employeeRequest.getEmployee();
        RequestInfo requestInfo = employeeRequest.getRequestInfo();
        // FIXME : Setting ts as null in RequestInfo as hr is following common-contracts with ts as Date
        // & ID Generation Service is following ts as epoch
        requestInfo.setTs(null);
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);

        super.validateEmployee(employeeRequest, errors);
        InvalidDataException invalidDataException = new InvalidDataException();

        Map<String, List<String>> hrConfigurations = hrMastersService.getHRConfigurations(employee.getTenantId(), requestInfoWrapper);

        if (hrConfigurations.get("Autogenerate_employeecode").get(0).equalsIgnoreCase("N") && (employee.getCode() != null)
                && duplicateExists("egeis_employee", "code", employee.getCode(), employee.getTenantId())) {

            invalidDataException.setFieldName("employee.code");
            invalidDataException.setMessageKey("Employee Code Already Exists In System. Please Enter Different Employee Code.");
            invalidDataException.setFieldValue("invalid");
            errorHandler.getErrorInvalidData(invalidDataException, employeeRequest.getRequestInfo());
        }

        if ((!StringUtils.isEmpty(employee.getPassportNo())) && duplicateExists("egeis_employee", "passportNo",
                employee.getPassportNo(), employee.getTenantId())) {
            RequestInfo requestinfo = new RequestInfo();

            invalidDataException.setFieldName("employee.passportNo");
            invalidDataException.setMessageKey("Passport Number Already Exists In System. Please Enter Correct Passport Number.");
            invalidDataException.setFieldValue("invalid");
            errorHandler.getErrorInvalidData(invalidDataException, employeeRequest.getRequestInfo());
        }

        if ((!StringUtils.isEmpty(employee.getGpfNo())) && duplicateExists("egeis_employee", "gpfNo",
                employee.getGpfNo(), employee.getTenantId())) {

            invalidDataException.setFieldName("employee.gpfNo");
            invalidDataException.setMessageKey("GPF Number Already Exists In System. Please Enter Correct GPF Number.");
            invalidDataException.setFieldValue("invalid");
            errorHandler.getErrorInvalidData(invalidDataException, employeeRequest.getRequestInfo());
        }
        if (employee.getPassportNo() != null && employee.getPassportNo().equals(""))
            employee.setPassportNo(null);
        if (employee.getGpfNo() != null && employee.getGpfNo().equals(""))
            employee.setGpfNo(null);
    }
    
    private void validateStatusOfEmployee(EmployeeRequest employeeRequest, Errors errors) {
    	log.info("Validating employee status");
    	ObjectMapper mapper = new ObjectMapper();
    	Long employeeStatusId = employeeRequest.getEmployee().getEmployeeStatus();
    	StringBuilder uri = new StringBuilder();
    	RequestInfoWrapper request = new RequestInfoWrapper();
    	uri.append(propertiesManager.getHrMastersServiceHostName()).append(propertiesManager.getHrMastersServiceBasePath());
    	try {
    		uri.append(propertiesManager.getHrMastersEmployeeStatusSearchPath())
    		.append("?id=").append(employeeStatusId).append("&tenantId=").
    		append(employeeRequest.getEmployee().getTenantId());
    		Object response = restTemplate.postForObject(uri.toString(), request, Map.class);
    		if(null != response) {
    			List<Object> statuses = mapper.convertValue(JsonPath.read(response, "$.HRStatus"), List.class);
    			if(CollectionUtils.isEmpty(statuses)) {
    	            errors.rejectValue("employee.employeeStatus", "invalid", "The status entered for this employee is invalid");
    			}
    		}
    	}catch(Exception e) {
            errors.rejectValue("employee.employeeStatus", "invalid", "The status entered for this employee is invalid");
    	}
    	
    }
    
    private void validateTypeOfEmployee(EmployeeRequest employeeRequest, Errors errors) {
    	log.info("Validating employee type");
    	ObjectMapper mapper = new ObjectMapper();
    	Long employeeTypeId = employeeRequest.getEmployee().getEmployeeType();
    	StringBuilder uri = new StringBuilder();
    	RequestInfoWrapper request = new RequestInfoWrapper();
    	uri.append(propertiesManager.getHrMastersServiceHostName()).append(propertiesManager.getHrMastersServiceBasePath());
    	try {
    		uri.append(propertiesManager.getHrMastersEmployeeTypeSearchPath())
    		.append("?id=").append(employeeTypeId).append("&tenantId=").append(employeeRequest.getEmployee().getTenantId());
    		Object response = restTemplate.postForObject(uri.toString(), request, Map.class);
    		if(null != response) {
    			List<Object> types = mapper.convertValue(JsonPath.read(response, "$.EmployeeType"), List.class);
    			if(CollectionUtils.isEmpty(types)) {
    	            errors.rejectValue("employee.employeeType", "invalid", "The employee type entered for this employee is invalid");
    			}
    		}
    	}catch(Exception e) {
            errors.rejectValue("employee.employeeType", "invalid", "The employee type entered for this employee is invalid");
    	}
    	
    }

    /**
     * Checks if the given string is present in db for the given column and given table.
     */
    Boolean duplicateExists(String table, String column, String value, String tenantId) {
        return employeeRepository.duplicateExists(table, column, value, tenantId);
    }
}