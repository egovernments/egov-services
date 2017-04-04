package org.egov.user.persistence.repository;

import org.egov.user.web.contract.ActionRequest;
import org.egov.user.web.contract.ActionResponse;
import org.egov.user.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ActionRestRepository{

	private RestTemplate restTemplate;
	private String url;

	public ActionRestRepository(final RestTemplate restTemplate,
			@Value("${egov.services.accesscontrol.host}") final String accessControlHost,
			@Value("${egov.services.accesscontrol.action_search}") final String url) {
		this.restTemplate = restTemplate;
		this.url = accessControlHost + url;
	}

	public ActionResponse getActionByRoleId(final List<Long> roleId) {
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setApiId("org.egov.pgr");
		requestInfo.setVer("1");
		requestInfo.setAction("POST");
		requestInfo.setTs("");
		requestInfo.setDid("");
		requestInfo.setKey("");
		requestInfo.setMsgId("");
		requestInfo.setRequesterId("");
		requestInfo.setAuthToken(null);
		ActionRequest actionRequest=ActionRequest.builder().requestInfo(requestInfo).roleIds(roleId).build();

		return restTemplate.postForObject(url,actionRequest,ActionResponse.class);
	}

}
