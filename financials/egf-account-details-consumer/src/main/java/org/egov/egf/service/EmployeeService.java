package org.egov.egf.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.egf.config.PropertiesManager;
import org.egov.egf.domain.model.contract.AccountDetailKeyContract;
import org.egov.egf.domain.model.contract.AccountDetailKeyContractRequest;
import org.egov.egf.domain.model.contract.AccountDetailKeyContractResponse;
import org.egov.egf.domain.model.contract.AccountDetailTypeContract;
import org.egov.egf.domain.model.contract.AccountDetailTypeContractResponse;
import org.egov.egf.domain.model.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

	@Autowired
	private PropertiesManager propertiesManager;

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

	public void processRequest(int employeeId, String tenantId, RequestInfo requestInfo) {
		try {
			AccountDetailTypeContract accDetailType = getAccountDetailType(tenantId, requestInfo);
			AccountDetailKeyContractResponse accDetailKeyResponse = createAccountDetailKey(employeeId, accDetailType,
					requestInfo, tenantId);
			LOGGER.debug("Created AccountDetailKeyContractResponse : " + accDetailKeyResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AccountDetailTypeContract getAccountDetailType(String tenantId, RequestInfo requestInfo) throws URISyntaxException {
		URI url = null;
		String urlStr = null;
		try {
			urlStr = propertiesManager.getEgfMastersServiceHostname()
					+ propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceAccountdetailtypesBasepath()
					+ propertiesManager.getEgfMastersServiceAccountdetailtypesSearchpath()
					+ "?name=Employee&tenantId=" + tenantId;
			url = new URI(urlStr);

			LOGGER.debug(url.toString());
		} catch (URISyntaxException e) {
			LOGGER.error("Exception Occurred While Creating URI: " + urlStr);
			throw e;
		}

		AccountDetailTypeContractResponse accountDetailTypeResponse = null;
		try {
			accountDetailTypeResponse = new RestTemplate().postForObject(url, requestInfo,
					AccountDetailTypeContractResponse.class);
		} catch (Exception e) {
			LOGGER.error("Exception Occurred While Accessing Searching AccountDetailType At URI: " + url
					+ " With Payload: " + requestInfo);
			throw e;
		}
		LOGGER.debug("AccountDetailTypeResponse returned from egf-masters : " + accountDetailTypeResponse);
		return accountDetailTypeResponse.getAccountDetailTypes().get(0);
	}

	private AccountDetailKeyContractResponse createAccountDetailKey(int employeeId,
			AccountDetailTypeContract accDetailType, RequestInfo requestInfo, String tenantId) throws URISyntaxException {

		AccountDetailKeyContract accDetailKey = new AccountDetailKeyContract();
				accDetailKey.setKey(employeeId);
				accDetailKey.setAccountDetailType(accDetailType);
				accDetailKey.setCreatedBy(Long.parseLong(requestInfo.getRequesterId()));
				accDetailKey.setCreatedDate(new Date());
				accDetailKey.setTenantId(tenantId);

		List<AccountDetailKeyContract> accDetailKeyContracts = new ArrayList<>();
		accDetailKeyContracts.add(accDetailKey);

		AccountDetailKeyContractRequest accDetailKeyRequest = AccountDetailKeyContractRequest.builder()
				.requestInfo(requestInfo).accountDetailKeys(accDetailKeyContracts)
				.accountDetailKey(accDetailKey).build();

		LOGGER.debug("AccountDetailKeyContractRequest : " + accDetailKeyRequest);

		URI url = null;
		String urlStr = null;
		try {
			urlStr = propertiesManager.getEgfMastersServiceHostname()
					+ propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceAccountdetailkeysBasepath()
					+ propertiesManager.getEgfMastersServiceAccountdetailkeysCreatepath()
					+ "?tenantId=" + tenantId;
			url = new URI(urlStr);

			LOGGER.debug(url.toString());
		} catch (URISyntaxException e) {
			LOGGER.error("Exception Occurred While Creating URI: " + urlStr);
			throw e;
		}

		AccountDetailKeyContractResponse accountDetailKeyResponse = null;
		try {
			accountDetailKeyResponse = new RestTemplate().postForObject(url, accDetailKeyRequest,
					AccountDetailKeyContractResponse.class);
		} catch (HttpClientErrorException e) {
			LOGGER.error("Exception Occurred While Accessing Creating AccountDetailKey At URI: " + url
					+ " With Payload: " + accDetailKeyRequest);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Exception Occurred While Accessing Creating AccountDetailKey At URI: " + url
					+ " With Payload: " + accDetailKeyRequest);
			throw e;
		}
		LOGGER.debug("AccountDetailKeyResponse returned from egf-masters : " + accountDetailKeyResponse);
		return accountDetailKeyResponse;
	}

}
