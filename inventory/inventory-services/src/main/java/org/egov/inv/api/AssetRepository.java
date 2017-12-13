package org.egov.inv.api;

import org.egov.common.RequestInfoWrapper;
import org.egov.inv.model.Asset;
import org.egov.inv.model.AssetResponse;
import org.egov.inv.model.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AssetRepository {

	private RestTemplate restTemplate;
	private String hostUrl;

	private String searchUrl;



	public AssetRepository(@Value("${egov.service.host.url}") String hostUrl, @Value("${egov.serices.asset.searh.url}") String searchUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
		this.searchUrl=searchUrl;
	}

	public Asset findByCode(Asset asset,RequestInfo info) {

		try {
			String url = String.format("%s%s", hostUrl, searchUrl);
			StringBuffer content = new StringBuffer();
			if (asset.getCode() != null) {
				content.append("code=" + asset.getCode());
			}

			if (asset.getTenantId() != null) {
				content.append("&tenantId=" + asset.getTenantId());
			}
			RequestInfoWrapper req=new RequestInfoWrapper();
			req.setRequestInfo(getRequestInfo(info));
			url = url + content.toString();
			AssetResponse result = restTemplate.postForObject(url, req, AssetResponse.class);

			if (result.getAssets() != null && result.getAssets().size() == 1) {
				return result.getAssets().get(0);
			} else {
				return null;
			}
		} catch (RestClientException e) {
			throw e;
		}

	}

	private org.egov.common.contract.request.RequestInfo getRequestInfo(RequestInfo requestInfo) {
		org.egov.common.contract.request.RequestInfo info=new org.egov.common.contract.request.RequestInfo();
		 return  info.builder().action(requestInfo.getAction())
		   .apiId(requestInfo.getApiId())
		   .authToken(requestInfo.getAuthToken())
		   .correlationId(requestInfo.getCorrelationId())
		   .did(requestInfo.getDid())
		   .key(requestInfo.getKey())
		   .msgId(requestInfo.getMsgId())
		   .ts(requestInfo.getTs())
		   //.userInfo(requestInfo.getUserInfo())
		   .ver(requestInfo.getVer()).build();
	}
	
	
}