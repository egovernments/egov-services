package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.RecruitmentType;
import org.egov.eis.repository.RecruitmentTypeRepository;
import org.egov.eis.web.contract.RecruitmentTypeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecruitmentTypeServiceTest {

	@Mock
	private RecruitmentTypeRepository recruitmentTypeRepository;
	
	@InjectMocks
	private RecruitmentTypeService recruitmentTypeService;
	
	@Test
	public void test_getRecruitmentTypes() {
		List<RecruitmentType> recruitmentTypes = new ArrayList<>();
		when(recruitmentTypeRepository.findForCriteria(any(RecruitmentTypeGetRequest.class))).thenReturn(recruitmentTypes);
		List<RecruitmentType> result = recruitmentTypeService.getRecruitmentTypes(any(RecruitmentTypeGetRequest.class));
	    assertThat(result).isEqualTo(recruitmentTypes);
	}
}
