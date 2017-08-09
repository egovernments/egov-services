package org.egov.eis.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.eis.model.Movement;
import org.egov.eis.model.PromotionBasis;
import org.egov.eis.model.TransferReason;
import org.egov.eis.model.WorkFlowDetails;
import org.egov.eis.model.enums.TransferType;
import org.egov.eis.model.enums.TypeOfMovement;
import org.egov.eis.service.MovementService;
import org.egov.eis.utils.Resources;
import org.egov.eis.web.contract.MovementResponse;
import org.egov.eis.web.contract.MovementSearchRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MovementController.class)
public class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovementService movementService;

    @MockBean
    private ErrorHandler errorHandler;

    @MockBean
    private ResponseInfoFactory responseInfoFactory;

    private Resources resources = new Resources();

    @Test
    public void test_movement_controller_should_search_movements() throws IOException, Exception {
        final MovementResponse movementResponse = (MovementResponse) getMovementResponse().getBody();
        when(movementService.getMovements(Mockito.any(MovementSearchRequest.class), Mockito.any(RequestInfo.class)))
                .thenReturn(movementResponse.getMovement());
        when(responseInfoFactory.createResponseInfoFromRequestInfo(Mockito.any(RequestInfo.class), Mockito.anyBoolean()))
                .thenReturn(new ResponseInfo());
        mockMvc.perform(post("/movements/_search").param("tenantId", "ap.kurnool").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(resources.getFileContents("movement/movementContractSearchRequest.json"))).andExpect(status().isOk())
                .andExpect(content().json(resources.getFileContents("movement/movementContractSearchResponse.json")));
    }
    
    private ResponseEntity<MovementResponse> getMovementResponse() throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final Date date = sdf.parse("23/06/2017");
        final MovementResponse movementResponse = new MovementResponse();
        final List<Movement> movements = new ArrayList<>();
        final Movement movement = new Movement();
        final WorkFlowDetails workFlowDetails = new WorkFlowDetails();
        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId(null);
        responseInfo.setMsgId(null);
        responseInfo.setResMsgId(null);
        responseInfo.setStatus("200");
        responseInfo.setTs(null);
        responseInfo.setVer(null);
        workFlowDetails.setAssignee(1L);
        movement.setEmployeeId(1L);
        movement.setTypeOfMovement(TypeOfMovement.PROMOTION);
        movement.setCurrentAssignment(1L);
        movement.setTransferType(TransferType.TRANSFER_WITHIN_DEPARTMENT_OR_CORPORATION_OR_ULB);
        final PromotionBasis promotionBasis = new PromotionBasis();
        promotionBasis.setId(1L);
        movement.setPromotionBasis(promotionBasis);
        movement.setRemarks("Transfer");
        final TransferReason transferReason = new TransferReason();
        transferReason.setId(1L);
        movement.setReason(transferReason);
        movement.setEffectiveFrom(date);
        movement.setEnquiryPassedDate(date);
        movement.setTransferedLocation("ap.adoni");
        movement.setDepartmentAssigned(1L);
        movement.setDesignationAssigned(1L);
        movement.setPositionAssigned(1L);
        movement.setFundAssigned(1L);
        movement.setFunctionAssigned(1L);
        movement.setWorkflowDetails(workFlowDetails);
        movement.setTenantId("ap.kurnool");
        movement.setCreatedBy(1L);
        movement.setCreatedDate(date);
        movement.setLastModifiedBy(1L);
        movement.setLastModifiedDate(date);
        movements.add(movement);
        movementResponse.setMovement(movements);
        movementResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<MovementResponse>(movementResponse, HttpStatus.OK);
    }
}