package org.egov.eis.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Movement;
import org.egov.eis.model.PromotionBasis;
import org.egov.eis.model.TransferReason;
import org.egov.eis.model.WorkFlowDetails;
import org.egov.eis.model.enums.TransferType;
import org.egov.eis.model.enums.TypeOfMovement;
import org.egov.eis.repository.builder.MovementQueryBuilder;
import org.egov.eis.repository.rowmapper.MovementRowMapper;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.UserService;
import org.egov.eis.service.WorkFlowService;
import org.egov.eis.web.contract.Employee;
import org.egov.eis.web.contract.EmployeeInfo;
import org.egov.eis.web.contract.HRStatus;
import org.egov.eis.web.contract.MovementRequest;
import org.egov.eis.web.contract.MovementSearchRequest;
import org.egov.eis.web.contract.ProcessInstance;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.Task;
import org.egov.eis.web.contract.User;
import org.egov.eis.web.contract.UserResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class MovementRepositoryTest {

    @InjectMocks
    private MovementRepository movementRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private MovementRowMapper movementRowMapper;

    @Mock
    private MovementQueryBuilder movementQueryBuilder;

    @Mock
    private WorkFlowService workFlowService;

    @Mock
    private UserService userService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private PropertiesManager propertiesManager;

    @Mock
    private MovementDocumentsRepository documentsRepository;

    private List<Movement> movements;

    private List<HRStatus> hrStatuses;

    private List<Long> positions;

    private List<User> users;

    private RequestInfo requestInfo;

    private ResponseInfo responseInfo;

    private WorkFlowDetails workFlowDetails;

    private UserResponse userResponse;

    @Before
    public void before() {
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
        when(propertiesManager.getHrMastersServiceStatusesKey()).thenReturn("");
    }

    @Test
    public void test_movement_repository_should_return_movements() {
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
        when(movementQueryBuilder.getQuery(Mockito.any(MovementSearchRequest.class), Mockito.anyList(),
                Mockito.any(RequestInfo.class)))
                        .thenReturn("");
        when(documentsRepository.findByMovementId(Mockito.anyLong(), Mockito.anyString())).thenReturn(new ArrayList<>());
        when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(MovementRowMapper.class)))
                .thenReturn(movements);
        final MovementSearchRequest movementSearchRequest = new MovementSearchRequest();
        final RequestInfo requestInfo = new RequestInfo();
        final List<Movement> response = movementRepository.findForCriteria(movementSearchRequest, requestInfo);

        assertEquals(movements.size(), response.size());
    }

    @Test
    public void test_movement_repository_should_save_movement() {
        Movement movement1 = new Movement();
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement1.setTransferType(TransferType.TRANSFER_WITHIN_DEPARTMENT_OR_CORPORATION_OR_ULB);
        PromotionBasis promotionBasis = new PromotionBasis();
        promotionBasis.setId(1L);
        TransferReason transferReason = new TransferReason();
        transferReason.setId(1L);
        movement1.setPromotionBasis(promotionBasis);
        movement1.setReason(transferReason);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);

        users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        users.add(user);
        userResponse = new UserResponse(responseInfo, users);

        final ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId("1");

        when(workFlowService.start(Mockito.any(MovementRequest.class))).thenReturn(processInstance);
        when(userService.findUserByUserNameAndTenantId(Mockito.any(RequestInfo.class))).thenReturn(userResponse);
        when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(Object[].class))).thenReturn(0);

        MovementRequest result = movementRepository.saveMovement(movementRequest);
        assertEquals(result.getMovement().size(), movementRequest.getMovement().size());
    }

    @Test
    public void test_movement_repository_should_update_movement() {
        Movement movement1 = new Movement();
        workFlowDetails = new WorkFlowDetails();
        workFlowDetails.setAction("Approve");
        movement1.setId(1L);
        movement1.setEmployeeId(1L);
        movement1.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement1.setTransferType(TransferType.TRANSFER_WITHIN_DEPARTMENT_OR_CORPORATION_OR_ULB);
        movement1.setWorkflowDetails(workFlowDetails);
        movement1.setEmployeeAcceptance(true);
        movement1.setEffectiveFrom(new Date());
        PromotionBasis promotionBasis = new PromotionBasis();
        promotionBasis.setId(1L);
        TransferReason transferReason = new TransferReason();
        transferReason.setId(1L);
        movement1.setPromotionBasis(promotionBasis);
        movement1.setReason(transferReason);
        movements = new ArrayList<>();
        movements.add(movement1);
        final MovementRequest movementRequest = new MovementRequest();
        movementRequest.setMovement(movements);
        movementRequest.setRequestInfo(requestInfo);

        users = new ArrayList<>();
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        user.setDob("01/01/1990");
        users.add(user);
        userResponse = new UserResponse(responseInfo, users);

        EmployeeInfo employeeInfo = new EmployeeInfo();
        
        Employee employee = new Employee();
        employee.setUser(user);

        final Task task = new Task();
        task.setId("1");

        when(workFlowService.update(Mockito.any(MovementRequest.class))).thenReturn(task);
        when(userService.findUserByUserNameAndTenantId(Mockito.any(RequestInfo.class))).thenReturn(userResponse);
        when(jdbcTemplate.update(Mockito.anyString(), Mockito.any(Object[].class))).thenReturn(0);
        when(employeeService.updateEmployee(Mockito.any(Employee.class), Mockito.anyString(), Mockito.any(RequestInfo.class)))
                .thenReturn(employee);

       // Movement result = movementRepository.updateMovement(movementRequest);
       // assertEquals(result.getId(), movementRequest.getMovement().get(0).getId());
    }
}