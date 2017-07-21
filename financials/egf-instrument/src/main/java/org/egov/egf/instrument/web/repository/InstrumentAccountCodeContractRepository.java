package org.egov.egf.instrument.web.repository ;
import org.egov.common.web.contract.CommonResponse; 
import org.egov. egf.instrument.web.contract.InstrumentAccountCodeContract;
import org.egov. egf.instrument.web.contract.InstrumentAccountCodeSearchContract;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.web.client.RestTemplate; 
import org.springframework.stereotype.Service; 
import com.fasterxml.jackson.core.type.TypeReference; 
import com.fasterxml.jackson.databind.ObjectMapper; 
@Service 
public class InstrumentAccountCodeContractRepository { 
private RestTemplate restTemplate; 
private String hostUrl; 
public static final String SEARCH_URL = " /egf-instrument/instrumentaccountcodes/search?";
@Autowired
	private ObjectMapper objectMapper;
public InstrumentAccountCodeContractRepository(@Value("${egf.instrument.host.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public InstrumentAccountCodeContract findById(InstrumentAccountCodeContract instrumentAccountCodeContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (instrumentAccountCodeContract.getId() != null) {
			content.append("id=" + instrumentAccountCodeContract.getId());
		}

		if (instrumentAccountCodeContract.getTenantId() != null) {
			content.append("&tenantId=" + instrumentAccountCodeContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<InstrumentAccountCodeContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<InstrumentAccountCodeContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1)
			return result.getData().get(0);
		else
			return null;

	}
} 