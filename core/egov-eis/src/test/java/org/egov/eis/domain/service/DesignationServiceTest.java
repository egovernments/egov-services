package org.egov.eis.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.persistence.entity.Designation;
import org.egov.eis.persistence.repository.DesignationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DesignationServiceTest {
	
	@Mock
	private DesignationRepository designationRepository;
	
	@InjectMocks
	private DesignationService designationService;

	@Test
	public void test_should_fetch_designation_for_department_id() {

		Date currentDate = new Date();
		
		when(designationRepository.getAllDesignationsByDepartment(1L, currentDate)).thenReturn(getExpectedDesignationDetails());

		List<Designation> designationList = designationService.getAllDesignationByDepartment(1L, currentDate);

		assertEquals("Accounts Officer", designationList.get(0).getName());
	}
	
	private List<Designation> 	getExpectedDesignationDetails() {
		final List<Designation> desigList = new ArrayList<>(); 
		final Designation designation1 = Designation.builder().id(1L).name("Accounts Officer").code("AO").build();
		final Designation designation2 = Designation.builder().id(2L).name("Assistant Engineer").code("AE").build();
		desigList.add(designation1);
		desigList.add(designation2);
		return desigList;
	}
}
