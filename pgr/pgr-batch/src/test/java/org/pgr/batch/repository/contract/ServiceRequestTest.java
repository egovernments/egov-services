package org.pgr.batch.repository.contract;

import org.egov.pgr.common.contract.AttributeEntry;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.pgr.batch.repository.contract.ServiceRequest.PREVIOUS_ASSIGNEE;
import static org.pgr.batch.repository.contract.ServiceRequest.VALUES_POSITION_ID;

public class ServiceRequestTest {

	@Test
	public void test_should_return_true_when_current_assignee_is_same_as_previous_assignee() {
		final ServiceRequest serviceRequest = ServiceRequest.builder()
				.attribValues(Arrays.asList(
						new AttributeEntry(VALUES_POSITION_ID, "1"),
						new AttributeEntry(PREVIOUS_ASSIGNEE, "1")))
				.build();

		assertTrue(serviceRequest.isNewAssigneeSameAsPreviousAssignee());
	}

	@Test
	public void test_should_return_false_when_current_assignee_is_different_from_previous_assignee() {
		final ServiceRequest serviceRequest = ServiceRequest.builder()
				.attribValues(Arrays.asList(
						new AttributeEntry(VALUES_POSITION_ID, "1"),
						new AttributeEntry(PREVIOUS_ASSIGNEE, "2")))
				.build();

		assertFalse(serviceRequest.isNewAssigneeSameAsPreviousAssignee());
	}

}