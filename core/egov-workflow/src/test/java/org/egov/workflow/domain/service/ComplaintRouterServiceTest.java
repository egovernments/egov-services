package org.egov.workflow.domain.service;

import org.egov.workflow.persistence.entity.ComplaintRouter;
import org.egov.workflow.persistence.repository.*;
import org.egov.workflow.web.contract.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComplaintRouterServiceTest {

    private ComplaintRouterService complaintRouterService;

    @Mock
    private ComplaintRouterRepository complaintRouterRepository;

    @Mock
    private ComplaintTypeRepository complaintTypeRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private BoundaryRepository boundaryRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionHierarchyRepository positionHierarchyRepository;

    @Before
    public void before() {
        complaintRouterService = new ComplaintRouterService(complaintRouterRepository, boundaryRepository, positionRepository,
            complaintTypeRepository, employeeRepository, positionHierarchyRepository);
        final ComplaintTypeResponse complaintType = getComplaintType();
        final ComplaintRouter router = getComplaintRouter();
        when(complaintTypeRepository.fetchComplaintTypeByCode("C001", "tenantId")).thenReturn(complaintType);
        when(complaintRouterRepository.findByComplaintTypeAndBoundary(complaintType.getId(), 1L)).thenReturn(router);
        when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(router);
        when(complaintRouterRepository.findCityAdminGrievanceOfficer("ADMINISTRATION", "default")).thenReturn(router);
        when(positionHierarchyRepository.getByPositionByComplaintTypeAndFromPosition(2L, "C001", "tenantId")).thenReturn(getPositionHierarchies());
        when(positionRepository.getById(10L, "tenantId")).thenReturn(getPositions());
    }

    @Test
    public void test_should_return_assignee_for_complaint_from_router() {
        when(boundaryRepository.fetchBoundaryById(1L, "tenantId")).thenReturn(getBoundary());
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        when(positionRepository.getById(10L, "tenantId")).thenReturn(expectedPosition);
        final PositionResponse actualPosition = complaintRouterService.getAssignee(1L, "C001", null, "tenantId");
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_for_complaint_from_positionhierarcy() {
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(3L);
        when(positionRepository.getById(3L, "tenantId")).
            thenReturn(expectedPosition);
        final PositionResponse actualPosition = complaintRouterService.getAssignee(1L, "C001", 2L, "tenantId");
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_for_complaint_without_positionhierarchy() {
        when(positionHierarchyRepository.getByPositionByComplaintTypeAndFromPosition(2L, "C001", "tenantId")).thenReturn(getPositionHeirarchy());
        when(employeeRepository.getByRoleCode("GRO", "tenantId")).thenReturn(getEmployees());
        when(positionRepository.getById(10L, "tenantId")).thenReturn(getPositions());
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(1L, "C001", 2L, "tenantId");
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_only_with_complainttype_from_router() {
        when(boundaryRepository.fetchBoundaryById(1L, "tenantId")).thenReturn(new BoundaryResponse());
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(10L, "C001", null, "tenantId");
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_without_complainttype() {
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(null, "C001", null, "tenantId");
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_with_city_administrator() {
        final ComplaintRouter complaintRouter = new ComplaintRouter();
        complaintRouter.setBoundary(10L);
        complaintRouter.setPosition(10L);
        complaintRouter.setComplaintType(1L);
        when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(null);
        when(complaintRouterRepository.findCityAdminGrievanceOfficer("ADMINISTRATION", "tenantId")).thenReturn(complaintRouter);
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(null, "C001", null, "tenantId");
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_only_with_boundary() {
        final BoundaryResponse boundary = new BoundaryResponse();
        boundary.setName("Srinivas Nagar");
        boundary.setId(10L);
        when(boundaryRepository.fetchBoundaryById(10L, "tenantId")).thenReturn(boundary);
        final ComplaintRouter complaintRouter = new ComplaintRouter();
        complaintRouter.setBoundary(10L);
        complaintRouter.setPosition(10L);
        complaintRouter.setComplaintType(1L);
        when(complaintRouterRepository.findByOnlyBoundary(10L)).thenReturn(complaintRouter);
        when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(null);
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(10L, "C001", null, "tenantId");
        assertEquals(expectedPosition, actualPosition);

    }

    private ComplaintRouter getComplaintRouter() {
        final ComplaintRouter complaintRouter = new ComplaintRouter();
        complaintRouter.setBoundary(1L);
        complaintRouter.setPosition(10L);
        complaintRouter.setComplaintType(1L);
        return complaintRouter;
    }

    private ComplaintTypeResponse getComplaintType() {
        final ComplaintTypeResponse complaintType = new ComplaintTypeResponse();
        complaintType.setId(1L);
        complaintType.setServiceName("Water Problem");
        return complaintType;
    }

    private BoundaryResponse getBoundary() {
        final BoundaryResponse boundary = new BoundaryResponse();
        boundary.setName("Srinivas Nagar");
        boundary.setId(1L);
        final BoundaryResponse parent = new BoundaryResponse();
        parent.setId(30L);
        parent.setName("Kurnool Municipality");
        boundary.setParent(parent);
        return boundary;
    }

    private PositionHierarchyResponse getPositionHierarchies() {
        List<EscalationHierarchy> escalationHierarchies = new ArrayList<>();
        EscalationHierarchy escalationHierarchy = EscalationHierarchy.builder()
            .fromPosition(2L)
            .toPosition(3L)
            .build();
        escalationHierarchies.add(escalationHierarchy);
        return PositionHierarchyResponse.builder()
            .escalationHierarchies(escalationHierarchies)
            .build();
    }

    private List<Employee> getEmployees() {
        List<Assignment> assignments = new ArrayList<Assignment>();
        Assignment assignment = Assignment.builder().id(1L).isPrimary(Boolean.TRUE).department(1L).position(10L).build();
        assignments.add(assignment);

        final List<Employee> employees = new ArrayList<Employee>();
        final Employee employee = new Employee();
        employee.setCode("101010");
        employee.setId(1L);
        employee.setName("narasappa");
        employee.setUsername("egovernments");
        employee.setTenantId("tenantId");
        employee.setAssignments(assignments);

        employees.add(employee);
        return employees;
    }

    private PositionResponse getPositions() {
        final PositionResponse position = new PositionResponse();
        position.setId(10L);
        position.setName("Grievence_Officer_1");
        return position;
    }

    private PositionHierarchyResponse getPositionHeirarchy() {
        return PositionHierarchyResponse.builder().responseInfo(ResponseInfo.builder().build())
            .escalationHierarchies(new ArrayList<EscalationHierarchy>() {
            })
            .build();
    }

}
