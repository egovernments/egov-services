package org.egov.collection.persistence.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.egov.collection.domain.model.Status;
import org.egov.collection.domain.model.StatusCriteria;
import org.egov.collection.persistence.repository.builder.StatusQueryBuilder;
import org.egov.collection.persistence.repository.rowmapper.StatusRowMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class StatusRepositoryTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private StatusRowMapper statusRowMapper;

	@Mock
	private StatusQueryBuilder statusQueryBuilder;

	@InjectMocks
	private StatusRepository statusRepository;

	@Test
	public void test_should_get_all_statuses_as_Per_criteria() {
		when(statusQueryBuilder.getQuery(any(StatusCriteria.class), any(List.class))).thenReturn("");
		when(jdbcTemplate.query(any(String.class), any(Object[].class), any(StatusRowMapper.class)))
				.thenReturn(getStatusModel());
		List<Status> actualStatuses = statusRepository.getStatuses(getStatusCriteria());
		assertTrue(actualStatuses.get(0).equals(getStatusModel().get(0)));
	}

	private StatusCriteria getStatusCriteria() {
		return StatusCriteria.builder().code("SUBMITTED").objectType("ReceiptHeader").tenantId("default").build();
	}

	private List<Status> getStatusModel() {
		Status status = Status.builder().id(1L).code("SUBMITTED").description("Submitted").objectType("ReceiptHeader")
				.tenantId("default").createdBy(1L).lastModifiedBy(1L).build();
		return Arrays.asList(status);
	}

}
