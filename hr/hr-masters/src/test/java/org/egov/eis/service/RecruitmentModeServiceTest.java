package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.RecruitmentMode;
import org.egov.eis.repository.RecruitmentModeRepository;
import org.egov.eis.web.contract.RecruitmentModeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecruitmentModeServiceTest {

	@Mock
	private RecruitmentModeRepository recruitmentModeRepository;
	
	@InjectMocks
	private RecruitmentModeService recruitmentModeService;
	
	@Test
	public void test_getRecruitmentModes() {
		List<RecruitmentMode> recruitmentModes = new ArrayList<>();
		when(recruitmentModeRepository.findForCriteria(any(RecruitmentModeGetRequest.class))).thenReturn(recruitmentModes);
		List<RecruitmentMode> result = recruitmentModeService.getRecruitmentModes(any(RecruitmentModeGetRequest.class));
	    assertThat(result).isEqualTo(recruitmentModes);
	}
}
