package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.model.Group;
import org.egov.eis.repository.GroupRepository;
import org.egov.eis.web.contract.GroupGetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

	@Mock
	private GroupRepository groupRepository;
	
	@InjectMocks
	private GroupService groupService;
	
	@Test
	public void test_getGroups() {
		
		List<Group> groups = new ArrayList<>();
		when(groupRepository.findForCriteria(any(GroupGetRequest.class))).thenReturn(groups);
	    List<Group> result = groupService.getGroups(any(GroupGetRequest.class));
	    assertThat(result).isEqualTo(groups);
	}
}
