package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.AccountCodePurposeContract;import org.egov. egf.master.web.contract.AccountCodePurposeSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class AccountCodePurposeContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/accountcodepurposes/search?";
@Autowired
	private ObjectMapper objectMapper;public AccountCodePurposeContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<AccountCodePurposeContract> getAccountCodePurposeById(AccountCodePurposeSearchContract accountCodePurposeSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(accountCodePurposeSearchContract.getId()!=null)
        {
        	content.append("id="+accountCodePurposeSearchContract.getId());
        }
        
        if(accountCodePurposeSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+accountCodePurposeSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<AccountCodePurposeContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<AccountCodePurposeContract>>() {
				});

		return result;

	}
} 