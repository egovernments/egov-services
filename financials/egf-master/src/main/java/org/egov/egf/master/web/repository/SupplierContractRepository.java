package org.egov.egf.master.web.repository;

import org.egov.common.web.contract.CommonResponse;
import org.egov.egf.master.web.contract.SupplierContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SupplierContractRepository {
	private RestTemplate restTemplate;
	private String hostUrl;
	public static final String SEARCH_URL = " /egf-master/suppliers/search?";
	@Autowired
	private ObjectMapper objectMapper;

	public SupplierContractRepository(@Value("${egf.masterhost.url}") String hostUrl, RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.hostUrl = hostUrl;
	}

	public SupplierContract findById(SupplierContract supplierContract) {

		String url = String.format("%s%s", hostUrl, SEARCH_URL);
		StringBuffer content = new StringBuffer();
		if (supplierContract.getId() != null) {
			content.append("id=" + supplierContract.getId());
		}

		if (supplierContract.getTenantId() != null) {
			content.append("tenantId=" + supplierContract.getTenantId());
		}
		url = url + content.toString();
		CommonResponse<SupplierContract> result = objectMapper.convertValue(
				restTemplate.postForObject(url, null, CommonResponse.class),
				new TypeReference<CommonResponse<SupplierContract>>() {
				});

		if (result.getData() != null && result.getData().size() == 1) {
			return result.getData().get(0);
		} else {
			return null;
		}

	}
}