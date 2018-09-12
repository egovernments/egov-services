/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.egov.eis.config.PropertiesManager;
import org.egov.eis.model.Document;
import org.egov.eis.model.Movement;
import org.egov.eis.model.enums.MovementStatus;
import org.egov.eis.model.enums.TransferType;
import org.egov.eis.model.enums.TypeOfMovement;
import org.egov.eis.repository.MovementDocumentsRepository;
import org.egov.eis.repository.MovementRepository;
import org.egov.eis.util.ApplicationConstants;
import org.egov.eis.web.contract.*;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovementService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MovementService.class);

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private PositionService positionService;

    @Autowired
    private ApplicationConstants applicationConstants;

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private MovementDocumentsRepository documentsRepository;

    public List<Movement> getMovements(final MovementSearchRequest movementSearchRequest,
                                       final RequestInfo requestInfo) {
        List<Movement> movements = movementRepository.findForCriteria(movementSearchRequest, requestInfo);
        for (final Movement movement : movements) {
            final List<Document> documents = documentsRepository.findByMovementId(movement.getId(),
                    movement.getTenantId());
            for (final Document document : documents)
                movement.getDocuments().add(document.getDocument());
        }
        return movements;
    }

    public ResponseEntity<?> createMovements(final MovementRequest movementRequest, final String type) {
        final Boolean isExcelUpload = type != null && "upload".equalsIgnoreCase(type);

        movementRequest.setType(type);
        List<Movement> movementsList = new ArrayList<>();
        try {
            movementsList = validate(movementRequest);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final List<Movement> successMovementsList = new ArrayList<>();
        final List<Movement> errorMovementsList = new ArrayList<>();
        for (final Movement movement : movementsList)
            if (StringUtils.isEmpty(movement.getErrorMsg()))
                successMovementsList.add(movement);
            else
                errorMovementsList.add(movement);
        movementRequest.setMovement(successMovementsList);
        for (final Movement movement : movementRequest.getMovement())
            if (isExcelUpload)
                movement.setStatus(employeeService.getHRStatuses(propertiesManager.getHrMastersServiceStatusesKey(),
                        MovementStatus.APPROVED.toString(), null, movement.getTenantId(),
                        movementRequest.getRequestInfo()).get(0).getId());
            else
                movement.setStatus(employeeService.getHRStatuses(propertiesManager.getHrMastersServiceStatusesKey(),
                        MovementStatus.APPLIED.toString(), null, movement.getTenantId(),
                        movementRequest.getRequestInfo()).get(0).getId());
        String movementRequestJson = null;
        try {
            movementRequestJson = objectMapper.writeValueAsString(movementRequest);
            LOGGER.info("movementRequestJson::" + movementRequestJson);
        } catch (final JsonProcessingException e) {
            LOGGER.error("Error while converting Movement to JSON", e);
            e.printStackTrace();
        }
        try {
            if (movementsList.size() > 0 && movementsList.get(0).getEmployeeAcceptance())
                create(movementRequest);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        if (isExcelUpload)
            return getSuccessResponseForUpload(successMovementsList, errorMovementsList,
                    movementRequest.getRequestInfo());
        else
            return getSuccessResponseForCreate(movementsList, movementRequest.getRequestInfo());
    }

    private List<Movement> validate(final MovementRequest movementRequest) throws ParseException {
        for (final Movement movement : movementRequest.getMovement()) {
            String message = "";

            final Employee employee = employeeService.getEmployeeById(movementRequest);

            Date dor = new Date();
            String formattedDOB = null;

            final SimpleDateFormat inputDOB = new SimpleDateFormat("yyyy-MM-dd");
            final SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            if (employee.getUser().getDob() != null) {
                formattedDOB = output.format(inputDOB.parse(employee.getUser().getDob()));
            }

            if (employee.getDateOfRetirement() != null) {
                dor = employee.getDateOfRetirement();
            } else if (formattedDOB != null) {
                String dob = formattedDOB;
                dor = output.parse(dob);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dor);
                cal.add(Calendar.YEAR, 60);
                dor = cal.getTime();
            }
            // validateEmployeeNextPositionWithCurrent
            if (employee != null && employee.getId() != null && formattedDOB != null) {
                if (movement.getTypeOfMovement().equals(TypeOfMovement.PROMOTION) || (movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER_CUM_PROMOTION) && movement.getTransferType().equals(TransferType.TRANSFER_WITHIN_DEPARTMENT_OR_CORPORATION_OR_ULB))) {

                    for (Assignment assignment : employee.getAssignments()) {
                        Date formattedFD = output.parse(assignment.getFromDate());
                        Date formattedTD = output.parse(assignment.getToDate());

                        if (((formattedFD.after(movement.getEffectiveFrom()) && formattedFD.before(dor)) ||
                                (formattedTD.after(movement.getEffectiveFrom()) && formattedTD.before(dor)) ||
                                (formattedFD.before(movement.getEffectiveFrom()) && formattedTD.after(dor))) &&
                                movement.getPositionAssigned().equals(assignment.getPosition())) {
                            message = message + applicationConstants
                                    .getErrorMessage(applicationConstants.ERR_MOVEMENT_EMPLOYEE_POSITION_VALIDATE) + ", ";
                        }


                    }
                }
                if ((employee.getDateOfRetirement() == null
                        || (employee.getDateOfRetirement() != null && employee.getDateOfRetirement().equals("")))
                        && (employee.getUser().getDob() == null || (employee.getUser().getDob() != null && employee.getUser().getDob().equals(""))))
                    message = message + applicationConstants
                            .getErrorMessage(applicationConstants.ERR_MOVEMENT_EMPLOYEE_DOB_VALIDATE) + ", ";

                if (employee.getUser().getRoles().isEmpty())
                    message = message + applicationConstants.getErrorMessage(applicationConstants.ERR_MOVEMENT_EMPLOYEE_ROLE_VALIDATE);
            } else {
                message = message
                        + applicationConstants.getErrorMessage(applicationConstants.ERR_MOVEMENT_EMPLOYEE_DOB_VALIDATE)
                        + ", ";
            }

            setErrorMessage(movement, message);

            if (movement.getTypeOfMovement().equals(TypeOfMovement.PROMOTION) || (movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER_CUM_PROMOTION) && movement.getTransferType().equals(TransferType.TRANSFER_WITHIN_DEPARTMENT_OR_CORPORATION_OR_ULB))) {

                // validateEmployeeForPromotion
                final List<Long> positions = positionService.getPositions(movement, movementRequest.getRequestInfo(), dor, null);
                if (positions.contains(movement.getPositionAssigned()))
                    message = applicationConstants.getErrorMessage(ApplicationConstants.ERR_POSITION_NOT_VACANT) + ", ";
                setErrorMessage(movement, message);

                if (movement.getTypeOfMovement() != null
                        && movement.getTypeOfMovement().toString().equalsIgnoreCase(TypeOfMovement.TRANSFER.toString())
                        && movement.getPromotionBasis() != null) {
                    message = message
                            + applicationConstants.getErrorMessage(ApplicationConstants.ERR_MOVEMENT_TYPE_VALIDATE)
                            + ", ";
                }

                setErrorMessage(movement, message);

                if (movement.getTypeOfMovement() != null
                        && !movement.getTypeOfMovement().toString().equalsIgnoreCase(TypeOfMovement.TRANSFER.toString())
                        && movement.getPromotionBasis() == null) {
                    message = message + applicationConstants
                            .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_TYPE_VALIDATE_PROMOTION) + ", ";
                }

                setErrorMessage(movement, message);

                if (movement.getTenantId() == null || movement.getTenantId().isEmpty()) {
                    message = message
                            + applicationConstants.getErrorMessage(ApplicationConstants.ERR_MOVEMENT_NOT_TENANT) + ", ";
                }

                setErrorMessage(movement, message);
            } else if (movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER) && movement.getTransferType().equals(TransferType.TRANSFER_OUTSIDE_CORPORATION_OR_ULB)) {
                final List<Long> positions = positionService.getPositions(movement, movementRequest.getRequestInfo(), dor, movement.getTransferedLocation());
                if (positions.contains(movement.getPositionAssigned()))
                    message = applicationConstants.getErrorMessage(ApplicationConstants.ERR_POSITION_NOT_VACANT) + ", ";
            }
            if (movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER) && movement.getReason() == null) {

                message = message + applicationConstants
                        .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_TRANSFER_REASON_VALIDATE) + ", ";
                setErrorMessage(movement, message);

            }

            for (final Assignment employeeAssignment : employee.getAssignments()) {
                Date formattedFD = output.parse(employeeAssignment.getFromDate());
                if (formattedFD.after(movement.getEffectiveFrom())) {
                    message = message + applicationConstants.getErrorMessage(ApplicationConstants.ERR_MOVEMENT_OVERLAP_FUTURE_ASSIGNMENTDATE);
                    setErrorMessage(movement, message);
                }
            }

            if (movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER)
                    && !movement.getTransferType().equals(TransferType.TRANSFER_WITHIN_DEPARTMENT_OR_CORPORATION_OR_ULB)
                    && movement.getTransferedLocation() == null) {
                message = message + applicationConstants
                        .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_TRANSFER_LOCATION_VALIDATE) + ", ";
                setErrorMessage(movement, message);
            }
            LOGGER.info("CHECK MOVEMENT EXIST , MOVEMENT REQUEST::" + movement);

            List<Movement> existingMovements = movementRepository.findForExistingMovement(movement,
                    movementRequest.getRequestInfo());

            List<Movement> transferOutULB = existingMovements.stream().filter(existingmov -> existingmov.getTransferType().equals(TransferType.TRANSFER_OUTSIDE_CORPORATION_OR_ULB)).collect(Collectors.toList());

            LOGGER.info("EXISTING MOVEMENTS::" + existingMovements);

            if (!existingMovements.isEmpty() && !transferOutULB.isEmpty()) {
                message = message + applicationConstants
                        .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_EMPLOYEE_TRANSFER_VALIDATE) + ", ";
                setErrorMessage(movement, message);
            } else if (!existingMovements.isEmpty()) {
                message = message + applicationConstants.getErrorMessage(ApplicationConstants.ERR_MOVEMENT_EXISTING_EMPLOYEE_VALIDATE);
                setErrorMessage(movement, message);
            }

            if (movement.getEmployeeAcceptance() != null && !movement.getEmployeeAcceptance()
                    && "Approve".equalsIgnoreCase(movement.getWorkflowDetails().getAction())) {
                message = message + applicationConstants
                        .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_EMPLOYEEACCEPTANCE_VALIDATE) + ", ";
            }

            setErrorMessage(movement, message);

        }

        return movementRequest.getMovement();
    }

    private void setErrorMessage(Movement movement, String message) {
        if (!message.isEmpty())
            movement.setErrorMsg(message);
    }

    public ResponseEntity<?> getSuccessResponseForCreate(final List<Movement> movementsList,
                                                         final RequestInfo requestInfo) {
        final MovementResponse movementRes = new MovementResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        if (!StringUtils.isEmpty(movementsList.get(0).getErrorMsg()))
            httpStatus = HttpStatus.BAD_REQUEST;
        movementRes.setMovement(movementsList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(httpStatus.toString());
        movementRes.setResponseInfo(responseInfo);
        return new ResponseEntity<>(movementRes, httpStatus);
    }

    private ResponseEntity<?> getSuccessResponseForUpload(final List<Movement> successMovementsList,
                                                          final List<Movement> errorMovementsList, final RequestInfo requestInfo) {
        final MovementUploadResponse movementUploadResponse = new MovementUploadResponse();
        movementUploadResponse.getSuccessList().addAll(successMovementsList);
        movementUploadResponse.getErrorList().addAll(errorMovementsList);

        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        movementUploadResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(movementUploadResponse, HttpStatus.OK);
    }

    public MovementRequest create(final MovementRequest movementRequest) {
        movementRequest.getMovement().get(0).setId(movementRepository.generateSequence());
        return movementRepository.saveMovement(movementRequest);
    }

    public ResponseEntity<?> updateMovement(final MovementRequest movementRequest) {
        List<Movement> movements = new ArrayList<>();
        movements.add(movementRequest.getMovement().get(0));
        movements = validateUpdate(movementRequest);
        if (StringUtils.isEmpty(movements.get(0).getErrorMsg())) {
            final MovementSearchRequest movementSearchRequest = new MovementSearchRequest();
            final List<Long> ids = new ArrayList<>();
            ids.add(movements.get(0).getId());
            movementSearchRequest.setId(ids);
            final List<Movement> oldMovements = movementRepository.findForCriteria(movementSearchRequest,
                    movementRequest.getRequestInfo());
            movements.get(0).setStatus(oldMovements.get(0).getStatus());
            movements.get(0).setStateId(oldMovements.get(0).getStateId());
            String movementRequestJson = null;
            try {
                movementRequestJson = objectMapper.writeValueAsString(movementRequest);
                LOGGER.info("movementRequestJson::" + movementRequestJson);
            } catch (final JsonProcessingException e) {
                LOGGER.error("Error while converting Movement to JSON", e);
                e.printStackTrace();
            }
            try {
                  update(movementRequest);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }
            movementStatusChange(movements.get(0), movementRequest.getRequestInfo());
        }
        return getSuccessResponseForCreate(movements, movementRequest.getRequestInfo());
    }

    private void movementStatusChange(final Movement movement, final RequestInfo requestInfo) {
        final String workFlowAction = movement.getWorkflowDetails().getAction();
        final String objectName = propertiesManager.getHrMastersServiceStatusesKey();
        if ("Approve".equalsIgnoreCase(workFlowAction))
            movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.APPROVED.toString(), null,
                    movement.getTenantId(), requestInfo).get(0).getId());
        else if ("Reject".equalsIgnoreCase(workFlowAction))
            movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.REJECTED.toString(), null,
                    movement.getTenantId(), requestInfo).get(0).getId());
        else if ("Cancel".equalsIgnoreCase(workFlowAction))
            movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.CANCELLED.toString(), null,
                    movement.getTenantId(), requestInfo).get(0).getId());
        else if ("Submit".equalsIgnoreCase(workFlowAction))
            movement.setStatus(employeeService.getHRStatuses(objectName, MovementStatus.RESUBMITTED.toString(), null,
                    movement.getTenantId(), requestInfo).get(0).getId());
    }

    public Movement update(final MovementRequest movementRequest) {
        return movementRepository.updateMovement(movementRequest);
    }

    private List<Movement> validateUpdate(final MovementRequest movementRequest) {
        final SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            for (final Movement movement : movementRequest.getMovement()) {
                String errorMsg = "";
                String message = "";
                Date effectiveToDate = null;
                if (movement.getId() != null && (movement.getTypeOfMovement().equals(TypeOfMovement.PROMOTION) ||
                        (movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER_CUM_PROMOTION) &&
                                movement.getTransferType().equals(TransferType.TRANSFER_WITHIN_DEPARTMENT_OR_CORPORATION_OR_ULB)))
                        && "Approve".equalsIgnoreCase(movement.getWorkflowDetails().getAction())) {
                    final EmployeeInfo employee = employeeService.getEmployee(movement, movementRequest.getRequestInfo());
                    final SimpleDateFormat inputDOB = new SimpleDateFormat("yyyy-MM-dd");

                    effectiveToDate = inputDOB.parse(employee.getDob());

                    List<Assignment> assignments = employee.getAssignments().stream().filter(assignment -> assignment.getIsPrimary().equals(true)).collect(Collectors.toList());
                    for (Assignment assign : assignments) {
                        Date FormatFD = output.parse(assign.getFromDate());

                        if (FormatFD.after(movement.getEffectiveFrom())) {
                            message = applicationConstants
                                    .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_OVERLAP_ASSIGNMENTDATE) + ", ";
                        }
                        setErrorMessage(movement, message);

                    }
                    Date retirementDate = null;
                    if (employee.getDateOfRetirement() != null && !employee.getDateOfRetirement().equals(""))
                        retirementDate = employee.getDateOfRetirement();

                    if (retirementDate != null && movement.getEffectiveFrom().after(retirementDate)) {
                        message = applicationConstants
                                .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_RETIREMENTDATE_VALIDATE) + ", ";
                        setErrorMessage(movement, message);
                    }

                    if (movement.getEffectiveFrom().before(new Date())) {
                        message = applicationConstants
                                .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_EFFECTIVEFROM_VALIDATE) + ", ";
                        setErrorMessage(movement, message);
                    }
                    // validateEmployeeForPromotion
                    final List<Long> positions = positionService.getPositions(movement, movementRequest.getRequestInfo(), effectiveToDate, null);
                    if (positions.contains(movement.getPositionAssigned()))
                        message = applicationConstants.getErrorMessage(ApplicationConstants.ERR_POSITION_NOT_VACANT) + ", ";
                    setErrorMessage(movement, message);

                    // validateEmployeeNextDesignationWithCurrent
                    if (employee != null) {
                        for (Assignment assignment : employee.getAssignments()) {
                            Date formatFD = output.parse(assignment.getFromDate());
                            Date formatTD = output.parse(assignment.getToDate());
                            if (formatFD.before(movement.getEffectiveFrom())
                                    && formatTD.after(movement.getEffectiveFrom())
                                    && movement.getPositionAssigned().equals(assignment.getPosition())) {
                                message = message + applicationConstants.ERR_MOVEMENT_EMPLOYEE_POSITION_VALIDATE + ", ";
                            }
                        }
                    }

                    setErrorMessage(movement, message);

                    if (movement.getEmployeeAcceptance() != null && !movement.getEmployeeAcceptance()) {
                        message = message + ApplicationConstants.ERR_MOVEMENT_EMPLOYEEACCEPTANCE_VALIDATE + ", ";
                    }

                    setErrorMessage(movement, message);
                    //movement.setErrorMsg(errorMsg.replace(", ", ","));
                } else if (movement.getId() != null && movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER) &&
                        movement.getTransferType().equals(TransferType.TRANSFER_OUTSIDE_CORPORATION_OR_ULB)
                        && "Approve".equalsIgnoreCase(movement.getWorkflowDetails().getAction())) {
                    final Employee employee = employeeService.getEmployeeById(movementRequest);
                    EmployeeInfo employeeInfo = employeeService.getEmployee(null, employee.getCode(), movementRequest.getMovement().get(0).getTenantId(), movementRequest.getRequestInfo());

                    final SimpleDateFormat inputDOB = new SimpleDateFormat("yyyy-MM-dd");

                    effectiveToDate = inputDOB.parse(employee.getUser().getDob());

                    if (employeeInfo != null && !employeeInfo.equals("") && movement.getCheckEmployeeExists())
                        message = message + ApplicationConstants.ERR_MOVEMENT_EMPLOYEE_EXISTS + ", ";
                    setErrorMessage(movement, message);
                    if (employeeInfo == null || (employeeInfo != null && employeeInfo.equals("")))
                        movement.setCheckEmployeeExists(false);
                    final List<Long> positions = positionService.getPositions(movement, movementRequest.getRequestInfo(), effectiveToDate, movement.getTransferedLocation());
                    if (positions.contains(movement.getPositionAssigned())) {
                        message = applicationConstants.getErrorMessage(ApplicationConstants.ERR_POSITION_NOT_VACANT) + ", ";
                        setErrorMessage(movement, message);
                    }
                    if (!movement.getCheckEmployeeExists() && employeeInfo != null && !employeeInfo.equals("")) {
                        List<Assignment> assignments = employee.getAssignments().stream().filter(assignment -> assignment.getIsPrimary().equals(true)).collect(Collectors.toList());
                        for (Assignment assign : assignments) {
                            Date formatDate = output.parse(assign.getFromDate());
                            if (formatDate.after(movement.getEffectiveFrom())) {
                                message = applicationConstants
                                        .getErrorMessage(ApplicationConstants.ERR_MOVEMENT_OVERLAP_FUTURE_ASSIGNMENTDATE) + ", ";
                            }
                            setErrorMessage(movement, message);
                        }
                    }
                    movement.setErrorMsg(errorMsg.replace(", ", ","));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return movementRequest.getMovement();
    }

    public Boolean checkEmployeeExists(final MovementRequest movementRequest) {
        for (final Movement movement : movementRequest.getMovement()) {
            LOGGER.info("Employee exists:" + movement.getCheckEmployeeExists());

            if (movement.getId() != null && movement.getTypeOfMovement().equals(TypeOfMovement.TRANSFER) &&
                    movement.getTransferType().equals(TransferType.TRANSFER_OUTSIDE_CORPORATION_OR_ULB)
                    && "Approve".equalsIgnoreCase(movement.getWorkflowDetails().getAction())) {
                final Employee employee = employeeService.getEmployeeById(movementRequest);
                EmployeeInfo employeeInfo = employeeService.getEmployee(null, employee.getCode(), movementRequest.getMovement().get(0).getTransferedLocation(), movementRequest.getRequestInfo());
                LOGGER.info("Employee Info:" + employeeInfo.getCode());

                if (employeeInfo.getCode() != null && !employeeInfo.getCode().equals(""))
                    return true;
                else
                    return false;
            } else {
                return false;
            }

        }
        return false;
    }
}
