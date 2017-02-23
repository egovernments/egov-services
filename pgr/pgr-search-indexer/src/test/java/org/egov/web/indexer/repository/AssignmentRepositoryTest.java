package org.egov.web.indexer.repository;

import static org.junit.Assert.assertEquals;

import org.egov.web.indexer.contract.Assignment;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class AssignmentRepositoryTest {
	private AssignmentRepository assignmentRepository;
	private MockRestServiceServer server;

	@Before
	public void before() {
		RestTemplate restTemplate = new RestTemplate();
		assignmentRepository = new AssignmentRepository();
		server = MockRestServiceServer.bindTo(restTemplate).build();
	}

	@Test
	public void test_should_fetch_assignment_for_given_id() throws Exception {
		Assignment assignment = assignmentRepository.fetchAssignmentById(1L);
		server.verify();
		assertEquals(Long.valueOf(1), assignment.getId());
		assertEquals("Raman", assignment.getName());
	}

}
