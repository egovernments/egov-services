package org.egov.pgr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.contract.ServiceResponse;
import org.egov.pgr.model.ActionInfo;
import org.egov.pgr.model.Service;
import org.egov.pgr.model.user.UserResponse;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@org.springframework.stereotype.Service
public class NotificationService {
	
	@Value("${egov.hr.employee.host}")
	private String hrEmployeeHost;
	
	@Value("${egov.hr.employee.v2.host}")
	private String hrEmployeeV2Host;

	@Value("${egov.hr.employee.v2.search.endpoint}")
	private String hrEmployeeV2SearchEndpoint;
	
	@Value("${egov.user.host}")
	private String egovUserHost;

	@Value("${egov.user.search.endpoint}")
	private String egovUserSearchEndpoint;
	
	@Value("${egov.default.sla.in.ms}")
	private Long egovDefaultServiceSla;
	
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;
	
	@Autowired
	private GrievanceService requestService;

	@Autowired
	private PGRUtils pGRUtils;
	
	public static Map<String, Map<String, String>> localizedMessageMap = new HashMap<>();

	public List<Object> getServiceType(Service serviceReq, RequestInfo requestInfo, String locale) {
		StringBuilder uri = new StringBuilder();
		List<Object> listOfValues = new ArrayList<>();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareSearchRequestForServiceType(uri, serviceReq.getTenantId(),
				serviceReq.getServiceCode(), requestInfo);
		String serviceType = null;
		List<String> serviceTypes = null;
		List<String> slaHours = null;
		String tenantId = serviceReq.getTenantId().split("[.]")[0]; // localization values are for now state-level.
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			serviceTypes = JsonPath.read(result, PGRConstants.JSONPATH_SERVICE_CODES);
			slaHours = JsonPath.read(result, PGRConstants.JSONPATH_SLA);
			if (CollectionUtils.isEmpty(serviceTypes) || CollectionUtils.isEmpty(slaHours))
				return null;
			if (null == localizedMessageMap.get(locale + "|" + tenantId)) // static map that saves code-message pair against locale | tenantId.
				getLocalisedMessages(requestInfo, tenantId, locale, PGRConstants.LOCALIZATION_MODULE_NAME);
			serviceType = localizedMessageMap.get(locale + "|" + tenantId).get(PGRConstants.LOCALIZATION_COMP_CATEGORY_PREFIX + serviceTypes.get(0)); //result set is always of size one.
			if(StringUtils.isEmpty(serviceType))
				serviceType = PGRUtils.splitCamelCase(serviceTypes.get(0));
		} catch (Exception e) {
			return null;
		}
		Long sla = Long.valueOf(slaHours.get(0)) / 24; //converting hours to days.
		listOfValues.add(serviceType); listOfValues.add(sla);
		return listOfValues;
	}

	public Map<String, String> getEmployeeDetails(String tenantId, String id, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(hrEmployeeV2Host).append(hrEmployeeV2SearchEndpoint).append("?id=" + id)
				.append("&tenantId=" + tenantId);
		Object response = null;
		Map<String, String> employeeDetails = new HashMap<>();
		try {
			response = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			if (null == response) {
				return employeeDetails;
			}
			employeeDetails.put("name", JsonPath.read(response, PGRConstants.EMPLOYEE_NAME_JSONPATH));
			employeeDetails.put("department", JsonPath.read(response, PGRConstants.EMPLOYEE_DEPTCODE_JSONPATH));
			employeeDetails.put("designation", JsonPath.read(response, PGRConstants.EMPLOYEE_DESGCODE_JSONPATH));
			employeeDetails.put("phone", JsonPath.read(response, PGRConstants.EMPLOYEE_PHNO_JSONPATH));

		} catch (Exception e) {
			log.error("Exception: " + e);
		}
		return employeeDetails;
	}

	public String getDepartment(Service serviceReq, String code, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareMdMsRequestForDept(uri, serviceReq.getTenantId(), code,
				requestInfo);
		List<String> departmemts = null;
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			departmemts = JsonPath.read(result, PGRConstants.JSONPATH_DEPARTMENTS);
			if (null == departmemts || departmemts.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		return departmemts.get(0);
	}

	public String getDesignation(Service serviceReq, String code, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareMdMsRequestForDesignation(uri, serviceReq.getTenantId(), code,
				requestInfo);
		List<String> designations = null;
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			designations = JsonPath.read(result, PGRConstants.JSONPATH_DESIGNATIONS);
			if (null == designations || designations.isEmpty())
				return null;
		} catch (Exception e) {
			return null;
		}

		return designations.get(0);
	}

	public void getLocalisedMessages(RequestInfo requestInfo, String tenantId, String locale, String module) {
		Map<String, String> mapOfCodesAndMessages = new HashMap<>();
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = pGRUtils.prepareRequestForLocalization(uri, requestInfo, locale,
				tenantId, module);
		List<String> codes = null;
		List<String> messages = null;
		Object result = null;
		try {
			result = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			codes = JsonPath.read(result, PGRConstants.LOCALIZATION_CODES_JSONPATH);
			messages = JsonPath.read(result, PGRConstants.LOCALIZATION_MSGS_JSONPATH);
		} catch (Exception e) {
			log.error("Exception while fetching from localization: " + e);
		}
		if (null != result) {
			for (int i = 0; i < codes.size(); i++) {
				mapOfCodesAndMessages.put(codes.get(i), messages.get(i));
			}
			localizedMessageMap.put(locale + "|" + tenantId, mapOfCodesAndMessages);
		}
	}
	
	public String getPhoneNumberForNotificationService(RequestInfo requestInfo, String userId, String tenantId, String assignee, String role) {
		String phoneNumber = null;
		Object response = null;
		ObjectMapper mapper = pGRUtils.getObjectMapper();
		StringBuilder uri = new StringBuilder();
		Object request = new HashMap<>();
		if(role.equals(PGRConstants.ROLE_CITIZEN)) {
			request = pGRUtils.prepareRequestForUserSearch(uri, requestInfo, userId, tenantId);
			try {
				response = serviceRequestRepository.fetchResult(uri, request);
				if(null != response) {
					UserResponse res = mapper.convertValue(response, UserResponse.class);
					phoneNumber = res.getUser().get(0).getMobileNumber();
				}
			}catch(Exception e) {
				log.error("Couldn't fetch user for id: "+userId+" error: " + e);
			}
			return phoneNumber;
		}else if(role.equals(PGRConstants.ROLE_EMPLOYEE)) {
			Map<String, String> employeeDetails = getEmployeeDetails(tenantId, assignee, requestInfo);
			if(!StringUtils.isEmpty(employeeDetails.get("phone"))) {
				phoneNumber = employeeDetails.get("phone");
			}
		}
		return phoneNumber;
	}
	
	public String getCurrentAssigneeForTheServiceRequest(Service serviceReq, RequestInfo requestInfo) {
		ServiceReqSearchCriteria serviceReqSearchCriteria = ServiceReqSearchCriteria.builder().tenantId(serviceReq.getTenantId())
				.serviceRequestId(Arrays.asList(serviceReq.getServiceRequestId())).build();
		ServiceResponse response = (ServiceResponse) requestService.getServiceRequestDetails(requestInfo, serviceReqSearchCriteria);
		List<ActionInfo> actions = response.getActionHistory().get(0).getActions().parallelStream()
				.filter(obj -> !StringUtils.isEmpty(obj.getAssignee())).collect(Collectors.toList());
		if(CollectionUtils.isEmpty(actions)) {
			return null;
		}
		return actions.get(0).getAssignee();
	}
	
