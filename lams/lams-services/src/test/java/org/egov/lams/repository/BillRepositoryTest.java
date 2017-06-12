package org.egov.lams.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.web.contract.BillInfo;
import org.egov.lams.web.contract.BillRequest;
import org.egov.lams.web.contract.BillResponse;
import org.egov.lams.web.contract.BillSearchCriteria;
import org.egov.lams.web.contract.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class BillRepositoryTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private PropertiesManager propertiesManager;

	@InjectMocks
	private BillRepository billRepository;

	@Test
	public void createBillAndGetXmlTest() {

		RequestInfo requestInfo = new RequestInfo();
		BillRequest billRequest = new BillRequest();
		List<BillInfo> billInfos = getBillInfos();
		billRequest.setRequestInfo(requestInfo);
		billRequest.setBillInfos(billInfos);

		BillResponse billResponse = new BillResponse();
		String billXml = "billXml";
		List<String> billXmls = new ArrayList<>();
		billXmls.add(billXml);
		billResponse.setBillXmls(billXmls);
		String url = "host/_create";

		when(propertiesManager.getDemandServiceHostName()).thenReturn("host/");
		when(propertiesManager.getDemandBillCreateService()).thenReturn("_create");
		when(restTemplate.postForObject(url, billRequest, BillResponse.class)).thenReturn(billResponse);

		assertTrue(billXml.equals(billRepository.createBillAndGetXml(billInfos, requestInfo)));
	}

	@Test
	public void searchBillTest() {

		BillSearchCriteria billSearchCriteria = new BillSearchCriteria();
		billSearchCriteria.setBillId(1l);
		RequestInfo requestInfo = new RequestInfo();
		List<BillInfo> billInfos = getBillInfos();
		BillResponse billResponse = new BillResponse();
		billResponse.setBillInfos(billInfos);
		String url = "host/_search" + "?billId=" + billSearchCriteria.getBillId();

		when(propertiesManager.getDemandServiceHostName()).thenReturn("host/");
		when(propertiesManager.getDemandBillSearchService()).thenReturn("_search");
		when(restTemplate.postForObject(url, requestInfo, BillResponse.class)).thenReturn(billResponse);

		assertTrue(billInfos.get(0).equals(billRepository.searchBill(billSearchCriteria, requestInfo)));
	}

	private List<BillInfo> getBillInfos() {

		BillInfo billInfo = new BillInfo();
		billInfo.setBillAmount(99.0);
		List<BillInfo> billInfos = new ArrayList<>();
		billInfos.add(billInfo);
		return billInfos;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void getPurposeTest() {
		String url = "host/service";
		Map map = new HashMap<>();
		map.put("map", "map");
		//when(propertiesManager.getPurposeHostName()).thenReturn("host/");
		when(propertiesManager.getPurposeService()).thenReturn("service");
		when(restTemplate.getForObject(url, Map.class)).thenReturn(map);

		//assertTrue(map.equals(billRepository.getPurpose()));
	}
}
