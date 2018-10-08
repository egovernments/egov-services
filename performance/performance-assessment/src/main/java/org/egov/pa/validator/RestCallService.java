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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.pa.config.ApplicationProperties;
import org.egov.pa.model.Department;
import org.egov.pa.model.KpiCategory;
import org.egov.pa.model.Tenant;
import org.egov.pa.web.contract.MDMSResponse;
import org.egov.pa.web.contract.RequestInfoBody;
import org.egov.pa.web.contract.TenantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestCallService {
	
	public static Map<String, Tenant> tenantMap = new HashMap<>();
	
	@Autowired
	private ApplicationProperties applicationProperties; 
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	public boolean getULBNameFromTenant(final String tenantId) {
		if(tenantMap.containsKey(tenantId)) { 
			return true;
		}
        final StringBuilder url = new StringBuilder(
        		applicationProperties.getTenantServiceHostName() + applicationProperties.getTenantServiceSearchPath());
        url.append("?code=" + tenantId);
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
        final RequestInfoBody requestInfoBody = new RequestInfoBody(requestInfo);
        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
        log.info("URL to invoke Tenant Service : " + url.toString());
        log.info("Request Info to invoke the URL : " + request);
        final TenantResponse tr = restTemplate.postForObject(url.toString(), request, TenantResponse.class);
        if (null != tr) {
            log.info("Tenant Response : " + tr);
            if (null != tr.getTenant() && tr.getTenant().size() > 0) {
            	for(Tenant tenant : tr.getTenant()) 
            		tenantMap.put(tenant.getCode(), tenant);
            	return true;
            }
            else 
            	return false; 
        }
        return false; 
    }
	
	public List<Department> getDepartmentForId(final String tenantId) {
		final StringBuilder url = new StringBuilder(
				applicationProperties.getMdmsServiceHostName() + applicationProperties.getMdmsServiceSearchPath()
						+ applicationProperties.getMdmsServiceSearchGetDepartmentUrl());
		if(StringUtils.isNotBlank(tenantId))
			url.append("&tenantId=" + tenantId); 
		final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
		final RequestInfoBody requestInfoBody = new RequestInfoBody(requestInfo);
		final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
		log.info("URL to invoke MDMS Service : " + url.toString());
		log.info("Request Info to invoke the URL : " + request);
		MDMSResponse dr = restTemplate.postForObject(url.toString(), request, MDMSResponse.class);
		log.info("Response from MDMS : " + dr);
		if (null != dr && null != dr.getMdmsRes() && null != dr.getMdmsRes().getCommonMasters()
				&& null != dr.getMdmsRes().getCommonMasters().getDepartments()) {
			return dr.getMdmsRes().getCommonMasters().getDepartments();
		}
		return null;
	}
	
	public Map<String, KpiCategory> getCategory(final String tenantId) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, KpiCategory> kpiCategoryMap = new HashMap<>();
		final StringBuilder url = new StringBuilder(
				applicationProperties.getMdmsServiceHostName() + applicationProperties.getMdmsServiceSearchPath()
						+ applicationProperties.getMdmsServiceSearchGetCategoryUrl());
		if(StringUtils.isNotBlank(tenantId))
			url.append("&tenantId=" + tenantId.split("\\.")[0]);
		final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
		final RequestInfoBody requestInfoBody = new RequestInfoBody(requestInfo);
		log.info("URL to invoke MDMS Service : " + url.toString());
		log.info("Request Info to invoke the URL : " + requestInfoBody);
		Map<String, Object> response = restTemplate.postForObject(url.toString(), requestInfoBody, Map.class);
		log.info("Response from MDMS : " + response);
		MDMSResponse dr = mapper.convertValue(response, MDMSResponse.class);
		if (null != dr && null != dr.getMdmsRes() && null != dr.getMdmsRes().getKpiCategoryList()
				&& null != dr.getMdmsRes().getKpiCategoryList().getCategoryList()) {
			for(KpiCategory category : dr.getMdmsRes().getKpiCategoryList().getCategoryList()) { 
				kpiCategoryMap.put(category.getCode(), category); 
			}
			return kpiCategoryMap;
		}
		return null;
	}
	
	public List<Tenant> getAllTenants() {
		final StringBuilder url = new StringBuilder(
				applicationProperties.getMdmsServiceHostName() + applicationProperties.getMdmsServiceSearchPath()
						+ applicationProperties.getMdmsServiceSearchTenantUrl());
		final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
		final RequestInfoBody requestInfoBody = new RequestInfoBody(requestInfo);
		final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
		log.info("URL to invoke MDMS Service : " + url.toString());
		log.info("Request Info to invoke the URL : " + request);
		MDMSResponse dr = restTemplate.postForObject(url.toString(), request, MDMSResponse.class);
		log.info("Response from MDMS : " + dr);
		if (null != dr && null != dr.getMdmsRes() && null != dr.getMdmsRes().getTenantList()
				&& null != dr.getMdmsRes().getTenantList().getTenantList()) {
			return dr.getMdmsRes().getTenantList().getTenantList();
		}
		return null;
	}
}