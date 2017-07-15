package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.AccountEntityContract;import org.egov. egf.master.web.contract.AccountEntitySearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class AccountEntityContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/accountentities/search?";
@Autowired
	private ObjectMapper objectMapper;public AccountEntityContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<AccountEntityContract> getAccountEntityById(AccountEntitySearchContract accountEntitySearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(accountEntitySearchContract.getId()!=null)
        {
        	content.append("id="+accountEntitySearchContract.getId());
        }
        
        if(accountEntitySearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+accountEntitySearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<AccountEntityContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<AccountEntityContract>>() {
				});

		return result;

	}
} 