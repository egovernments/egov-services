package org.egov.user.domain.model;

import org.egov.user.domain.exception.InvalidUserSearchCriteriaException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserSearchCriteriaTest {

	@Test
	public void test_should_not_throw_exception_when_search_criteria_is_valid() {
		final UserSearchCriteria searchCriteria = UserSearchCriteria.builder()
				.tenantId("tenantId")
				.build();
		
		searchCriteria.validate();
		assertFalse(searchCriteria.isTenantIdAbsent());
	}

	@Test(expected = InvalidUserSearchCriteriaException.class)
	public void test_should_throw_exception_when_tenant_id_is_not_present() {
		final UserSearchCriteria searchCriteria = UserSearchCriteria.builder()
				.tenantId(null)
				.build();

		searchCriteria.validate();
	}

	@Test
	public void test_should_return_true_when_tenant_id_is_not_present() {
		final UserSearchCriteria searchCriteria = UserSearchCriteria.builder()
				.tenantId(null)
				.build();

		assertTrue(searchCriteria.isTenantIdAbsent());
	}

}