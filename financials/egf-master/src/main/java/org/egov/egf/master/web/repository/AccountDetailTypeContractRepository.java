package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.AccountDetailTypeContract;import org.egov. egf.master.web.contract.AccountDetailTypeSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class AccountDetailTypeContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/accountdetailtypes/search?";
@Autowired
	private ObjectMapper objectMapper;public AccountDetailTypeContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<AccountDetailTypeContract> getAccountDetailTypeById(AccountDetailTypeSearchContract accountDetailTypeSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(accountDetailTypeSearchContract.getId()!=null)
        {
        	content.append("id="+accountDetailTypeSearchContract.getId());
        }
        
        if(accountDetailTypeSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+accountDetailTypeSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<AccountDetailTypeContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<AccountDetailTypeContract>>() {
				});

		return result;

	}
} 