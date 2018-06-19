package org.egov.pgr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.pgr.contract.RequestInfoWrapper;
import org.egov.pgr.model.Service;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.PGRConstants;
import org.egov.pgr.utils.PGRUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
	
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private PGRUtils pGRUtils;
	
	public static Map<String, Map<String, String>> localizedMessageMap = new HashMap<>();

	public String getServiceType(Service serviceReq, RequestInfo requestInfo, String locale) {
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq mdmsCriteriaReq = pGRUtils.prepareSearchRequestForServiceType(uri, serviceReq.getTenantId(),
				serviceReq.getServiceCode(), requestInfo);
		String serviceType = null;
		List<String> serviceTypes = null;
		String tenantId = serviceReq.getTenantId().split("[.]")[0]; // localization values are for now state-level.
		try {
			Object result = serviceRequestRepository.fetchResult(uri, mdmsCriteriaReq);
			serviceTypes = JsonPath.read(result, PGRConstants.JSONPATH_SERVICE_CODES);
			log.info("Category type code: " + serviceTypes);
			if (CollectionUtils.isEmpty(serviceTypes))
				return null;
			if (null == localizedMessageMap.get(locale + "|" + tenantId)) // static map that saves code-message pair against locale | tenantId.
				getLocalisedMessages(requestInfo, tenantId, locale, PGRConstants.LOCALIZATION_MODULE_NAME);
			serviceType = localizedMessageMap.get(locale + "|" + tenantId).get(PGRConstants.LOCALIZATION_COMP_CATEGORY_PREFIX + serviceTypes.get(0));
			log.info("Category type: " + serviceType);
			if(StringUtils.isEmpty(serviceType))
				serviceType = serviceTypes.get(0);
		} catch (Exception e) {
			return null;
		}

		return serviceType;
	}

	public Map<String, String> getEmployeeDetails(String tenantId, String id, RequestInfo requestInfo) {
		StringBuilder uri = new StringBuilder();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		uri.append(hrEmployeeV2Host).append(hrEmployeeV2SearchEndpoint).append("?id=" + id)
				.append("&tenantId=" + tenantId);
		Object response = null;
		log.debug("Employee: " + response);
		Map<String, String> employeeDetails = new HashMap<>();
		try {
			response = serviceRequestRepository.fetchResult(uri, requestInfoWrapper);
			if (null == response) {
				return null;
			}
			log.debug("Employee: " + response);
			employeeDetails.put("name", JsonPath.read(response, PGRConstants.EMPLOYEE_NAME_JSONPATH));
			employeeDetails.put("department", JsonPath.read(response, PGRConstants.EMPLOYEE_DEPTCODE_JSONPATH));
			employeeDetails.put("designation", JsonPath.read(response, PGRConstants.EMPLOYEE_DESGCODE_JSONPATH));

		} catch (Exception e) {
			log.error("Exception: " + e);
			return null;
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
			log.info("Dept result: " + result);
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
			log.info("Designation result: " + result);
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
			log.info("localization result: " + result);
			codes = JsonPath.read(result, "$.messages.*.code");
			messages = JsonPath.read(result, "$.messages.*.message");
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
	
	public String getPhoneNumberForNotificationService(RequestInfo requestInfo, String userId, String tenantId) {
		Object phoneNumber = null;
		Object response = null;
		StringBuilder uri = new StringBuilder();
		Map<String, Object> request = pGRUtils.prepareRequestForUserSearch(uri, requestInfo, userId, tenantId);
		try {
			response = serviceRequestRepository.fetchResult(uri, request);
			if(null != response) {
				phoneNumber = JsonPath.read(response, "$.user.0.mobileNumber");
			}
		}catch(Exception e) {
			log.error("Couldn't fetch user for id: "+userId+" error: " + e);
		}
		return phoneNumber.toString();
	}

}
