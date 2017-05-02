package org.egov.eis.repository.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.config.ApplicationProperties;
import org.egov.eis.web.contract.HODEmployeeCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HODEmployeeQueryBuilderTest {

	@Mock
	private ApplicationProperties applicationProperties;
	
	@InjectMocks
	private HODEmployeeQueryBuilder hodEmployeeQueryBuilder;
	
	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void testEmptyQuery() {
		Mockito.doReturn("3").when(applicationProperties).empSearchPageSizeDefault();
		HODEmployeeCriteria hodEmployeeCriteria = new HODEmployeeCriteria().builder().tenantId("1").build();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = hodEmployeeQueryBuilder.getQueryForListOfHODEmployeeIds(hodEmployeeCriteria, preparedStatementValues);
		String expectedQueryString = "SELECT distinct a.employeeId AS id  FROM egeis_assignment a JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = a.tenantId WHERE a.tenantId = ? LIMIT ? OFFSET ?";
	
		assertEquals(queryString, expectedQueryString);
		assertEquals((String)preparedStatementValues.get(0), "1"); // tenant id is 1
		assertEquals((long)preparedStatementValues.get(1), 3); // limit is 3
		assertEquals((long)preparedStatementValues.get(2), 0); // offset is 0
	}
	
	@Test
	public void testQueryForDepartmentId() {
		Mockito.doReturn("3").when(applicationProperties).empSearchPageSizeDefault();
		List<Long> ids = new ArrayList();
		ids.add(100L);
		ids.add(200L);
		HODEmployeeCriteria hodEmployeeCriteria = new HODEmployeeCriteria().builder().tenantId("1").departmentId(10L).build();
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = hodEmployeeQueryBuilder.getQueryForListOfHODEmployeeIds(hodEmployeeCriteria, preparedStatementValues);
		String expectedQueryString = "SELECT distinct a.employeeId AS id  FROM egeis_assignment a JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = a.tenantId WHERE a.tenantId = ? AND hod.departmentId = ? LIMIT ? OFFSET ?";

		assertEquals(queryString, expectedQueryString);
		assertEquals((String)preparedStatementValues.get(0), "1"); // tenant id is 1
		assertEquals((long)preparedStatementValues.get(1), 10L); // department id
		assertEquals((long)preparedStatementValues.get(2), 3); // limit is 3
		assertEquals((long)preparedStatementValues.get(3), 0); // offset is 0
	}
	
	@Test
	public void testQueryWithAllFields() {
		Mockito.doReturn("3").when(applicationProperties).empSearchPageSizeDefault();
		HODEmployeeCriteria hodEmployeeCriteria = new HODEmployeeCriteria().builder().tenantId("1").departmentId(10L).asOnDate(new Date()).sortOrder("DESC").build();
		List<Long> empIds = new ArrayList();
		empIds.add(100L);
		empIds.add(200L);
		List<Object> preparedStatementValues = new ArrayList<>();

		String queryString = hodEmployeeQueryBuilder.getQuery(hodEmployeeCriteria, preparedStatementValues, empIds);
		String expectedQueryString = "SELECT e.id AS e_id, e.code AS e_code, e.employeeStatus AS e_employeeStatus, e.employeeTypeId AS e_employeeTypeId, e.bankId AS e_bankId, e.bankBranchId AS e_bankBranchId, e.bankAccount AS e_bankAccount, e.tenantId AS e_tenantId, a.id AS a_id, a.positionId AS a_positionId, a.fundId AS a_fundId, a.functionaryId AS a_functionaryId, a.functionId AS a_functionId, a.designationId AS a_designationId, a.departmentId AS a_departmentId, a.isPrimary AS a_isPrimary, a.fromDate AS a_fromDate, a.toDate AS a_toDate, a.gradeId AS a_gradeId, a.govtOrderNumber AS a_govtOrderNumber, a.createdBy AS a_createdBy, a.createdDate AS a_createdDate, a.lastModifiedBy AS a_lastModifiedBy, a.lastModifiedDate AS a_lastModifiedDate, a.employeeId AS a_employeeId, hod.id AS hod_id, hod.departmentId AS hod_departmentId, ej.jurisdictionId AS ej_jurisdictionId FROM egeis_employee e JOIN egeis_assignment a ON e.id = a.employeeId AND a.tenantId = e.tenantId JOIN egeis_hodDepartment hod ON a.id = hod.assignmentId AND hod.tenantId = e.tenantId LEFT JOIN egeis_employeeJurisdictions ej ON e.id = ej.employeeId AND ej.tenantId = e.tenantId WHERE e.tenantId = ? AND e.id IN (100, 200) AND hod.departmentId = ? AND ? BETWEEN a.fromDate AND a.toDate ORDER BY e.id DESC";
		
		assertEquals(queryString, expectedQueryString);
		assertEquals((String)preparedStatementValues.get(0), "1"); // tenant id is 1
		assertEquals((long)preparedStatementValues.get(1), 10L); // department id
	}
}
