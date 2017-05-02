package org.egov.eis.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.PositionGetRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PositionAssignmentQueryBuilderTest {

	@Mock
	private ApplicationProperties applicationProperties;
	
	@InjectMocks
	private PositionAssignmentQueryBuilder positionAssignmentQueryBuilder;
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testQueryWithEmployeeIdAndTenantId() {
		Mockito.doReturn("3").when(applicationProperties).empSearchPageSizeDefault();
		PositionGetRequest pr = new PositionGetRequest().builder().tenantId("1").build();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = positionAssignmentQueryBuilder.getQuery(100L, pr, preparedStatementValues);
		String expectedQueryString = "SELECT distinct positionId FROM egeis_assignment WHERE employeeId = ? AND tenantId = ? LIMIT ? OFFSET ?";
		
		assertEquals(queryString, expectedQueryString);
		assertEquals((long)preparedStatementValues.get(0), 100); // employee id is 100
		assertEquals((String)preparedStatementValues.get(1), "1"); // tenant id is 1
		assertEquals((long)preparedStatementValues.get(2), 3); // limit is 3
		assertEquals((long)preparedStatementValues.get(3), 0); // offset is 0
	}
	
	@Test
	public void testQueryWithPositionIdAndIsPrimary() {
		Mockito.doReturn("2").when(applicationProperties).empSearchPageSizeDefault();
		
		PositionGetRequest pr = getPositionRequestWithIds();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = positionAssignmentQueryBuilder.getQuery(100L, pr, preparedStatementValues);
		String expectedQueryString = "SELECT distinct positionId FROM egeis_assignment WHERE employeeId = ? AND tenantId = ? AND positionId IN  (10, 20) AND isPrimary = ? LIMIT ? OFFSET ?";
		
		assertEquals(queryString, expectedQueryString);
		assertEquals((long)preparedStatementValues.get(0), 100); // employee id is 100
		assertEquals((String)preparedStatementValues.get(1), "1"); // tenant id is 1
		assertEquals((boolean)preparedStatementValues.get(2), true); //  isPrimary is true
		assertEquals((long)preparedStatementValues.get(3), 2); // limit is 2
		assertEquals((long)preparedStatementValues.get(4), 0); // offset is 0
	}
	
	@Test
	public void testQueryWithAllFields() {
		Mockito.doReturn("2").when(applicationProperties).empSearchPageSizeDefault();
		
		PositionGetRequest pr = getPositionRequestWithAllFields();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = positionAssignmentQueryBuilder.getQuery(100L, pr, preparedStatementValues);
		String expectedQueryString = "SELECT distinct positionId FROM egeis_assignment WHERE employeeId = ? AND tenantId = ? AND positionId IN  (10, 20) AND isPrimary = ? AND ? BETWEEN fromDate AND toDate AND departmentId = ? AND designationId = ? LIMIT ? OFFSET ?";

		assertEquals(queryString, expectedQueryString);
		assertEquals((long)preparedStatementValues.get(0), 100); // employee id is 100
		assertEquals((String)preparedStatementValues.get(1), "1"); // tenant id is 1
		assertEquals((boolean)preparedStatementValues.get(2), true); //  isPrimary is true
		assertEquals((long)preparedStatementValues.get(4), 5); // department id is 0
		assertEquals((long)preparedStatementValues.get(5), 5); // designation id is 0
		assertEquals((long)preparedStatementValues.get(6), 5); // limit is 5
		assertEquals((long)preparedStatementValues.get(7), 20); // offset is 20
	}

	private PositionGetRequest getPositionRequestWithAllFields() {
		PositionGetRequest pr = new PositionGetRequest();
		List<Long> positionIds = new ArrayList<>();
		positionIds.add(10L);
		positionIds.add(20L);
		pr.setId(positionIds);
		pr.setTenantId("1");
		pr.setIsPrimary(true);
		pr.setAsOnDate(new Date());
		pr.setDesignationId(5L);
		pr.setDepartmentId(5L);
		pr.setPageNumber((short)5);
		pr.setPageSize((short)5);
		return pr;
	}

	private PositionGetRequest getPositionRequestWithIds() {
		PositionGetRequest pr = new PositionGetRequest();
		List<Long> positionIds = new ArrayList<>();
		positionIds.add(10L);
		positionIds.add(20L);
		pr.setId(positionIds);
		pr.setTenantId("1");
		pr.setIsPrimary(true);
		return pr;
	}
}