/*	public Map<String, Long> getSlaHours(RequestInfo requestInfo, String tenantId) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq request = pGRUtils.prepareServiceDefSearchMdmsRequest(uri, tenantId, requestInfo);
		Map<String, Long> mapOfServiceCodesAndSLA = new HashMap<>();
		try {
			Object response = serviceRequestRepository.fetchResult(uri, request);
			if(null != response) {
				List<String> serviceCodes = JsonPath.read(response, "$.MdmsRes.RAINMAKER-PGR.ServiceDefs.*.serviceCode");
				List<Integer> slaHours = JsonPath.read(response, "$.MdmsRes.RAINMAKER-PGR.ServiceDefs.*.slaHours");
				for(int i = 0; i < serviceCodes.size(); i++) {
					Long epoch = pGRUtils.convertToMilliSec(slaHours.get(i));
					mapOfServiceCodesAndSLA.put(serviceCodes.get(i), epoch);
				}
			}
		}catch(Exception e) {
			log.error("Exception while fetching service codes and respective sla: ",e);
		}
		if(CollectionUtils.isEmpty(mapOfServiceCodesAndSLA.keySet())) {
			log.info("Returning default sla: "+egovDefaultServiceSla);
			mapOfServiceCodesAndSLA.put("default", egovDefaultServiceSla);
		}
		return mapOfServiceCodesAndSLA;
	}*/
	
	public Long getSlaHours() {
		log.info("Returning default sla: "+egovDefaultServiceSla);
		return egovDefaultServiceSla;
	}

}
