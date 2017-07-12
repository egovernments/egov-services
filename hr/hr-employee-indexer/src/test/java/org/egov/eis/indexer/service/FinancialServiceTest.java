package org.egov.eis.indexer.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.response.ResponseInfo;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class FinancialServiceTest {

	@Mock
	private PropertiesManager propertiesManager;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private FinancialsService fianancialsService;

	@Test
	public void testGetBank() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Bank bank = new Bank().builder().id(100L).name("employee1").tenantId("1").build();
		List<Bank> banks = new ArrayList<>();
		banks.add(bank);
		BankResponse bankResponse = new BankResponse().builder().responseInfo(responseInfo).banks(banks).build();

		when(propertiesManager.getEgfMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgfMastersServiceBasepath()).thenReturn("/egf-masters");
		when(propertiesManager.getEgfMastersServiceBankSearchPath()).thenReturn("/banks/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/egf-masters/banks/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), BankResponse.class))
				.thenReturn(bankResponse);

		Bank insertedBank = fianancialsService.getBank(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedBank, bank);
	}

	@Test
	public void testGetBankBranch() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		BankBranch bankBranch = new BankBranch().builder().id(100L).name("employee1").tenantId("1").build();
		List<BankBranch> bankBranches = new ArrayList<>();
		bankBranches.add(bankBranch);
		BankBranchResponse bankBranchResponse = new BankBranchResponse().builder().responseInfo(responseInfo)
				.bankBranches(bankBranches).build();

		when(propertiesManager.getEgfMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgfMastersServiceBasepath()).thenReturn("/egf-masters");
		when(propertiesManager.getEgfMastersServiceBankBranchSearchPath()).thenReturn("/bankbranches/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/egf-masters/bankbranches/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), BankBranchResponse.class))
				.thenReturn(bankBranchResponse);

		BankBranch insertedBankBranch = fianancialsService.getBankBranch(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedBankBranch, bankBranch);
	}

	@Test
	public void testGetFund() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Fund fund = new Fund().builder().id(100L).name("employee1").tenantId("1").build();
		List<Fund> funds = new ArrayList<>();
		funds.add(fund);
		FundResponse fundResponse = new FundResponse().builder().responseInfo(responseInfo).funds(funds).build();

		when(propertiesManager.getEgfMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgfMastersServiceBasepath()).thenReturn("/egf-masters");
		when(propertiesManager.getEgfMastersServiceFundSearchPath()).thenReturn("/funds/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/egf-masters/funds/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), FundResponse.class))
				.thenReturn(fundResponse);

		Fund insertedFund = fianancialsService.getFund(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedFund, fund);
	}

	@Test
	public void testGetFunction() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Function function = new Function().builder().id(100L).name("employee1").tenantId("1").build();
		List<Function> functions = new ArrayList<>();
		functions.add(function);
		FunctionResponse functionResponse = new FunctionResponse().builder().responseInfo(responseInfo)
				.functions(functions).build();

		when(propertiesManager.getEgfMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgfMastersServiceBasepath()).thenReturn("/egf-masters");
		when(propertiesManager.getEgfMastersServiceFunctionSearchPath()).thenReturn("/functions/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/egf-masters/functions/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), FunctionResponse.class))
				.thenReturn(functionResponse);

		Function insertedFunction = fianancialsService.getFunction(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedFunction, function);
	}

	@Test
	public void testGetFunctionary() {

		ResponseInfo responseInfo = new ResponseInfo("emp", "1.0", "2017-01-18T07:18:23.130Z", "uief87324",
				"20170310130900", "200");
		Functionary functionary = new Functionary().builder().id(100L).name("employee1").tenantId("1").build();
		List<Functionary> functionaries = new ArrayList<>();
		functionaries.add(functionary);
		FunctionaryResponse functionaryResponse = new FunctionaryResponse().builder().responseInfo(responseInfo)
				.functionaries(functionaries).build();

		when(propertiesManager.getEgfMastersServiceHost()).thenReturn("http://egov-micro-dev.egovernments.org");
		when(propertiesManager.getEgfMastersServiceBasepath()).thenReturn("/egf-masters");
		when(propertiesManager.getEgfMastersServiceFunctionarySearchPath()).thenReturn("/functionaries/_search");

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		URI uri = null;
		try {
			uri = new URI("http://egov-micro-dev.egovernments.org/egf-masters/functionaries/_search?id=100&tenantId=1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		when(restTemplate.postForObject(uri, getRequestInfoAsHttpEntity(requestInfoWrapper), FunctionaryResponse.class))
				.thenReturn(functionaryResponse);

		Functionary insertedFunctionary = fianancialsService.getFunctionary(100L, "1", new RequestInfoWrapper());
		assertEquals(insertedFunctionary, functionary);
	}

	private Object getRequestInfoAsHttpEntity(RequestInfoWrapper requestInfoWrapper) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RequestInfoWrapper> httpEntityRequest = new HttpEntity<RequestInfoWrapper>(requestInfoWrapper,
				headers);
		return httpEntityRequest;
	}
}
