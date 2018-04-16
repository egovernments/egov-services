package org.egov.eis.indexer.service;

import java.net.URI;

import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.egf.persistence.queue.contract.Bank;
import org.egov.egf.persistence.queue.contract.BankBranch;
import org.egov.egf.persistence.queue.contract.BankBranchResponse;
import org.egov.egf.persistence.queue.contract.BankResponse;
import org.egov.egf.persistence.queue.contract.Function;
import org.egov.egf.persistence.queue.contract.FunctionResponse;
import org.egov.egf.persistence.queue.contract.Functionary;
import org.egov.egf.persistence.queue.contract.FunctionaryResponse;
import org.egov.egf.persistence.queue.contract.Fund;
import org.egov.egf.persistence.queue.contract.FundResponse;
import org.egov.eis.indexer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FinancialsService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(FinancialsService.class);

	public Bank getBank(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		BankResponse bankResponse = null;
		try {
			url = new URI(propertiesManager.getEgfMastersServiceHost()
					+ propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceBankSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			bankResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					BankResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Bank API : " + e.getMessage());
			return null;
		}
		return bankResponse.getBanks().get(0);
	}

	public BankBranch getBankBranch(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		BankBranchResponse bankBranchResponse = null;
		try {
			url = new URI(propertiesManager.getEgfMastersServiceHost()
					+ propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceBankBranchSearchPath() + "?id=" + id
					+ "&tenantId=" + tenantId);
			LOGGER.debug(url.toString());
			bankBranchResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					BankBranchResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing BankBranch API : " + e.getMessage());
			return null;
		}
		return bankBranchResponse.getBankBranches().get(0);
	}

	public Fund getFund(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		FundResponse fundResponse = null;
		try {
			url = new URI(propertiesManager.getEgfMastersServiceHost()
					+ propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceFundSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			fundResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					FundResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Fund API : " + e.getMessage());
			return null;
		}
		return fundResponse.getFunds().get(0);
	}

	public Function getFunction(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		FunctionResponse functionResponse = null;
		try {
			url = new URI(propertiesManager.getEgfMastersServiceHost()
					+ propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceFunctionSearchPath() + "?id=" + id + "&tenantId="
					+ tenantId);
			LOGGER.debug(url.toString());
			functionResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					FunctionResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Function API : " + e.getMessage());
			return null;
		}
		return functionResponse.getFunctions().get(0);
	}

	public Functionary getFunctionary(Long id, String tenantId, RequestInfoWrapper requestInfoWrapper) {
		URI url = null;
		FunctionaryResponse functionaryResponse = null;
		try {
			url = new URI(propertiesManager.getEgfMastersServiceHost()
					+ propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceFunctionarySearchPath() + "?id=" + id
					+ "&tenantId=" + tenantId);
			LOGGER.debug(url.toString());
			functionaryResponse = restTemplate.postForObject(url, getRequestInfoAsHttpEntity(requestInfoWrapper),
					FunctionaryResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Following exception occurred while accessing Functionary API : " + e.getMessage());
			return null;
		}
		return functionaryResponse.getFunctionaries().get(0);
	}

	private HttpEntity<RequestInfoWrapper> getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RequestInfoWrapper> httpEntityRequest = new HttpEntity<RequestInfoWrapper>(requestInfoWrapper,
				headers);
		return httpEntityRequest;
	}
}