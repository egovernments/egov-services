package org.egov.tl.indexer.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.indexer.config.PropertiesManager;
import org.egov.tl.indexer.domain.exception.EndPointException;
import org.egov.tl.indexer.web.contract.FinancialYearContract;
import org.egov.tl.indexer.web.requests.TlMasterRequestInfo;
import org.egov.tl.indexer.web.requests.TlMasterRequestInfoWrapper;
import org.egov.tl.indexer.web.response.FinancialYearContractResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FinancialYearContractRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public FinancialYearContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public FinancialYearContract findFinancialYearById(String tenantId, String id,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManager.getFinancialYearServiceHostName()
				+ propertiesManager.getFinancialYearServiceBasePath();
		String searchUrl = propertiesManager.getFinancialYearServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		content.append("id=" + id);

		if (tenantId != null) {
			content.append("&tenantId=" + tenantId);
		}
		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = getTlMasterRequestInfoWrapper(requestInfoWrapper);
		url = url + content.toString();
		FinancialYearContractResponse financialYearContractResponse = null;
		try {

			financialYearContractResponse = restTemplate.postForObject(url, tlMasterRequestInfoWrapper,
					FinancialYearContractResponse.class);

		} catch (Exception e) {
			throw new EndPointException(propertiesManager.getEndPointError() + url,
					requestInfoWrapper.getRequestInfo());
		}

		if (financialYearContractResponse != null && financialYearContractResponse.getFinancialYears() != null
				&& financialYearContractResponse.getFinancialYears().size() > 0) {

			return financialYearContractResponse.getFinancialYears().get(0);
		} else {
			return null;
		}

	}

	public TlMasterRequestInfoWrapper getTlMasterRequestInfoWrapper(RequestInfoWrapper requestInfoWrapper) {

		TlMasterRequestInfoWrapper tlMasterRequestInfoWrapper = new TlMasterRequestInfoWrapper();
		TlMasterRequestInfo tlMasterRequestInfo = new TlMasterRequestInfo();
		ModelMapper mapper = new ModelMapper();
		mapper.map(requestInfoWrapper.getRequestInfo(), tlMasterRequestInfo);
		tlMasterRequestInfo.setTs(new Date().getTime());
		tlMasterRequestInfoWrapper.setRequestInfo(tlMasterRequestInfo);

		return tlMasterRequestInfoWrapper;
	}
}