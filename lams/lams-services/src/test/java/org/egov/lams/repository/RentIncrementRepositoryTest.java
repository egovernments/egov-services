package org.egov.lams.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.repository.RentIncrementRepository;
import org.egov.lams.repository.rowmapper.RentIncrementRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RentIncrementRepositoryTest {

	@Mock
	JdbcTemplate jdbcTemplate;

	@InjectMocks
	private RentIncrementRepository rentIncrementRepository;

	@Test
	public void getRentIncrementsTest() {
		List<RentIncrementType> rentIncrements = new ArrayList<>();
		RentIncrementType rentIncrementType = new RentIncrementType();
		rentIncrementType.setId(1l);
		rentIncrements.add(rentIncrementType);

		when(jdbcTemplate.query(any(String.class), any(RentIncrementRowMapper.class))).thenReturn(rentIncrements);
		assertTrue(rentIncrements.equals(rentIncrementRepository.getRentIncrements()));
	}

	@Test
	public void getRentIncrementByIdTest() {

		RentIncrementType rentIncrementType = new RentIncrementType();
		rentIncrementType.setId(1l);
		Long rentId = 1l;

		when(jdbcTemplate.queryForObject(any(String.class), any(Object[].class), any(RentIncrementRowMapper.class)))
				.thenReturn(rentIncrementType);
		assertTrue(rentIncrementType.equals(rentIncrementRepository.getRentIncrementById(rentId)));
	}
}
