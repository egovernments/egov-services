package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.FunctionContract;import org.egov. egf.master.web.contract.FunctionSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class FunctionContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/functions/search?";
@Autowired
	private ObjectMapper objectMapper;public FunctionContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<FunctionContract> getFunctionById(FunctionSearchContract functionSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(functionSearchContract.getId()!=null)
        {
        	content.append("id="+functionSearchContract.getId());
        }
        
        if(functionSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+functionSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<FunctionContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<FunctionContract>>() {
				});

		return result;

	}
} 