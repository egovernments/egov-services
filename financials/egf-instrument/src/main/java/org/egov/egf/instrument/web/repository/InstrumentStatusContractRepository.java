package org.egov.egf.instrument.web.repository ;
import org.egov.common.web.contract.CommonResponse; 
import org.egov. egf.instrument.web.contract.InstrumentStatusContract;
import org.egov. egf.instrument.web.contract.InstrumentStatusSearchContract;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.web.client.RestTemplate; 
import org.springframework.stereotype.Service; 
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper; 
@Service 
public class InstrumentStatusContractRepository { 
private RestTemplate restTemplate; 
private String hostUrl; 
public static final String SEARCH_URL = " /egf-instrument/instrumentstatuses/search?";
@Autowired
	private ObjectMapper objectMapper;
public InstrumentStatusContractRepository(@Value("${egf.instrument.host.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public InstrumentStatusContract findById(InstrumentStatusContract instrumentStatusContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (instrumentStatusContract.getId() != null) {
			content.append("id=" + instrumentStatusContract.getId());
		}

		if (instrumentStatusContract.getTenantId() != null) {
			content.append("&tenantId=" + instrumentStatusContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<InstrumentStatusContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<InstrumentStatusContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1)
			return result.getData().get(0);
		else
			return null;

	}
} 