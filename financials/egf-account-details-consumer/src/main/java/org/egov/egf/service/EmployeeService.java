/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any Long of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.egf.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;

import org.egov.egf.config.PropertiesManager;
import org.egov.egf.domain.model.contract.AccountDetailKeyContract;
import org.egov.egf.domain.model.contract.AccountDetailKeyContractRequest;
import org.egov.egf.domain.model.contract.AccountDetailKeyContractResponse;
import org.egov.egf.domain.model.contract.RequestInfo;
import org.egov.egf.domain.model.contract.RestErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmployeeService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Value("${si.microservice.user}")
	private String siUser;

	@Value("${si.microservice.password}")
	private String siPassword;

	@Value("${si.microservice.usertype}")
	private String siUserType;

	@Value("${si.microservice.scope}")
	private String siScope;

	@Value("${si.microservice.granttype}")
	private String siGrantType;

	@Value("${egov.services.user.token.url}")
	private String tokenGenUrl;

	public static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

	public void processRequest(String employeeId, String employeeName,String tenantId, RequestInfo requestInfo) {
		try {
			// AccountDetailTypeContract accDetailType =
			// getAccountDetailType(tenantId, requestInfo);
			AccountDetailKeyContractResponse accDetailKeyResponse = createAccountDetailKey(employeeId,employeeName, requestInfo);
			LOGGER.debug("Created AccountDetailKeyContractResponse : " + accDetailKeyResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private AccountDetailKeyContractResponse createAccountDetailKey(String employeeId, String employeeName, RequestInfo requestInfo)
			throws URISyntaxException {

		AccountDetailKeyContract accDetailKey = new AccountDetailKeyContract();
		accDetailKey.setKeyId(employeeId);
		// accDetailKey.setAccountDetailType(accDetailType);
		// accDetailKey.setCreatedBy(Long.parseLong(requestInfo.getRequesterId()));
		accDetailKey.setCreatedDate(new Date());
		// accDetailKey.setLastModifiedBy(Long.parseLong(requestInfo.getRequesterId()));
		accDetailKey.setLastModifiedDate(new Date());
		// accDetailKey.setTenantId(tenantId);
		accDetailKey.setKeyName(employeeName);

		requestInfo.setAuthToken(generateAdminToken());
		AccountDetailKeyContractRequest accDetailKeyRequest = AccountDetailKeyContractRequest.builder()
				.requestInfo(requestInfo).accountDetailKey(accDetailKey).tenantId(requestInfo.getTenantId()).build();

		LOGGER.debug("AccountDetailKeyContractRequest : " + accDetailKeyRequest);

		URI url = null;
		String urlStr = null;
		try {
			urlStr = propertiesManager.getFainanceErpHostname(requestInfo.getTenantId()) + propertiesManager.getEgfMastersServiceBasepath()
					+ propertiesManager.getEgfMastersServiceAccountdetailkeysBasepath()
					+ propertiesManager.getEgfMastersServiceAccountdetailkeysCreatepath();
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

	public RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestErrorHandler());

		return restTemplate;
	}

	public String generateAdminToken() {
		final RestTemplate restTemplate = createRestTemplate();

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		header.add("Authorization", "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0");

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", this.siUser);
		map.add("scope", this.siScope);
		map.add("password", this.siPassword);
		map.add("grant_type", this.siGrantType);
		// TOD-DO - Mani : why this hard coding ?
		map.add("tenantId", "pb.jalandhar");
		map.add("userType", this.siUserType);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, header);

		try {
			LOGGER.info("call:" + tokenGenUrl);
			Object response = restTemplate.postForObject(tokenGenUrl, request, Object.class);
			if (response != null)
				return String.valueOf(((HashMap) response).get("access_token"));
		} catch (RestClientException e) {
			LOGGER.info("Eror while getting admin authtoken", e);
			return null;
		}
		return null;
	}

}
