package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.FunctionaryContract;import org.egov. egf.master.web.contract.FunctionarySearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class FunctionaryContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/functionaries/search?";
@Autowired
	private ObjectMapper objectMapper;public FunctionaryContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<FunctionaryContract> getFunctionaryById(FunctionarySearchContract functionarySearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(functionarySearchContract.getId()!=null)
        {
        	content.append("id="+functionarySearchContract.getId());
        }
        
        if(functionarySearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+functionarySearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<FunctionaryContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<FunctionaryContract>>() {
				});

		return result;

	}
} 