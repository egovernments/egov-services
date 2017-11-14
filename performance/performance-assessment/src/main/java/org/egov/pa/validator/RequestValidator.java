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
package org.egov.pa.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.response.ErrorField;
import org.egov.pa.model.Document;
import org.egov.pa.model.KPI;
import org.egov.pa.model.KpiValue;
import org.egov.pa.service.impl.KpiValueServiceImpl;
import org.egov.pa.utils.PerformanceAssessmentConstants;
import org.egov.pa.web.contract.KPIRequest;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.egov.pa.web.errorhandler.Error;
import org.egov.pa.web.errorhandler.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestValidator {
	
	@Autowired
	private RestCallService restCallService; 
	
	@Autowired 
	private KpiValueServiceImpl kpiValueService;
	
	private static final String DEFAULT_COUNT = "0"; 
	private static final String SEARCH_POSSIBLE = "YES"; 
	
	
	public ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();
        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError fieldError : errors.getFieldErrors())
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
        errRes.setError(error);
        return errRes;
    }
	
	public List<ErrorResponse> validateRequest(final KPIRequest kpiRequest, Boolean createOrUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(kpiRequest,createOrUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        log.info(errorResponses.size() + " Error Responses are found");
        return errorResponses;
    }
	
	public Error getError(final KPIRequest kpiRequest, Boolean createOrUpdate) {
		final List<ErrorField> errorFields = new ArrayList<>();
		List<KPI> kpis = kpiRequest.getKpIs();
		
		for(KPI kpi : kpis) { 
			if(StringUtils.isBlank(kpi.getName())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPINAME_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.KPINAME_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.KPINAME_MANDATORY_FIELD_NAME));
			}
			
			if(StringUtils.isBlank(kpi.getCode())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPICODE_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.KPICODE_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.KPICODE_MANDATORY_FIELD_NAME));
			}
			
			if(StringUtils.isBlank(kpi.getFinancialYear())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.FINYEAR_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.FINYEAR_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.FINYEAR_MANDATORY_FIELD_NAME));
			}
			
			if(null != kpi.getDepartmentId() && kpi.getDepartmentId() <= 0) { 
					errorFields.add(buildErrorField(PerformanceAssessmentConstants.DEPARTMENT_CODE_MANDATORY_CODE, 
		                    PerformanceAssessmentConstants.DEPARTMENT_CODE_MANDATORY_ERROR_MESSAGE,
		                    PerformanceAssessmentConstants.DEPARTMENT_CODE_MANDATORY_FIELD_NAME));
				}
			
			// Check whether the document details are available and validate them
			if(createOrUpdate && null != kpi.getDocuments() && kpi.getDocuments().size() > 0) { 
				for(Document doc : kpi.getDocuments()) { 
					if(StringUtils.isBlank(doc.getName())) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.DOCNAME_MANDATORY_CODE, 
			                    PerformanceAssessmentConstants.DOCNAME_MANDATORY_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.DOCNAME_MANDATORY_FIELD_NAME));
					}
					if(StringUtils.isBlank(doc.getCode())) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.DOCCODE_MANDATORY_CODE, 
			                    PerformanceAssessmentConstants.DOCCODE_MANDATORY_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.DOCCODE_MANDATORY_FIELD_NAME));
					}
					if(null == doc.getActive()) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.DOCACTIVE_MANDATORY_CODE, 
			                    PerformanceAssessmentConstants.DOCACTIVE_MANDATORY_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.DOCACTIVE_MANDATORY_FIELD_NAME));
					}
				}
			}
			
		}
				return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PerformanceAssessmentConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();
	}
	
	public List<ErrorResponse> validateRequest(final KPIValueRequest kpiValueRequest, Boolean createOrUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(kpiValueRequest,createOrUpdate);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        log.info(errorResponses.size() + " Error Responses are found");
        return errorResponses;
    }
	
	public Error getError(final KPIValueRequest kpiValueRequest, Boolean createOrUpdate) {
		final List<ErrorField> errorFields = new ArrayList<>();
		
		for(KpiValue kpiValue : kpiValueRequest.getKpiValues()) { 
			KPI kpi = kpiValue.getKpi();
			if(StringUtils.isBlank(kpi.getCode())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPICODE_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.KPICODE_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.KPICODE_MANDATORY_FIELD_NAME));
			}
			
			if(StringUtils.isBlank(kpiValue.getTenantId())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.TENANTID_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.TENANTID_MANADATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.TENANTID_MANADATORY_FIELD_NAME));
			}
			
			if(null == kpiValue.getResultValue()) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.ACTUALVALUE_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.ACTUALVALUE_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.ACTUALVALUE_MANDATORY_FIELD_NAME));
			}
			
			if(null != kpiValueRequest.getKpiValues()) { 
				for(int i=0 ; i < kpiValueRequest.getKpiValues().size() ; i++) {
					KpiValue eachValue = kpiValueRequest.getKpiValues().get(i); 
					if(!kpiValueService.checkKpiExists(eachValue.getKpi().getCode())) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPICODE_INVALID_CODE, 
			                    PerformanceAssessmentConstants.KPICODE_INVALID_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.KPICODE_INVALID_FIELD_NAME));
					}
					
					if(!restCallService.getULBNameFromTenant(eachValue.getTenantId())) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.TENANT_INVALID_CODE, 
			                    PerformanceAssessmentConstants.TENANT_INVALID_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.TENANT_INVALID_FIELD_NAME));
					}
					
					if(!kpiValueService.checkKpiTargetExists(eachValue.getKpi().getCode())) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.TARGET_UNAVAILABLE_CODE, 
			                    PerformanceAssessmentConstants.TARGET_UNAVAILABLE_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.TARGET_UNAVAILABLE_FIELD_NAME));
					}
					
					if(createOrUpdate && kpiValueService.checkKpiValueExistsForTenant(eachValue.getKpi().getCode(), eachValue.getTenantId())) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.CODE_TENANT_UNIQUE_CODE, 
			                    PerformanceAssessmentConstants.CODE_TENANT_UNIQUE_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.CODE_TENANT_UNIQUE_FIELD_NAME));
					}
				}
				
			} else { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPIVALUES_MANDATORY_CODE,  
	                    PerformanceAssessmentConstants.KPIVALUES_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.KPIVALUES_MANDATORY_FIELD_NAME));
			}
		}
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PerformanceAssessmentConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();
	}
	
	
	public List<ErrorResponse> validateRequest(final KPIValueSearchRequest kpiValueSearchRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(kpiValueSearchRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        log.info(errorResponses.size() + " Error Responses are found");
        return errorResponses;
    }
	
	public Error getError(final KPIValueSearchRequest kpiValueSearchRequest) {
		final List<ErrorField> errorFields = new ArrayList<>();
		
		if(null == kpiValueSearchRequest.getFinYear()) { 
			errorFields.add(buildErrorField(PerformanceAssessmentConstants.FINYEAR_SEARCH_MANDATORY_CODE,  
                    PerformanceAssessmentConstants.FINYEAR_SEARCH_MANDATORY_ERROR_MESSAGE,
                    PerformanceAssessmentConstants.FINYEAR_SEARCH_MANDATORY_FIELD_NAME));
		}
		if(null == kpiValueSearchRequest.getKpiCodes()) { 
			errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPICODE_SEARCH_MANDATORY_CODE,  
                    PerformanceAssessmentConstants.KPICODE_SEARCH_MANDATORY_ERROR_MESSAGE,
                    PerformanceAssessmentConstants.KPICODE_SEARCH_MANDATORY_FIELD_NAME));
		}
		if(null == kpiValueSearchRequest.getTenantId()) { 
			errorFields.add(buildErrorField(PerformanceAssessmentConstants.TENANTID_SEARCH_MANDATORY_CODE,  
                    PerformanceAssessmentConstants.TENANTID_SEARCH_MANDATORY_ERROR_MESSAGE,
                    PerformanceAssessmentConstants.TENANTID_SEARCH_MANDATORY_FIELD_NAME));
		}
		
		String tenantCount = DEFAULT_COUNT;
		String kpiCount = DEFAULT_COUNT;
		String finYearCount = DEFAULT_COUNT; 
		if(null != kpiValueSearchRequest.getFinYear() && kpiValueSearchRequest.getFinYear().size() > 0) { 
			finYearCount = (kpiValueSearchRequest.getFinYear().size() == 1 && !kpiValueSearchRequest.getFinYear().get(0).equals("ALL")) ? "1" : "*" ;  	
		}
		if(null != kpiValueSearchRequest.getTenantId() && kpiValueSearchRequest.getTenantId().size() > 0) { 
			tenantCount = (kpiValueSearchRequest.getTenantId().size() == 1 && !kpiValueSearchRequest.getTenantId().get(0).equals("ALL")) ? "1" : "*" ;  	
		}
		if(null != kpiValueSearchRequest.getKpiCodes() && kpiValueSearchRequest.getKpiCodes().size() > 0) { 
			kpiCount = (kpiValueSearchRequest.getKpiCodes().size() == 1 && !kpiValueSearchRequest.getKpiCodes().get(0).equals("ALL")) ? "1" : "*" ;  	
		}
		String check = kpiValueService.searchPossibilityCheck(tenantCount, kpiCount, finYearCount);
		if(!check.equals(SEARCH_POSSIBLE)) { 
			errorFields.add(buildErrorField(PerformanceAssessmentConstants.SEARCH_PARAMETERS_INVALID_CODE,  
                    PerformanceAssessmentConstants.SEARCH_PARAMETERS_INVALID_ERROR_MESSAGE,
                    PerformanceAssessmentConstants.SEARCH_PARAMETERS_INVALID_FIELD_NAME));
		}
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PerformanceAssessmentConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();
	}
	
	
	/**
     * This method returns ErrorField object for the Code, Message and Field Params
     * @param code
     * @param message
     * @param field
     * @return
     */
    private ErrorField buildErrorField(final String code, final String message, final String field) {
        return ErrorField.builder().code(code).message(message).field(field).build();

    }

}
