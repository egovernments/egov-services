package org.egov.egf.instrument.web.repository ;
import org.egov.common.web.contract.CommonResponse; 
import org.egov. egf.instrument.web.contract.InstrumentContract;
import org.egov. egf.instrument.web.contract.InstrumentSearchContract;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.web.client.RestTemplate; 
import org.springframework.stereotype.Service; 
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper; 
@Service 
public class InstrumentContractRepository { 
private RestTemplate restTemplate; 
private String hostUrl; 
public static final String SEARCH_URL = " /egf-instrument/instruments/search?";
@Autowired
	private ObjectMapper objectMapper;
public InstrumentContractRepository(@Value("${egf.instrument.host.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public InstrumentContract findById(InstrumentContract instrumentContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (instrumentContract.getId() != null) {
			content.append("id=" + instrumentContract.getId());
		}

		if (instrumentContract.getTenantId() != null) {
			content.append("&tenantId=" + instrumentContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<InstrumentContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<InstrumentContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1)
			return result.getData().get(0);
		else
			return null;

	}
} 