package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.eis.repository.HRConfigurationRepository;
import org.egov.eis.web.contract.HRConfigurationGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HRConfigurationServiceTest {

	@Mock
	private HRConfigurationRepository hrConfigurationRepository;
	
	@InjectMocks
	private HRConfigurationService hrConfigurationService;
	
	@Test
	public void test_getHrConfigs() {
		
		Map<String, List<String>> hrConfigurations = new HashMap<>();
		when(hrConfigurationRepository.findForCriteria(any(HRConfigurationGetRequest.class))).thenReturn(hrConfigurations);
		Map<String, List<String>> result = hrConfigurationService.getHRConfigurations(any(HRConfigurationGetRequest.class));
	    assertThat(result).isEqualTo(hrConfigurations);
	}
}
