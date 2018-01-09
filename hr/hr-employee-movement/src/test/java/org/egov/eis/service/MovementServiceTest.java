package org.egov.eis.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Movement;
import org.egov.eis.model.PromotionBasis;
import org.egov.eis.model.WorkFlowDetails;
import org.egov.eis.model.enums.TypeOfMovement;
import org.egov.eis.repository.MovementRepository;
import org.egov.eis.util.ApplicationConstants;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class MovementServiceTest {

    @InjectMocks
    private MovementService movementService;

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private PositionService positionService;

    @Mock
    private ApplicationConstants applicationConstants;

    @Mock
    private PropertiesManager propertiesManager;

    private List<Movement> movements;

    private List<HRStatus> hrStatuses;

    private List<Long> positions;

    private RequestInfo requestInfo;

    private ResponseInfo responseInfo;

    private WorkFlowDetails workFlowDetails;

    private PromotionBasis promotionBasis;

    private EmployeeInfo employee;

    @Before
    public void before() throws JsonProcessingException {
        hrStatuses = new ArrayList<>();
        HRStatus hrStatus1 = new HRStatus();
        HRStatus hrStatus2 = new HRStatus();
        hrStatus1.setId(1L);
        hrStatus2.setId(2L);
        hrStatuses.add(hrStatus1);
        hrStatuses.add(hrStatus2);

        positions = new ArrayList<>();
        positions.add(1L);
        positions.add(2L);

        requestInfo = new RequestInfo();
        responseInfo = new ResponseInfo();

        when(employeeService.getHRStatuses(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyString(),
                Mockito.any(RequestInfo.class))).thenReturn(hrStatuses);
        when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("");
        when(responseInfoFactory.createResponseInfoFromRequestInfo(Mockito.any(RequestInfo.class), Mockito.anyBoolean()))
                .thenReturn(responseInfo);
        when(positionService.getPositions(Mockito.any(Movement.class), Mockito.any(RequestInfo.class))).thenReturn(positions);
        when(applicationConstants.getErrorMessage(Mockito.anyString())).thenReturn("Error");
        when(propertiesManager.getHrMastersServiceStatusesKey()).thenReturn("");
    }

    @Test
    public void test_movement_service_should_return_movements() {
        Movement movement1 = new Movement();
        Movement movement2 = new Movement();
        workFlowDetails = new WorkFlowDetails();
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement1.setWorkflowDetails(workFlowDetails);
        movement2.setId(2L);
        movement2.setEmployeeId(2L);
        movement2.setTypeOfMovement(TypeOfMovement.TRANSFER);
        movement2.setWorkflowDetails(workFlowDetails);
        movements = new ArrayList<>();
        movements.add(movement1);
        movements.add(movement2);
        when(movementRepository.findForCriteria(Mockito.any(MovementSearchRequest.class), Mockito.any(RequestInfo.class)))
                .thenReturn(movements);
        final MovementSearchRequest movementSearchRequest = new MovementSearchRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final List<Movement> response = movementService.getMovements(movementSearchRequest, requestInfo);

        assertEquals(movements.size(), response.size());
    }

    @Test
    public void test_movement_service_should_create_movement() {
        Movement movement1 = new Movement();
        workFlowDetails = new WorkFlowDetails();
        promotionBasis = new PromotionBasis();
        promotionBasis.setId(1L);
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement1.setWorkflowDetails(workFlowDetails);
        movement1.setTenantId("default");
        movement1.setPromotionBasis(promotionBasis);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);
        employee = new EmployeeInfo();
        employee.setTenantId("default");
        employee.setId(1L);
        when(employeeService.getEmployee(movement1,movementRequest.getRequestInfo())).thenReturn(employee);
        //ResponseEntity<?> responseEntity = movementService.createMovements(movementRequest, null);
       // final MovementResponse movementResponse = (MovementResponse) responseEntity.getBody();
       // assertEquals(movementResponse.getMovement().size(), movementRequest.getMovement().size());
    }

    @Test
    public void test_movement_service_should_fail_to_create_movement() {
        Movement movement1 = new Movement();
        workFlowDetails = new WorkFlowDetails();
        workFlowDetails.setAction("Approve");
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement1.setWorkflowDetails(workFlowDetails);
        movement1.setPositionAssigned(1L);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);
        
        //ResponseEntity<?> responseEntity = movementService.createMovements(movementRequest, null);
        //final MovementResponse movementResponse = (MovementResponse) responseEntity.getBody();
        
        //assertNotNull(movementResponse.getMovement().get(0).getErrorMsg());
    }
    
    @Test
    public void test_movement_service_repository_should_create_movement() {
        Movement movement1 = new Movement();
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setPositionAssigned(1L);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);
        
        when(movementRepository.saveMovement(Mockito.any(MovementRequest.class))).thenReturn(movementRequest);
        
        final MovementRequest result = movementService.create(movementRequest);
        
        assertEquals(result.getMovement().size(), movementRequest.getMovement().size());
    }
    
    @Test
    public void test_movement_service_should_update_movement() {
        Movement movement1 = new Movement();
        workFlowDetails = new WorkFlowDetails();
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement1.setWorkflowDetails(workFlowDetails);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);
        
        when(movementRepository.findForCriteria(Mockito.any(MovementSearchRequest.class), Mockito.any(RequestInfo.class)))
                .thenReturn(movements);

        //ResponseEntity<?> responseEntity = movementService.updateMovement(movementRequest);
       // final MovementResponse movementResponse = (MovementResponse) responseEntity.getBody();
       // assertEquals(movementResponse.getMovement().size(), movementRequest.getMovement().size());
    }

    @Test
    public void test_movement_service_should_fail_to_update_movement() {
        Movement movement1 = new Movement();
        workFlowDetails = new WorkFlowDetails();
        workFlowDetails.setAction("Approve");
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement1.setWorkflowDetails(workFlowDetails);
        movement1.setPositionAssigned(1L);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);
        
       // ResponseEntity<?> responseEntity = movementService.createMovements(movementRequest, null);
       // final MovementResponse movementResponse = (MovementResponse) responseEntity.getBody();
        
       // assertNotNull(movementResponse.getMovement().get(0).getErrorMsg());
    }
    
    @Test
    public void test_movement_service_repository_should_update_movement() {
        Movement movement1 = new Movement();
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setPositionAssigned(1L);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);
        
        when(movementRepository.updateMovement(Mockito.any(MovementRequest.class))).thenReturn(movement1);
        
        final Movement result = movementService.update(movementRequest);
        
        assertEquals(result.getId(), movementRequest.getMovement().get(0).getId());
    }
}