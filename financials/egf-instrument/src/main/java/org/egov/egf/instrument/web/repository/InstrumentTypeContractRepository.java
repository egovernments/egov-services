package org.egov.egf.instrument.web.repository ;
import org.egov.common.web.contract.CommonResponse; 
import org.egov. egf.instrument.web.contract.InstrumentTypeContract;
import org.egov. egf.instrument.web.contract.InstrumentTypeSearchContract;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.web.client.RestTemplate; 
import org.springframework.stereotype.Service; 
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper; 
@Service 
public class InstrumentTypeContractRepository { 
private RestTemplate restTemplate; 
private String hostUrl; 
public static final String SEARCH_URL = " /egf-instrument/instrumenttypes/search?";
@Autowired
	private ObjectMapper objectMapper;
public InstrumentTypeContractRepository(@Value("${egf.instrument.host.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public InstrumentTypeContract findById(InstrumentTypeContract instrumentTypeContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (instrumentTypeContract.getId() != null) {
			content.append("id=" + instrumentTypeContract.getId());
		}

		if (instrumentTypeContract.getTenantId() != null) {
			content.append("&tenantId=" + instrumentTypeContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<InstrumentTypeContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<InstrumentTypeContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1)
			return result.getData().get(0);
		else
			return null;

	}
} 