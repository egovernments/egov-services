package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.FundsourceContract;import org.egov. egf.master.web.contract.FundsourceSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class FundsourceContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/fundsources/search?";
@Autowired
	private ObjectMapper objectMapper;public FundsourceContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<FundsourceContract> getFundsourceById(FundsourceSearchContract fundsourceSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(fundsourceSearchContract.getId()!=null)
        {
        	content.append("id="+fundsourceSearchContract.getId());
        }
        
        if(fundsourceSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+fundsourceSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<FundsourceContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<FundsourceContract>>() {
				});

		return result;

	}
} 