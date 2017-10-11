package org.egov.mr.repository;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.egov.mr.model.Witness;
import org.egov.mr.model.enums.RelatedTo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class WitnessRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private WitnessRepository witnessRepository;

	@Test
	public void testSave() {
		when(jdbcTemplate.update(any(String.class), Matchers.<Object[]>any())).thenReturn(1);
		witnessRepository.save("2", "ap.kurnool", getWitness());
	}

	@Test
	public void testUpdate() {
		when(jdbcTemplate.update(any(String.class), Matchers.<Object[]>any())).thenReturn(1);
		witnessRepository.update("2", "ap.kurnool", getWitness());
	}

	@Test
	public void testDelete() {
		when(jdbcTemplate.update(any(String.class), any(String.class), any(String.class))).thenReturn(1);
		witnessRepository.delete("2", "ap.kurnool");
	}

	public Witness getWitness() {
		Witness witness = new Witness();
		witness.setAadhaar("BQHX24PQ");
		witness.setAddress("No.10, D.No 13 Plaza, Outdoor site,Bangalore");
		witness.setDob(Long.valueOf("24"));
		witness.setEmail("abc@gmail.com");
		witness.setMobileNo("9874563210");
		witness.setName("witness");
		witness.setOccupation("Developer");
		witness.setRelatedTo(RelatedTo.BRIDE);
		witness.setRelationForIdentification("Uncle");
		witness.setRelationshipWithApplicants("Uncle");
		witness.setWitnessNo(Integer.valueOf("6"));
		return witness;
	}
}
