package org.egov.mr.repository.rowmapper;

import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarriageRegnIdsRowMapperTest {

	@Mock
	ResultSet rs;

	@InjectMocks
	private MarriageRegnIdsRowMapper marriageRegnIdsRowMapper;

	@Test
	public void test() throws Exception {
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);

		when(rs.getObject("mr_applicationnumber")).thenReturn("A15B20C");
		String value = marriageRegnIdsRowMapper.mapRow(rs, 1);
		assertEquals("A15B20C", value);
	}

}
