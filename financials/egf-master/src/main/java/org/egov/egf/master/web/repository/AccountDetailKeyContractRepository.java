package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.AccountDetailKeyContract;import org.egov. egf.master.web.contract.AccountDetailKeySearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class AccountDetailKeyContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/accountdetailkeys/search?";
@Autowired
	private ObjectMapper objectMapper;public AccountDetailKeyContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<AccountDetailKeyContract> getAccountDetailKeyById(AccountDetailKeySearchContract accountDetailKeySearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(accountDetailKeySearchContract.getId()!=null)
        {
        	content.append("id="+accountDetailKeySearchContract.getId());
        }
        
        if(accountDetailKeySearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+accountDetailKeySearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<AccountDetailKeyContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<AccountDetailKeyContract>>() {
				});

		return result;

	}
} 