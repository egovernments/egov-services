package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.EgfStatusContract;import org.egov. egf.master.web.contract.EgfStatusSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class EgfStatusContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/egfstatuses/search?";
@Autowired
	private ObjectMapper objectMapper;public EgfStatusContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<EgfStatusContract> getEgfStatusById(EgfStatusSearchContract egfStatusSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(egfStatusSearchContract.getId()!=null)
        {
        	content.append("id="+egfStatusSearchContract.getId());
        }
        
        if(egfStatusSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+egfStatusSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<EgfStatusContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<EgfStatusContract>>() {
				});

		return result;

	}
} 