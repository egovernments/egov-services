package org.egov.collection.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.egov.collection.domain.model.Status;
import org.egov.collection.domain.model.StatusCriteria;
import org.egov.collection.persistence.repository.StatusRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(StatusService.class)
@WebAppConfiguration
public class StatusServiceTest {

	@Mock
	StatusRepository statusRepository;
	@InjectMocks
	StatusService statusService;

	@Test
	public void test_should_get_all_status_by_criteria() {

		when(statusRepository.getStatuses(getStatusCriteria())).thenReturn(getStatusModel());

		List<Status> statusModel = statusService.getStatuses(getStatusCriteria());
		assertThat(statusModel.get(0).getCode()).isEqualTo(getStatusModel().get(0).getCode());

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
