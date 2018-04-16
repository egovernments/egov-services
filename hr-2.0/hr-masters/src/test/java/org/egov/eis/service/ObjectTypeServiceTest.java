package org.egov.eis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.eis.model.ObjectType;
import org.egov.eis.repository.ObjectTypeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ObjectTypeServiceTest {

	@Mock
	private ObjectTypeRepository objectTypeRepository;
	
	@InjectMocks
	private ObjectTypeService objectTypeService;
	
	@Test
	public void test_getObjectType() {
		ObjectType objectType = new ObjectType();
		when(objectTypeRepository.findForId(any(Long.class))).thenReturn(objectType);
	    ObjectType result = objectTypeService.getObjectType(any(Long.class));
	    assertThat(result).isEqualTo(objectType);
	}
}
