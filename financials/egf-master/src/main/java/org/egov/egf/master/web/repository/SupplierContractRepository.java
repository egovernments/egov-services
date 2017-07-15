package org.egov.egf.master.web.repository ;
import org.egov.common.web.contract.CommonResponse; import org.egov. egf.master.web.contract.SupplierContract;import org.egov. egf.master.web.contract.SupplierSearchContract;import org.springframework.beans.factory.annotation.Autowired; import org.springframework.beans.factory.annotation.Value; import org.springframework.web.client.RestTemplate;  import com.fasterxml.jackson.core.type.TypeReference; import com.fasterxml.jackson.databind.ObjectMapper; public class SupplierContractRepository { private RestTemplate restTemplate; private String hostUrl; public static final String SEARCH_URL = " /egf-master/suppliers/search?";
@Autowired
	private ObjectMapper objectMapper;public SupplierContractRepository(@Value("${egf.masterhost.url}") String hostUrl,RestTemplate restTemplate) {
this.restTemplate = restTemplate;
this.hostUrl = hostUrl;
}
public CommonResponse<SupplierContract> getSupplierById(SupplierSearchContract supplierSearchContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content=new StringBuffer();
        if(supplierSearchContract.getId()!=null)
        {
        	content.append("id="+supplierSearchContract.getId());
        }
        
        if(supplierSearchContract.getTenantId()!=null)
        {
        	content.append("tenantId="+supplierSearchContract.getTenantId());
        }
        url=url+content.toString();
		CommonResponse<SupplierContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<SupplierContract>>() {
				});

		return result;

	}
} 