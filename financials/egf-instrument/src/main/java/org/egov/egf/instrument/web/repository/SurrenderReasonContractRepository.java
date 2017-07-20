package org.egov.egf.instrument.web.repository ;
import org.egov.common.web.contract.CommonResponse; 
import org.egov. egf.instrument.web.contract.SurrenderReasonContract;
import org.egov. egf.instrument.web.contract.SurrenderReasonSearchContract;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.web.client.RestTemplate; 
import org.springframework.stereotype.Service; 
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper; 
@Service 
public class SurrenderReasonContractRepository { 
private RestTemplate restTemplate; 
private String hostUrl; 
public static final String SEARCH_URL = " /egf-instrument/surrenderreasons/search?";
@Autowired
	private ObjectMapper objectMapper;
public SurrenderReasonContractRepository(@Value("${egf.instrument.host.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public SurrenderReasonContract findById(SurrenderReasonContract surrenderReasonContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (surrenderReasonContract.getId() != null) {
			content.append("id=" + surrenderReasonContract.getId());
		}

		if (surrenderReasonContract.getTenantId() != null) {
			content.append("&tenantId=" + surrenderReasonContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<SurrenderReasonContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<SurrenderReasonContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1)
			return result.getData().get(0);
		else
			return null;

	}
} 