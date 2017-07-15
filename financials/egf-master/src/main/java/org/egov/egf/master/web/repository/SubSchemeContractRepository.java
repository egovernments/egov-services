package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.SubSchemeContract;import org.egov. egf.master.web.contract.SubSchemeSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class SubSchemeContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/subschemes/search?";
@Autowired
	private ObjectMapper objectMapper;public SubSchemeContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<SubSchemeContract> getSubSchemeById(SubSchemeSearchContract subSchemeSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(subSchemeSearchContract.getId()!=null)
        {
        	content.append("id="+subSchemeSearchContract.getId());
        }
        
        if(subSchemeSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+subSchemeSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<SubSchemeContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<SubSchemeContract>>() {
				});

		return result;

	}
} 