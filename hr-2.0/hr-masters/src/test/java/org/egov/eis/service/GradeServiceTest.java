package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Grade;
import org.egov.eis.repository.GradeRepository;
import org.egov.eis.web.contract.GradeGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GradeServiceTest {


	@Mock
	private GradeRepository gradeRepository;
	
	@InjectMocks
	private GradeService gradeService;
	
	@Test
	public void test_getGrades() {
		
		List<Grade> grades = new ArrayList<>();
		when(gradeRepository.findForCriteria(any(GradeGetRequest.class))).thenReturn(grades);
	    List<Grade> result = gradeService.getGrades(any(GradeGetRequest.class));
	    assertThat(result).isEqualTo(grades);
	}
}
