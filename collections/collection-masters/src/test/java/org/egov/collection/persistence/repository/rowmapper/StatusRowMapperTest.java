package org.egov.collection.persistence.repository.rowmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.text.ParseException;

import org.egov.collection.domain.model.Status;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class StatusRowMapperTest {

	@Mock
	private ResultSet rs;

	@InjectMocks
	private StatusRowMapper statusRowMapper;

	@Test
	public void test_should_map_result_set_to_entity() throws Exception {

		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		when((Long) rs.getObject("id")).thenReturn(1L);
		when(rs.getString("code")).thenReturn("SUBMITTED");
		when(rs.getString("objecttype")).thenReturn("Receipt Header");
		when(rs.getString("description")).thenReturn("Submitted");
		when(rs.getString("tenantId")).thenReturn("default");
		when((Long) rs.getObject("createdBy")).thenReturn(1L);
		when((Long) rs.getObject("lastModifiedBy")).thenReturn(1L);
		try{
		when(rs.getTimestamp("createdDate")).thenReturn(null);
		when(rs.getTimestamp("lastModifiedDate")).thenReturn(null);
		}
		catch(Exception e)
		{
			Assert.fail("Exception " + e);
		}
		Status actualStatus = statusRowMapper.mapRow(rs, 1);
		Status expectedStatus = getModelStatus();
		assertThat(expectedStatus.equals(actualStatus));

	}

	private Status getModelStatus() {
		return Status.builder().id(1L).code("SUBMITTED").objectType("Receipt Header").description("Submitted")
				.tenantId("default").createdBy(1L).lastModifiedBy(1L).build();
	}

}
