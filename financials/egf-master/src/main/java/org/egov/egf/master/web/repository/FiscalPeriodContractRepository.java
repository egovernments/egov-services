package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.FiscalPeriodContract;import org.egov. egf.master.web.contract.FiscalPeriodSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class FiscalPeriodContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/fiscalperiods/search?";
@Autowired
	private ObjectMapper objectMapper;public FiscalPeriodContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<FiscalPeriodContract> getFiscalPeriodById(FiscalPeriodSearchContract fiscalPeriodSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(fiscalPeriodSearchContract.getId()!=null)
        {
        	content.append("id="+fiscalPeriodSearchContract.getId());
        }
        
        if(fiscalPeriodSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+fiscalPeriodSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<FiscalPeriodContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<FiscalPeriodContract>>() {
				});

		return result;

	}
} 