package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.PositionHierarchy;
import org.egov.eis.repository.PositionHierarchyRepository;
import org.egov.eis.web.contract.PositionHierarchyGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PositionhierarchyServiceTest {

	@Mock
	private PositionHierarchyRepository positionHierarchyRepository;
	
	@InjectMocks
	private PositionHierarchyService positionHierarchyService;
	
	@Test
	public void test_getPositionHierarchy() {
		List<PositionHierarchy> positionHierarchies = new ArrayList<>();
		when(positionHierarchyRepository.findForCriteria(any(PositionHierarchyGetRequest.class))).thenReturn(positionHierarchies);
		List<PositionHierarchy> result = positionHierarchyService.getPositionHierarchies(any(PositionHierarchyGetRequest.class));
	    assertThat(result).isEqualTo(positionHierarchies);
	}
}
