package org.egov.tradelicense.web.repository;

import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.domain.service.TLConfigurationService;
import org.egov.tradelicense.web.contract.TLConfigurationGetRequest;
import org.egov.tradelicense.web.response.BoundaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BoundaryContractRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManger;
	
	@Autowired
    private TLConfigurationService tlConfigurationService;

	public BoundaryContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public BoundaryResponse findByLocalityCodes(String tenantId, String codes, RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getLocationServiceHostName() + propertiesManger.getLocationServiceBasePath();
		String searchUrl = propertiesManger.getLocationServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("tenantId=" + tenantId);
		}
		
		if (codes != null && !codes.isEmpty()) {
			content.append("&codes=" + codes);
		}
		
		if(propertiesManger.getLocationBoundryHierarchyKey() != null && !propertiesManger.getLocationBoundryHierarchyKey().isEmpty()){
			
			String locationBoundaryHierarchyKey = propertiesManger.getLocationBoundryHierarchyKey();
			
			if(locationBoundaryHierarchyKey != null &&  tenantId != null){
				
				String locationBoundaryHierarchyValue = getBoundaryHierarchyValue(tenantId, locationBoundaryHierarchyKey);
				
				if(locationBoundaryHierarchyValue != null){
					
					content.append("&hierarchyType=" + locationBoundaryHierarchyValue);
				}
				
			}
			
		} else {
			
			content.append("&hierarchyType=LOCATION");	
		}
		
		content.append("&boundaryType=" + "Locality");
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		
		try {
			
			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			
			log.error(propertiesManger.getLocationEndPointError());
		}

		
		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			
			return boundaryResponse;
			
		} else {
			
			return null;
		}

	}

	public BoundaryResponse findByRevenueWardCodes(String tenantId, String codes, RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getLocationServiceHostName() + propertiesManger.getLocationServiceBasePath();
		String searchUrl = propertiesManger.getLocationServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("tenantId=" + tenantId);
		}
		
		if (codes != null && !codes.isEmpty()) {
			content.append("&codes=" + codes);
		}
		
		if(propertiesManger.getRevenueBoundryHierarchyKey() != null && !propertiesManger.getRevenueBoundryHierarchyKey().isEmpty()){
			
			String revenueBoundaryHierarchyKey = propertiesManger.getRevenueBoundryHierarchyKey();
			
			if(revenueBoundaryHierarchyKey != null &&  tenantId != null){
				
				String revenueBoundaryHierarchyValue = getBoundaryHierarchyValue(tenantId, revenueBoundaryHierarchyKey);
				
				if(revenueBoundaryHierarchyValue != null){
					
					content.append("&hierarchyType=" + revenueBoundaryHierarchyValue);
				}
				
			}
			
		} else {
			
			content.append("&hierarchyType=REVENUE");	
		}
		
		content.append("&boundaryType=Ward");
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		
		try {

			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			
			log.error(propertiesManger.getLocationEndPointError());
		}
		
		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			
			return boundaryResponse;
			
		} else {
			
			return null;
		}

	}

	public BoundaryResponse findByAdminWardCodes(String tenantId, String codes, RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getLocationServiceHostName() + propertiesManger.getLocationServiceBasePath();
		String searchUrl = propertiesManger.getLocationServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("tenantId=" + tenantId);
		}
		
		if (codes != null && !codes.isEmpty()) {
			
			content.append("&codes=" + codes);
		}
		
		if(propertiesManger.getAdminBoundryHierarchyKey() != null && !propertiesManger.getAdminBoundryHierarchyKey().isEmpty()){
			
			String adminBoundaryHierarchyKey = propertiesManger.getAdminBoundryHierarchyKey();
			
			if(adminBoundaryHierarchyKey != null &&  tenantId != null){
				
				String adminBoundaryHierarchyValue = getBoundaryHierarchyValue(tenantId, adminBoundaryHierarchyKey);
				
				if(adminBoundaryHierarchyValue != null){
					
					content.append("&hierarchyType=" + adminBoundaryHierarchyValue);
				}
				
			}
			
		} else {
			
			content.append("&hierarchyType=" + "ADMINISTRATION");	
		}
		
		content.append("&boundaryType=Ward");
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		
		try {

			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			
			log.error(propertiesManger.getLocationEndPointError());
		}

		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			
			return boundaryResponse;
			
		} else {
			
			return null;
		}

	}

	public BoundaryResponse findByBoundaryCodes(String tenantId, String codes, RequestInfoWrapper requestInfoWrapper) {
		
		String hostUrl = propertiesManger.getLocationServiceHostName() + propertiesManger.getLocationServiceBasePath();
		String searchUrl = propertiesManger.getLocationServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		
		if (codes != null && !codes.isEmpty()) {
			content.append("codes=" + codes);
		}

		if (tenantId != null && !tenantId.isEmpty()) {
			content.append("&tenantId=" + tenantId);
		}
		
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		
		try {

			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			
			log.error(propertiesManger.getLocationEndPointError());
		}

		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			
			return boundaryResponse;
			
		} else {
			
			return null;
		}
	}
	
	private String getBoundaryHierarchyValue(String tenantId, String boundaryHierarchyKey) {
		
		String boundaryHierarchyValue = null;
		TLConfigurationGetRequest tlConfigurationGetRequest = new TLConfigurationGetRequest();
		tlConfigurationGetRequest.setName(boundaryHierarchyKey);
		tlConfigurationGetRequest.setTenantId(tenantId);
		Map<String, List<String>> tlConfigurationKeyValuesList = tlConfigurationService
                .getTLConfigurations(tlConfigurationGetRequest);
		if(tlConfigurationKeyValuesList != null 
				&& tlConfigurationKeyValuesList.get(boundaryHierarchyKey) != null
				&& !tlConfigurationKeyValuesList.get(boundaryHierarchyKey).isEmpty()
				&& tlConfigurationKeyValuesList.get(boundaryHierarchyKey).size() > 0){
			
			boundaryHierarchyValue = tlConfigurationKeyValuesList.get(boundaryHierarchyKey).get(0);
		}
		
		return boundaryHierarchyValue;
	}
}