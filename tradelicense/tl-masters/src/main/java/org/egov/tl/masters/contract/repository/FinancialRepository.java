package org.egov.tl.masters.contract.repository;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.masters.web.requests.TlMasterRequestInfo;
import org.egov.tl.masters.web.requests.TlMasterRequestInfoWrapper;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.EndPointException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.model.FinancialYearContract;
import org.egov.tradelicense.domain.model.FinancialYearContractResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FinancialRepository {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public FinancialYearContract findFinancialYearById(String tenantId, String id,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManager.getFinancialServiceHostName()
				+ propertiesManager.getFinancialServiceBasePath();
		String searchUrl = propertiesManager.getFinancialServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();

		content.append("?id=" + id);

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
			throw new EndPointException("Error connecting to Location end point " + url,
					requestInfoWrapper.getRequestInfo());
		}

		if (financialYearContractResponse != null && financialYearContractResponse.getFinancialYears() != null
				&& financialYearContractResponse.getFinancialYears().size() > 0) {

			return financialYearContractResponse.getFinancialYears().get(0);
		} else {
			throw new InvalidInputException(propertiesManager.getFinancialYearErrorMsg(),
					requestInfoWrapper.getRequestInfo());
		}

	}

	public FinancialYearContract findFinancialYearIdByDate(String tenantId, Long date,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManager.getFinancialServiceHostName()
				+ propertiesManager.getFinancialServiceBasePath();
		String searchUrl = propertiesManager.getFinancialServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		String asOnDate = sf.format(new Date(date));
		if (date != null) {
			content.append("?asOnDate=" + asOnDate);
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
			throw new EndPointException("Error connecting to Location end point " + url,
					requestInfoWrapper.getRequestInfo());
		}

		if (financialYearContractResponse != null && financialYearContractResponse.getFinancialYears() != null
				&& financialYearContractResponse.getFinancialYears().size() > 0) {

			return financialYearContractResponse.getFinancialYears().get(0);
		} else {
			return null;
		}

	}
	
	public FinancialYearContract findFinancialYearByFinRange(String tenantId, String finRange,
			RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManager.getFinancialServiceHostName()
				+ propertiesManager.getFinancialServiceBasePath();
		String searchUrl = propertiesManager.getFinancialServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (finRange != null) {
			content.append("?finYearRange=" + finRange);
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
			throw new EndPointException("Error connecting to Location end point " + url,
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
