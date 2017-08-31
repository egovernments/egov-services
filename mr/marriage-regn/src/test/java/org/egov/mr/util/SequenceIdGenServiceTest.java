package org.egov.mr.util;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.mr.web.errorhandler.FieldError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class SequenceIdGenServiceTest {

	@InjectMocks
	private SequenceIdGenService sequenceIdGenService;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testIdSeqGen() {
		List<Long> ids = new ArrayList<>();
		ids.add(Long.valueOf("2"));
		ids.add(Long.valueOf("6"));
		when(jdbcTemplate.queryForList(Matchers.any(String.class), Matchers.<Object[]>any(),
				Matchers.<Class<Long>>any())).thenReturn(ids);
		assertEquals(sequenceIdGenService.idSeqGen(1, "seq_name"), ids);
	}

	@Test
	public void testGetIds() {
		List<String> ids = new ArrayList<>();
		ids.add("2");
		ids.add("6");
		when(jdbcTemplate.queryForList(any(String.class), Matchers.<Object[]>any(), Matchers.<Class<String>>any()))
				.thenReturn(ids);
		assertEquals(sequenceIdGenService.getIds(1, "seq_name"), ids);
	}

}
