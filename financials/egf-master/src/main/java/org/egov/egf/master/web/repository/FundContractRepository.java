package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.FundContract;import org.egov. egf.master.web.contract.FundSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class FundContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/funds/search?";
@Autowired
	private ObjectMapper objectMapper;public FundContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<FundContract> getFundById(FundSearchContract fundSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(fundSearchContract.getId()!=null)
        {
        	content.append("id="+fundSearchContract.getId());
        }
        
        if(fundSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+fundSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<FundContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<FundContract>>() {
				});

		return result;

	}
} 