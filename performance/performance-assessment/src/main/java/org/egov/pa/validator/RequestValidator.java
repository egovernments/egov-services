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
import org.egov.pa.model.KpiTarget;
import org.egov.pa.model.KpiValue;
import org.egov.pa.model.KpiValueDetail;
import org.egov.pa.model.TargetType;
import org.egov.pa.service.impl.KpiMasterServiceImpl;
import org.egov.pa.service.impl.KpiTargetServiceImpl;
import org.egov.pa.service.impl.KpiValueServiceImpl;
import org.egov.pa.utils.PerformanceAssessmentConstants;
import org.egov.pa.web.contract.KPIRequest;
import org.egov.pa.web.contract.KPITargetRequest;
import org.egov.pa.web.contract.KPIValueRequest;
import org.egov.pa.web.contract.KPIValueSearchRequest;
import org.egov.pa.web.contract.ValueResponse;
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
	private KpiValueServiceImpl kpiValueService;
	
	@Autowired
	private KpiMasterServiceImpl kpiMasterService; 
	
	@Autowired
	private KpiTargetServiceImpl kpiTargetService; 
	
	private static final String DEFAULT_COUNT = "*"; 
	private static final String SEARCH_POSSIBLE = "YES"; 
	
	
	public ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();
        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        List<ErrorField> errFields = new ArrayList<>();
        if (errors.hasFieldErrors()) { 
            for (final FieldError fieldError : errors.getFieldErrors()) { 
            	ErrorField ef = new ErrorField();
            	ef.setField(fieldError.getField());
            	ef.setMessage("Value Entered : " + fieldError.getRejectedValue() + " is not a valid value. Please re-enter !");
            	errFields.add(ef);
            	error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
            	error.setMessage("Not matching the required expression :: "); 
            }
            error.setErrorFields(errFields);   
        }
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
	
	public List<ErrorResponse> validateRequest(final KPITargetRequest kpiTargetRequest, Boolean createOrUpdate) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(kpiTargetRequest,createOrUpdate);
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

			
			
			
			
			kpi.setPeriodicity(PerformanceAssessmentConstants.PERIODICITY_DEFAULT);
			if(StringUtils.isBlank(kpi.getName())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPINAME_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.KPINAME_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.KPINAME_MANDATORY_FIELD_NAME));
			}
			
			/*if(StringUtils.isBlank(kpi.getCode())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPICODE_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.KPICODE_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.KPICODE_MANDATORY_FIELD_NAME));
			}
			*/
			if(!createOrUpdate && errorFields.size() <= 0) {
				String targetType = kpiMasterService.targetAlreadyAvailable(kpi.getCode()); 
				if(StringUtils.isNotBlank(targetType) && !targetType.equals(kpi.getTargetType())) {  
					errorFields.add(buildErrorField(PerformanceAssessmentConstants.TARGET_EXISTS_CODE, 
		                    PerformanceAssessmentConstants.TARGET_EXISTS_ERROR_MESSAGE,
		                    PerformanceAssessmentConstants.TARGET_EXISTS_FIELD_NAME));
				}
				
			}
			
			if(StringUtils.isBlank(kpi.getFinancialYear())) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.FINYEAR_MANDATORY_CODE, 
	                    PerformanceAssessmentConstants.FINYEAR_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.FINYEAR_MANDATORY_FIELD_NAME));
			}
			
			if (null != kpi.getDepartmentId() && kpi.getDepartmentId() <= 0) {
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.DEPARTMENT_CODE_MANDATORY_CODE,
						PerformanceAssessmentConstants.DEPARTMENT_CODE_MANDATORY_ERROR_MESSAGE,
						PerformanceAssessmentConstants.DEPARTMENT_CODE_MANDATORY_FIELD_NAME));
			}
			
			/*if(kpiMasterService.checkNameOrCodeExists(kpiRequest, createOrUpdate)) {
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.NAMECODE_UNIQUE_CODE, 
	                    PerformanceAssessmentConstants.NAMECODE_UNIQUE_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.NAMECODE_UNIQUE_FIELD_NAME));
			}*/
			
			// Check whether the document details are available and validate them
			if(null != kpi.getDocuments() && kpi.getDocuments().size() > 0) { 
				List<Document> finalDocumentList = new ArrayList<>(); 
				for(Document doc : kpi.getDocuments()) { 
					if(doc.getActive() && StringUtils.isBlank(doc.getName())) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.DOCNAME_MANDATORY_CODE, 
			                    PerformanceAssessmentConstants.DOCNAME_MANDATORY_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.DOCNAME_MANDATORY_FIELD_NAME));
					}
					if(null == doc.getActive()) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.DOCACTIVE_MANDATORY_CODE, 
			                    PerformanceAssessmentConstants.DOCACTIVE_MANDATORY_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.DOCACTIVE_MANDATORY_FIELD_NAME));
					}
					if((doc.getActive() && StringUtils.isNotBlank(doc.getName())) || 
							(!doc.getActive() && StringUtils.isNotBlank(doc.getName()))) {
						finalDocumentList.add(doc); 
					}
				}
				kpi.setDocuments(finalDocumentList);
			}
			
		}
				return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PerformanceAssessmentConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();
	}
	
	public Error getError(final KPITargetRequest kpiTargetRequest, Boolean createOrUpdate) {
		final List<ErrorField> errorFields = new ArrayList<>();
		List<KpiTarget> targetList = kpiTargetRequest.getKpiTargets();
		
		if(!createOrUpdate && kpiTargetService.checkActualValuesForKpi(kpiTargetRequest)) { 
			errorFields.add(buildErrorField(PerformanceAssessmentConstants.TARGETUPDATE_INVALID_CODE,
					PerformanceAssessmentConstants.TARGETUPDATE_INVALID_ERROR_MESSAGE,
					PerformanceAssessmentConstants.TARGETUPDATE_INVALID_FIELD_NAME));
		}

		for (KpiTarget target : targetList) {

			if (StringUtils.isBlank(target.getFinYear())) {
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.TARGETFINYEAR_UNAVAILABLE_CODE,
						PerformanceAssessmentConstants.TARGETFINYEAR_UNAVAILABLE_ERROR_MESSAGE,
						PerformanceAssessmentConstants.TARGETFINYEAR_UNAVAILABLE_FIELD_NAME));
			}
			
			KPI kpi = target.getKpi(); 
			if( null != kpi && StringUtils.isNotBlank(kpi.getTargetType()) 
					&& (kpi.getTargetType().equals(TargetType.VALUE.toString()) || kpi.getTargetType().equals(TargetType.OBJECTIVE.toString()))
					&& StringUtils.isBlank(target.getTargetValue())) {
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.TARGETDESC_MANDATORY_CODE,
						PerformanceAssessmentConstants.TARGETDESC_MANDATORY_ERROR_MESSAGE,
						PerformanceAssessmentConstants.TARGETDESC_MANDATORY_FIELD_NAME));
			}
			
			if (StringUtils.isNotBlank(target.getTargetValue()) &&  
					target.getTargetValue().equals("0")) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.TARGETVALUE_INVALID_CODE,
						PerformanceAssessmentConstants.TARGETVALUE_INVALID_ERROR_MESSAGE,
						PerformanceAssessmentConstants.TARGETVALUE_INVALID_FIELD_NAME));
			}
			
			if (StringUtils.isNotBlank(target.getTargetDescription()) &&    
					target.getTargetDescription().equals("0")) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.TARGETVALUE_INVALID_CODE,
						PerformanceAssessmentConstants.TARGETVALUE_INVALID_ERROR_MESSAGE,
						PerformanceAssessmentConstants.TARGETVALUE_INVALID_FIELD_NAME));
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
		
		List<ValueResponse> valueResponseList = kpiValueRequest.getKpiValues();
		for(ValueResponse vr : valueResponseList) {
			KpiValue value = vr.getKpiValue();
			log.info("Value List : " + value.getValueList().toString());
			if(null == value.getValueList() || value.getValueList().size() <= 0) { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.VALUE_LIST_REQUIRED_CODE, 
	                    PerformanceAssessmentConstants.VALUE_LIST_REQUIRED_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.VALUE_LIST_REQUIRED_FIELD_NAME));
			}
			
			for(KpiValueDetail valueDetail : value.getValueList()) {
				if(StringUtils.isBlank(valueDetail.getValueid())) { 
					errorFields.add(buildErrorField(PerformanceAssessmentConstants.VALUE_DETAIL_INVALID_CODE, 
		                    PerformanceAssessmentConstants.VALUE_DETAIL_INVALID_ERROR_MESSAGE,
		                    PerformanceAssessmentConstants.VALUE_DETAIL_INVALID_FIELD_NAME));
				}
				
				/*if(null != vr.getKpi() && null != vr.getKpi().getTargetType() && vr.getKpi().getTargetType().equals(TargetType.OBJECTIVE.toString())) { 
					log.info("Validation for Objective Type KPI for No or Work In Progress Values");
					if(null != valueDetail.getValue() && (valueDetail.getValue().equals("2") || valueDetail.getValue().equals("3"))
							&& StringUtils.isBlank(valueDetail.getRemarks())) {
						String customErrorMessage = PerformanceAssessmentConstants.REMARKS_MANDATORY_ONCONDITION_ERROR_MESSAGE.concat(" Check Month "+ valueDetail.getPeriod()); 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.REMARKS_MANDATORY_ONCONDITION_CODE,
								customErrorMessage,
			                    PerformanceAssessmentConstants.REMARKS_MANDATORY_ONCONDITION_FIELD_NAME));
					}
				}*/
				
				if(StringUtils.isBlank(valueDetail.getPeriod())) { 
					errorFields.add(buildErrorField(PerformanceAssessmentConstants.VALUE_PERIOD_INVALID_CODE, 
		                    PerformanceAssessmentConstants.VALUE_PERIOD_INVALID_ERROR_MESSAGE,
		                    PerformanceAssessmentConstants.VALUE_PERIOD_INVALID_FIELD_NAME));
				}
			}
		}
		
		/*for(ValueResponse vr : kpiValueRequest.getKpiValues()) {
			KpiValue kpiValue= vr.getKpiValue(); 
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
					
					int numberOfDocsReq = kpiValueService.numberOfDocsRequired(eachValue.getKpi().getCode());
					if(null != eachValue.getDocuments() && eachValue.getDocuments().size() < numberOfDocsReq) { 
						errorFields.add(buildErrorField(PerformanceAssessmentConstants.MANDATORY_DOCS_REQUIRED_CODE, 
			                    PerformanceAssessmentConstants.MANDATORY_DOCS_REQUIRED_ERROR_MESSAGE,
			                    PerformanceAssessmentConstants.MANDATORY_DOCS_REQUIRED_FIELD_NAME));
					}
				}
				
			} else { 
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.KPIVALUES_MANDATORY_CODE,  
	                    PerformanceAssessmentConstants.KPIVALUES_MANDATORY_ERROR_MESSAGE,
	                    PerformanceAssessmentConstants.KPIVALUES_MANDATORY_FIELD_NAME));
			}
		}*/
		return Error.builder().code(HttpStatus.BAD_REQUEST.value())
				.message(PerformanceAssessmentConstants.INVALID_REQUEST_MESSAGE).errorFields(errorFields).build();
	}
	
	
	public List<ErrorResponse> validateRequest(final KPIValueSearchRequest kpiValueSearchRequest, Boolean compare) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(kpiValueSearchRequest, compare);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        log.info(errorResponses.size() + " Error Responses are found");
        return errorResponses;
    }
	
	public Error getError(final KPIValueSearchRequest kpiValueSearchRequest, Boolean compare) {
		final List<ErrorField> errorFields = new ArrayList<>();

		if (compare) {
			

			String ulbCount = DEFAULT_COUNT;
			String kpiCount = DEFAULT_COUNT;
			String finYearCount = DEFAULT_COUNT;
			if (null != kpiValueSearchRequest.getFinYear() && kpiValueSearchRequest.getFinYear().size() > 0) {
				finYearCount = (kpiValueSearchRequest.getFinYear().size() == 1
						&& !kpiValueSearchRequest.getFinYear().get(0).equals("ALL")) ? "1" : "*";
			}
			if (null != kpiValueSearchRequest.getUlbList() && kpiValueSearchRequest.getUlbList().size() > 0) {
				ulbCount = (kpiValueSearchRequest.getUlbList().size() == 1
						&& !kpiValueSearchRequest.getUlbList().get(0).equals("ALL")) ? "1" : "*";
			}
			if (null != kpiValueSearchRequest.getKpiCodes() && kpiValueSearchRequest.getKpiCodes().size() > 0) {
				kpiCount = (kpiValueSearchRequest.getKpiCodes().size() == 1
						&& !kpiValueSearchRequest.getKpiCodes().get(0).equals("ALL")) ? "1" : "*";
			}
			log.info("Search Possibility Check :: ULB Count : " + ulbCount + " KPI Count : " + kpiCount + " Fin Year Count : " + finYearCount);
			String check = kpiValueService.searchPossibilityCheck(ulbCount, kpiCount, finYearCount);
			log.info("Search Possibility says Search is : " + check);
			if (StringUtils.isNotBlank(check) && !check.split("_")[0].equals(SEARCH_POSSIBLE)) {
				errorFields.add(buildErrorField(PerformanceAssessmentConstants.SEARCH_PARAMETERS_INVALID_CODE,
						PerformanceAssessmentConstants.SEARCH_PARAMETERS_INVALID_ERROR_MESSAGE,
						PerformanceAssessmentConstants.SEARCH_PARAMETERS_INVALID_FIELD_NAME));
			} else if(StringUtils.isNotBlank(check)) {
				kpiValueSearchRequest.setGraphType(check.split("_")[1]);
			}
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
