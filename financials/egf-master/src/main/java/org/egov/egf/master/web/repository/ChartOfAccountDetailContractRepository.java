package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.ChartOfAccountDetailContract;import org.egov. egf.master.web.contract.ChartOfAccountDetailSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class ChartOfAccountDetailContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/chartofaccountdetails/search?";
@Autowired
	private ObjectMapper objectMapper;public ChartOfAccountDetailContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<ChartOfAccountDetailContract> getChartOfAccountDetailById(ChartOfAccountDetailSearchContract chartOfAccountDetailSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(chartOfAccountDetailSearchContract.getId()!=null)
        {
        	content.append("id="+chartOfAccountDetailSearchContract.getId());
        }
        
        if(chartOfAccountDetailSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+chartOfAccountDetailSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<ChartOfAccountDetailContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<ChartOfAccountDetailContract>>() {
				});

		return result;

	}
} 