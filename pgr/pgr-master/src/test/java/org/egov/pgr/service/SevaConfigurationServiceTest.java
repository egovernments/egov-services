package org.egov.pgr.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.pgr.repository.SevaConfigurationRepository;
import org.egov.pgr.web.contract.SevaConfigurationGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SevaConfigurationServiceTest {

	@Mock
	private SevaConfigurationRepository sevaConfigurationRepository;

	@InjectMocks
	private SevaConfigurationService sevaConfigurationService;

	@Test
	public void test_should_fetch_service_type_from_db() {
		SevaConfigurationGetRequest criteria = SevaConfigurationGetRequest.builder().keyName("name")
				.id(Collections.singletonList(1L)).tenantId("default").build();
		when(sevaConfigurationRepository.findForCriteria(criteria)).thenReturn(getSearchResult());
		Map<String, List<String>> result = sevaConfigurationService.getSevaConfigurations(criteria);
		assertEquals(result, getSearchResult());

	}

	private HashMap<String, List<String>> getSearchResult() {
		HashMap<String, List<String>> objectObjectHashMap = new HashMap<>();
		objectObjectHashMap.put("test", Collections.singletonList("test"));
		return objectObjectHashMap;

	}
}
