package org.egov.lams.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.repository.LamsConfigurationRepository;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class LamsConfigurationServiceTest {
	
	@Mock
	private LamsConfigurationRepository lamsConfigurationRepository;
	
	@InjectMocks
	private LamsConfigurationService lamsConfigurationService;

	@Test
	public void getLamsConfigurationTest(){
		
		Map<String, List<String>> map = new HashMap<>();
		List<String> list = new ArrayList<>();
		list.add("value");
		map.put("key",list);		
		when(lamsConfigurationRepository.findForCriteria(any(LamsConfigurationGetRequest.class))).thenReturn(map);
		assertTrue(map.equals(lamsConfigurationService.getLamsConfigurations(any(LamsConfigurationGetRequest.class))));
	}

}
