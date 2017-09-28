package org.egov.egf.bill.web.repository;

import org.egov.egf.bill.web.contract.ChecklistContract;
import org.egov.egf.bill.web.requests.ChecklistResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChecklistContractRepository {
    private RestTemplate restTemplate;
    private String hostUrl;
    public static final String SEARCH_URL = "/egf-bill/checklists/_search?";

    public ChecklistContractRepository(@Value("${egf.bill.host.url}") String hostUrl, RestTemplate restTemplate) {
	this.restTemplate = restTemplate;
	this.hostUrl = hostUrl;
    }

    public ChecklistContract findById(ChecklistContract checklistContract) {
	String url = String.format("%s%s", hostUrl, SEARCH_URL);
	StringBuffer content = new StringBuffer();
	if (checklistContract.getId() != null) {
	    content.append("id=" + checklistContract.getId());
	}
	if (checklistContract.getTenantId() != null) {
	    content.append("&tenantId=" + checklistContract.getTenantId());
	}
	url = url + content.toString();
	ChecklistResponse result = restTemplate.postForObject(url, null, ChecklistResponse.class);
	if (result.getChecklists() != null && result.getChecklists().size() == 1) {
	    return result.getChecklists().get(0);
	} else {
	    return null;
	}
    }
}