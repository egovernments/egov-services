package org.egov.mr.repository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.mr.model.MarryingPerson;
import org.egov.mr.model.enums.MaritalStatus;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MarryingPersonRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private MarryingPersonRepository marryingPersonRepository;

	@Test
	public void testSave() {
		when(jdbcTemplate.update(any(String.class), Matchers.<Object[]>any())).thenReturn(Integer.valueOf("2"));
		marryingPersonRepository.save(getMarryingPerson(), "ap.kurool");
	}

	@Test
	public void testUpdate() {
		when(jdbcTemplate.update(any(String.class), Matchers.<Object[]>any())).thenReturn(Integer.valueOf("2"));
		marryingPersonRepository.update(getMarryingPerson(), "ap.kurool");
	}

	public MarryingPerson getMarryingPerson() {
		MarryingPerson marryingPerson = new MarryingPerson();
		marryingPerson.setAadhaar("QBAXD46A");
		marryingPerson.setCity("Bangalore");
		marryingPerson.setDob(Long.valueOf("221947"));
		marryingPerson.setEducation("B.E");
		marryingPerson.setEmail("xyz@gmail.com");
		marryingPerson.setHandicapped("No");
		marryingPerson.setId(Long.valueOf("6"));
		marryingPerson.setLocality("Bellandur");
		marryingPerson.setMobileNo("9874563210");
		marryingPerson.setName("MarryingPerson");
		marryingPerson.setNationality("Indian");
		marryingPerson.setOccupation("Developer");
		marryingPerson.setParentName("ParentsName");
		marryingPerson.setPhoto("Yes");
		marryingPerson.setReligion(Long.valueOf("6"));
		marryingPerson.setReligionPractice("Hindu");
		marryingPerson.setResidenceAddress("No.10 D.No13 AKM, OutsideDoors, Bellandur, Bangalore");
		marryingPerson.setStatus(MaritalStatus.UNMARRIED);
		marryingPerson.setStreet("Bellandur");

		return marryingPerson;
	}
}
