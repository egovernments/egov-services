package org.egov.tradelicense.web.repository;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
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
		
		if(propertiesManger.getLocationBoundryHierarchy() != null && !propertiesManger.getLocationBoundryHierarchy().isEmpty()){
			
			content.append("&hierarchyType=" + propertiesManger.getLocationBoundryHierarchy());
			
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
		
		if (tenantId != null) {
			content.append("tenantId=" + tenantId);
		}
		
		if (codes != null && !codes.isEmpty()) {
			content.append("&codes=" + codes);
		}
		
		if(propertiesManger.getRevenueBoundryHierarchy() != null && !propertiesManger.getRevenueBoundryHierarchy().isEmpty()){
			
			content.append("&hierarchyType=" + propertiesManger.getRevenueBoundryHierarchy());
			
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
		
		if(propertiesManger.getAdminBoundryHierarchy() != null && !propertiesManger.getAdminBoundryHierarchy().isEmpty()){
			
			content.append("&hierarchyType=" + propertiesManger.getAdminBoundryHierarchy());
			
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

		if (tenantId != null) {
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
}