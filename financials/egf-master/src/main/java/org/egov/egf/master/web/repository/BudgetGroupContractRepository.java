package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.BudgetGroupContract;import org.egov. egf.master.web.contract.BudgetGroupSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class BudgetGroupContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/budgetgroups/search?";
@Autowired
	private ObjectMapper objectMapper;public BudgetGroupContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<BudgetGroupContract> getBudgetGroupById(BudgetGroupSearchContract budgetGroupSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(budgetGroupSearchContract.getId()!=null)
        {
        	content.append("id="+budgetGroupSearchContract.getId());
        }
        
        if(budgetGroupSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+budgetGroupSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<BudgetGroupContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<BudgetGroupContract>>() {
				});

		return result;

	}
} 