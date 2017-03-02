package org.egov.workflow.domain.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.workflow.persistence.entity.ComplaintRouter;
import org.egov.workflow.persistence.repository.BoundaryRepository;
import org.egov.workflow.persistence.repository.ComplaintRouterRepository;
import org.egov.workflow.persistence.repository.ComplaintTypeRepository;
import org.egov.workflow.persistence.repository.EmployeeRepository;
import org.egov.workflow.persistence.repository.PositionHierarchyRepository;
import org.egov.workflow.persistence.repository.PositionRepository;
import org.egov.workflow.web.contract.BoundaryResponse;
import org.egov.workflow.web.contract.ComplaintTypeResponse;
import org.egov.workflow.web.contract.Employee;
import org.egov.workflow.web.contract.PositionHierarchyResponse;
import org.egov.workflow.web.contract.PositionResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        when(complaintTypeRepository.fetchComplaintTypeByCode("C001")).thenReturn(complaintType);
        when(complaintRouterRepository.findByComplaintTypeAndBoundary(complaintType.getId(), 1L)).thenReturn(router);
        when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(router);
        when(complaintRouterRepository.findCityAdminGrievanceOfficer("ADMINISTRATION")).thenReturn(router);
        when(positionHierarchyRepository.getByObjectTypeObjectSubTypeAndFromPosition("Complaint", "C001", 2L))
                .thenReturn(getPositionHierarchies());
        when(positionRepository.getByEmployeeCode("101010")).thenReturn(getPositions());
        when(positionRepository.getById(10L)).thenReturn(getPositions().get(0));
    }

    @Test
    public void test_should_return_assignee_for_complaint_from_router() {
        when(boundaryRepository.fetchBoundaryById(1L)).thenReturn(getBoundary());
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        when(positionRepository.getById(10L)).thenReturn(expectedPosition);
        final PositionResponse actualPosition = complaintRouterService.getAssignee(1L, "C001", null);
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_for_complaint_from_positionhierarcy() {
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(3L);
        expectedPosition.setName("Grievence_Officer_1");
        when(positionRepository.getById(3L)).thenReturn(expectedPosition);
        final PositionResponse actualPosition = complaintRouterService.getAssignee(1L, "C001", 2L);
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_for_complaint_without_positionhierarchy() {
        when(employeeRepository.getByRoleName("Grievance Routing Officer")).thenReturn(getEmplyees());
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(1L, "C001", 30L);
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_only_with_complainttype_from_router() {
        when(boundaryRepository.fetchBoundaryById(1L)).thenReturn(new BoundaryResponse());
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(10L, "C001", null);
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_without_complainttype() {
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(null, "C001", null);
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_with_city_administrator() {
        when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(null);
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(null, "C001", null);
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void test_should_return_assignee_only_with_boundary() {
        final BoundaryResponse boundary = new BoundaryResponse();
        boundary.setName("Srinivas Nagar");
        boundary.setId(10L);
        when(boundaryRepository.fetchBoundaryById(10L)).thenReturn(boundary);
        final ComplaintRouter complaintRouter = new ComplaintRouter();
        complaintRouter.setBoundary(10L);
        complaintRouter.setPosition(10L);
        complaintRouter.setComplaintType(1L);
        when(complaintRouterRepository.findByOnlyBoundary(10L)).thenReturn(complaintRouter);
        when(complaintRouterRepository.findByOnlyComplaintType(1L)).thenReturn(null);
        final PositionResponse expectedPosition = new PositionResponse();
        expectedPosition.setId(10L);
        expectedPosition.setName("Grievence_Officer_1");
        final PositionResponse actualPosition = complaintRouterService.getAssignee(10L, "C001", null);
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

    private List<PositionHierarchyResponse> getPositionHierarchies() {
        final List<PositionHierarchyResponse> positionsHirarchies = new ArrayList<PositionHierarchyResponse>();
        final PositionHierarchyResponse positionHierarchy = new PositionHierarchyResponse();
        final PositionResponse fromPosition = new PositionResponse();
        fromPosition.setId(2L);
        fromPosition.setName("Accounts_Officer_1");
        positionHierarchy.setFromPosition(fromPosition);
        final PositionResponse toPosition = new PositionResponse();
        toPosition.setId(3L);
        toPosition.setName("Grievence_Officer_1");
        positionHierarchy.setToPosition(toPosition);
        positionHierarchy.setId(1L);
        positionsHirarchies.add(positionHierarchy);
        return positionsHirarchies;
    }

    private List<Employee> getEmplyees() {
        final List<Employee> employees = new ArrayList<Employee>();
        final Employee employee = new Employee();
        employee.setCode("101010");
        employee.setId(1L);
        employee.setName("narasappa");
        employee.setUsername("egovernments");
        employees.add(employee);
        return employees;
    }

    private List<PositionResponse> getPositions() {
        final List<PositionResponse> positions = new ArrayList<PositionResponse>();
        final PositionResponse position = new PositionResponse();
        position.setId(10L);
        position.setName("Grievence_Officer_1");
        positions.add(position);
        return positions;
    }

}
