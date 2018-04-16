package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.RecruitmentQuota;
import org.egov.eis.repository.RecruitmentQuotaRepository;
import org.egov.eis.web.contract.RecruitmentQuotaGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecruitmentQuotaServiceTest {

	@Mock
	private RecruitmentQuotaRepository recruitmentQuotaRepository;
	
	@InjectMocks
	private RecruitmentQuotaService recruitmentQuotaService;
	
	@Test
	public void test_getRecruitmentQuotas() {
		List<RecruitmentQuota> recruitmentQuotas = new ArrayList<>();
		when(recruitmentQuotaRepository.findForCriteria(any(RecruitmentQuotaGetRequest.class))).thenReturn(recruitmentQuotas);
		List<RecruitmentQuota> result = recruitmentQuotaService.getRecruitmentQuotas(any(RecruitmentQuotaGetRequest.class));
	    assertThat(result).isEqualTo(recruitmentQuotas);
	}
}
