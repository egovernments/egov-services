package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.HRStatus;
import org.egov.eis.repository.HRStatusRepository;
import org.egov.eis.web.contract.HRStatusGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HRStatusServiceTest {

	@Mock
	private HRStatusRepository hrStatusRepository;
	
	@InjectMocks
	private HRStatusService hrStatusService;
	
	@Test
	public void test_getHrStatus() {
		List<HRStatus> hrServices = new ArrayList<>();
		when(hrStatusRepository.findForCriteria(any(HRStatusGetRequest.class))).thenReturn(hrServices);
		List<HRStatus> result = hrStatusService.getHRStatuses(any(HRStatusGetRequest.class));
	    assertThat(result).isEqualTo(hrServices);
	}
}
