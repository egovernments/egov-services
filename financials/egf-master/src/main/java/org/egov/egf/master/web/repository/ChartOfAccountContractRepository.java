package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.ChartOfAccountContract;import org.egov. egf.master.web.contract.ChartOfAccountSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class ChartOfAccountContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/chartofaccounts/search?";
@Autowired
	private ObjectMapper objectMapper;public ChartOfAccountContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<ChartOfAccountContract> getChartOfAccountById(ChartOfAccountSearchContract chartOfAccountSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(chartOfAccountSearchContract.getId()!=null)
        {
        	content.append("id="+chartOfAccountSearchContract.getId());
        }
        
        if(chartOfAccountSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+chartOfAccountSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<ChartOfAccountContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<ChartOfAccountContract>>() {
				});

		return result;

	}
} 