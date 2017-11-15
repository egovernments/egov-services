/*package org.egov.common ;
import org.egov.inv.model.IndentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper; 
@Service 
public class IndentContractRepository { 
private RestTemplate restTemplate; 
private String hostUrl; 
public static final String SEARCH_URL = " /inventory-services/indents/search?";
@Autowired
private ObjectMapper objectMapper;
public IndentContractRepository(@Value("${inv.inv.host.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public Object findById(Object indentContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (indentContract.getId() != null) {
			content.append("id=" + indentContract.getId());
		}

		if (indentContract.getTenantId() != null) {
			content.append("&tenantId=" + indentContract.getTenantId());
		}
		url = url + content.toString();
		IndentResponse result = restTemplate.postForObject(url, null, IndentResponse.class);
			

		if (result.getIndents() != null && result.getIndents().size() == 1) {
			return result.getIndents().get(0);
		} else {
			return null;
		}

	}
} */