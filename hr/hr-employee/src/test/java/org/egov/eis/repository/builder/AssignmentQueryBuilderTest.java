package org.egov.eis.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.AssignmentGetRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AssignmentQueryBuilderTest {

	@Mock
	private ApplicationProperties applicationProperties;
	
	@InjectMocks
	private AssignmentQueryBuilder assignmentQueryBuilder;
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testQueryWithEmployeeIdAndTenantId() {
		Mockito.doReturn("3").when(applicationProperties).empSearchPageSizeDefault();
		AssignmentGetRequest ar = new AssignmentGetRequest().builder().tenantId("1").build();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = assignmentQueryBuilder.getQuery(100L, ar, preparedStatementValues);
		String expectedQueryString = "SELECT a.id as a_id, a.positionId as a_positionId, a.fundId as a_fundId, a.functionaryId as a_functionaryId, a.functionId as a_functionId, a.designationId as a_designationId, a.departmentId as a_departmentId, a.isPrimary as a_isPrimary, a.fromDate as a_fromDate, a.toDate as a_toDate, a.gradeId as a_gradeId, a.govtOrderNumber as a_govtOrderNumber, a.createdBy as a_createdBy, a.createdDate as a_createdDate, a.lastModifiedBy as a_lastModifiedBy, a.lastModifiedDate as a_lastModifiedDate, a.tenantId as a_tenantId, hod.id as hod_id, hod.departmentId as hod_departmentId FROM egeis_employee e JOIN egeis_assignment a ON a.employeeId = e.id LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = ? WHERE e.id = ? AND a.tenantId = ? ORDER BY a.fromDate DESC LIMIT ? OFFSET ?";
		
		assertEquals(queryString, expectedQueryString);
		assertEquals((String)preparedStatementValues.get(0), "1"); // tenant id is 1
		assertEquals((long)preparedStatementValues.get(1), 100); // employee id is 100
		assertEquals((long)preparedStatementValues.get(3), 3); // limit is 3
		assertEquals((long)preparedStatementValues.get(4), 0); // offset is 0
	}
	
	@Test
	public void testQueryWithAssignmentIdAndIsPrimary() {
		Mockito.doReturn("2").when(applicationProperties).empSearchPageSizeDefault();
		
		AssignmentGetRequest ar = getAssignmentRequestWithIds();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = assignmentQueryBuilder.getQuery(100L, ar, preparedStatementValues);
		String expectedQueryString = "SELECT a.id as a_id, a.positionId as a_positionId, a.fundId as a_fundId, a.functionaryId as a_functionaryId, a.functionId as a_functionId, a.designationId as a_designationId, a.departmentId as a_departmentId, a.isPrimary as a_isPrimary, a.fromDate as a_fromDate, a.toDate as a_toDate, a.gradeId as a_gradeId, a.govtOrderNumber as a_govtOrderNumber, a.createdBy as a_createdBy, a.createdDate as a_createdDate, a.lastModifiedBy as a_lastModifiedBy, a.lastModifiedDate as a_lastModifiedDate, a.tenantId as a_tenantId, hod.id as hod_id, hod.departmentId as hod_departmentId FROM egeis_employee e JOIN egeis_assignment a ON a.employeeId = e.id LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = ? WHERE e.id = ? AND a.tenantId = ? AND a.id IN (10, 20) AND a.isPrimary = ? ORDER BY a.fromDate DESC LIMIT ? OFFSET ?";

		assertEquals(queryString, expectedQueryString);
		assertEquals((String)preparedStatementValues.get(0), "1"); // tenant id is 1
		assertEquals((long)preparedStatementValues.get(1), 100); // employee id is 100
		assertEquals((boolean)preparedStatementValues.get(3), true); //  isPrimary is true
		assertEquals((long)preparedStatementValues.get(4), 2); // limit is 2
		assertEquals((long)preparedStatementValues.get(5), 0); // offset is 0
	}
	
	@Test
	public void testQueryWithPagingInfo() {
		Mockito.doReturn("2").when(applicationProperties).empSearchPageSizeDefault();
		
		AssignmentGetRequest ar = getAssignmentRequestWithPagingInfo();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = assignmentQueryBuilder.getQuery(100L, ar, preparedStatementValues);
		String expectedQueryString = "SELECT a.id as a_id, a.positionId as a_positionId, a.fundId as a_fundId, a.functionaryId as a_functionaryId, a.functionId as a_functionId, a.designationId as a_designationId, a.departmentId as a_departmentId, a.isPrimary as a_isPrimary, a.fromDate as a_fromDate, a.toDate as a_toDate, a.gradeId as a_gradeId, a.govtOrderNumber as a_govtOrderNumber, a.createdBy as a_createdBy, a.createdDate as a_createdDate, a.lastModifiedBy as a_lastModifiedBy, a.lastModifiedDate as a_lastModifiedDate, a.tenantId as a_tenantId, hod.id as hod_id, hod.departmentId as hod_departmentId FROM egeis_employee e JOIN egeis_assignment a ON a.employeeId = e.id LEFT JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = ? WHERE e.id = ? AND a.tenantId = ? AND a.id IN (10, 20) AND a.isPrimary = ? ORDER BY a.fromDate DESC LIMIT ? OFFSET ?";

		assertEquals(queryString, expectedQueryString);
		assertEquals((String)preparedStatementValues.get(0), "1"); // tenant id is 1
		assertEquals((long)preparedStatementValues.get(1), 100); // employee id is 100
		assertEquals((boolean)preparedStatementValues.get(3), true); //  isPrimary is true
		assertEquals((long)preparedStatementValues.get(4), 5); // limit is 5
		assertEquals((long)preparedStatementValues.get(5), 20); // offset is 20
	}

	private AssignmentGetRequest getAssignmentRequestWithPagingInfo() {
		AssignmentGetRequest ar = new AssignmentGetRequest();
		List<Long> assignmentIds = new ArrayList<>();
		assignmentIds.add(10L);
		assignmentIds.add(20L);
		ar.setTenantId("1");
		ar.setAssignmentId(assignmentIds);
		ar.setIsPrimary(true);
		ar.setPageNumber((short) 5);
		ar.setPageSize((short) 5);
		return ar;
	}

	private AssignmentGetRequest getAssignmentRequestWithIds() {
		AssignmentGetRequest ar = new AssignmentGetRequest();
		List<Long> assignmentId = new ArrayList<>();
		assignmentId.add(10L);
		assignmentId.add(20L);
		ar.setTenantId("1");
		ar.setAssignmentId(assignmentId);
		ar.setIsPrimary(true);
		return ar;
	}
}
