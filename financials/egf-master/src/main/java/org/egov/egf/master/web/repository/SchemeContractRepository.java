package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.SchemeContract;import org.egov. egf.master.web.contract.SchemeSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class SchemeContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/schemes/search?";
@Autowired
	private ObjectMapper objectMapper;public SchemeContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<SchemeContract> getSchemeById(SchemeSearchContract schemeSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(schemeSearchContract.getId()!=null)
        {
        	content.append("id="+schemeSearchContract.getId());
        }
        
        if(schemeSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+schemeSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<SchemeContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<SchemeContract>>() {
				});

		return result;

	}
} 