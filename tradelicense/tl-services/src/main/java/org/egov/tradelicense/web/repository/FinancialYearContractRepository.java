package org.egov.tradelicense.web.repository;

import java.util.Date;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.web.contract.FinancialYearContract;
import org.egov.tradelicense.web.requests.FinancialYearContractResponse;
import org.egov.tradelicense.web.requests.TlMasterRequestInfo;
import org.egov.tradelicense.web.requests.TlMasterRequestInfoWrapper;
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
	private PropertiesManager propertiesManger;

	public FinancialYearContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public FinancialYearContract findFinancialYearIdByDate(String tenantId, String date,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getFinancialYearServiceHostName()
				+ propertiesManger.getFinancialYearServiceBasePath();
		String searchUrl = propertiesManger.getFinancialYearServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		if (date != null) {
			content.append("asOnDate=" + date);
		}

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
			log.error("Error while connecting to the location end point");
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