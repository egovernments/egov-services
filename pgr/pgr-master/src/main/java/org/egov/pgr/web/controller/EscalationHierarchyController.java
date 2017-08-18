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

package org.egov.pgr.web.controller;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.pgr.config.ApplicationProperties;
import org.egov.pgr.domain.exception.PGRMasterException;
import org.egov.pgr.domain.model.EscalationHierarchy;
import org.egov.pgr.service.EscalationHierarchyService;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.EscalationHierarchyGetReq;
import org.egov.pgr.web.contract.EscalationHierarchyReq;
import org.egov.pgr.web.contract.EscalationHierarchyRes;
import org.egov.pgr.web.contract.RequestInfoWrapper;
import org.egov.pgr.web.contract.factory.ResponseInfoFactory;
import org.egov.pgr.web.errorhandlers.Error;
import org.egov.pgr.web.errorhandlers.ErrorHandler;
import org.egov.pgr.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/escalation-hierarchy")
public class EscalationHierarchyController {

    private static final Logger logger = LoggerFactory.getLogger(EscalationHierarchyController.class);

    private static final String[] taskAction = {"create", "update"};
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String FIELD = "field";

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private EscalationHierarchyService escalationHierarchyService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ErrorHandler errHandler;

    HashMap<String, String> escalationHierarchyException = new HashMap<>();

    @PostMapping(value = "/v1/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final EscalationHierarchyReq escalationHierarchyRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("EscalationHierarchy Create : Request ::" + escalationHierarchyRequest);

        validateServiceGroupRequest(escalationHierarchyRequest, taskAction[0]);

        final List<EscalationHierarchy> escalationHierarchies = escalationHierarchyService.createEscalationHierarchy(
                applicationProperties.getCreateEscalationHierarchyTopicName(),
                applicationProperties.getCreateEscalationHierarchyTopicKey(), escalationHierarchyRequest);

        return getSuccessResponse(escalationHierarchies, escalationHierarchyRequest.getRequestInfo());

    }

    @PostMapping(value = "/v1/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final EscalationHierarchyReq escalationHierarchyRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("EscalationHierarchy Update: Request::" + escalationHierarchyRequest);

        validateServiceGroupRequest(escalationHierarchyRequest, taskAction[1]);

        final List<EscalationHierarchy> escalationHierarchies = escalationHierarchyService.updateEscalationHierarchy(
                applicationProperties.getUpdateEscalationHierarchyTopicName(),
                applicationProperties.getUpdateEscalationHierarchyTopicKey(), escalationHierarchyRequest);

        return getSuccessResponse(escalationHierarchies, escalationHierarchyRequest.getRequestInfo());

    }

    @PostMapping("/v1/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final EscalationHierarchyGetReq escHierarchyGetRequest,
                                    final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
                                    final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        List<EscalationHierarchy> escHierarchyList = null;
        try {
            escHierarchyList = escalationHierarchyService.getAllEscalationHierarchy(escHierarchyGetRequest);
        } catch (final Exception exception) {
            logger.error("EscalationHierarchy Search : Error while processing request : " + escHierarchyGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(escHierarchyList, requestInfo);

    }

    private void validateServiceGroupRequest(final EscalationHierarchyReq escalationHierarchyRequest,
                                             String action) {
        fromtopositioncheck(escalationHierarchyRequest);
        addTenantIdValidationErrors(escalationHierarchyRequest);
        serviceCodeCheck(escalationHierarchyRequest);
        if (action.equals(taskAction[0])) {
            checkCombinationExists(escalationHierarchyRequest);
        }
    }

    private ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError fieldError : errors.getFieldErrors())
                error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
        errRes.setError(error);
        return errRes;
    }

    private void fromtopositioncheck(final EscalationHierarchyReq escalationHierarchyRequest) {
        for (int i = 0; i < escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
            if (null == escalationHierarchyRequest.getEscalationHierarchy().get(i).getFromPosition()
                    || escalationHierarchyRequest.getEscalationHierarchy().get(i).getFromPosition() <= 0L) {
                escalationHierarchyException.put(CODE, PgrMasterConstants.FROMPOSITION_MANDATORY_CODE);
                escalationHierarchyException.put(MESSAGE, PgrMasterConstants.FROMPOSITION_MANADATORY_ERROR_MESSAGE);
                escalationHierarchyException.put(FIELD, PgrMasterConstants.FROMPOSITION_MANADATORY_FIELD_NAME);
                throw new PGRMasterException(escalationHierarchyException);
            }
            if (null == escalationHierarchyRequest.getEscalationHierarchy().get(i).getToPosition()
                    || escalationHierarchyRequest.getEscalationHierarchy().get(i).getToPosition() <= 0L) {
                escalationHierarchyException.put(CODE, PgrMasterConstants.TOPOSITION_MANDATORY_CODE);
                escalationHierarchyException.put(MESSAGE, PgrMasterConstants.TOPOSITION_MANADATORY_ERROR_MESSAGE);
                escalationHierarchyException.put(FIELD, PgrMasterConstants.TOPOSITION_MANADATORY_FIELD_NAME);
                throw new PGRMasterException(escalationHierarchyException);
            }
            if (escalationHierarchyRequest.getEscalationHierarchy().get(i)
                    .getFromPosition() == escalationHierarchyRequest.getEscalationHierarchy().get(i).getToPosition()) {
                escalationHierarchyException.put(CODE, PgrMasterConstants.FROMTOPOSITION_UNIQUE_CODE);
                escalationHierarchyException.put(MESSAGE, PgrMasterConstants.FROMTOPOSITION_UNIQUE_ERROR_MESSAGE);
                escalationHierarchyException.put(FIELD, PgrMasterConstants.FROMTOPOSITION_UNIQUE_FIELD_NAME);
                throw new PGRMasterException(escalationHierarchyException);
            }
        }
    }

    private void serviceCodeCheck(final EscalationHierarchyReq escalationHierarchyRequest) {
        for (int i = 0; i < escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
            if (escalationHierarchyRequest.getEscalationHierarchy().get(i).getServiceCode() == null || escalationHierarchyRequest.getEscalationHierarchy().get(i).getServiceCode().isEmpty()) {
                escalationHierarchyException.put(CODE, PgrMasterConstants.GRIEVANCETYPE_CODE_MANDATORY_CODE);
                escalationHierarchyException.put(MESSAGE, PgrMasterConstants.GRIEVANCETYPE_CODE_MANADATORY_ERROR_MESSAGE);
                escalationHierarchyException.put(FIELD, PgrMasterConstants.GRIEVANCETYPE_CODE_MANADATORY_FIELD_NAME);
                throw new PGRMasterException(escalationHierarchyException);
            }
        }
    }

    private void addTenantIdValidationErrors(final EscalationHierarchyReq escalationHierarchyRequest) {
        for (int i = 0; i < escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
            if (escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId() == null || escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId().isEmpty()) {
                escalationHierarchyException.put(CODE, PgrMasterConstants.TENANTID_MANDATORY_CODE);
                escalationHierarchyException.put(MESSAGE, PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE);
                escalationHierarchyException.put(FIELD, PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME);
                throw new PGRMasterException(escalationHierarchyException);
            } else
                return;
        }
    }

    private void checkCombinationExists(final EscalationHierarchyReq escalationHierarchyRequest) {
        for (int i = 0; i < escalationHierarchyRequest.getEscalationHierarchy().size(); i++) {
            if (escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId() == null || escalationHierarchyRequest.getEscalationHierarchy().get(i).getTenantId().isEmpty()) {
                escalationHierarchyException.put(CODE, PgrMasterConstants.TENANTID_MANDATORY_CODE);
                escalationHierarchyException.put(MESSAGE, PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE);
                escalationHierarchyException.put(FIELD, PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME);
                throw new PGRMasterException(escalationHierarchyException);
            } else
                return;
        }
    }

    private ResponseEntity<?> getSuccessResponse(final List<EscalationHierarchy> escalationHierarchyList,
                                                 final RequestInfo requestInfo) {
        final EscalationHierarchyRes escalationHierarchyRes = new EscalationHierarchyRes();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        escalationHierarchyRes.setResponseInfo(responseInfo);
        escalationHierarchyRes.setEscalationHierarchies(escalationHierarchyList);
        return new ResponseEntity<>(escalationHierarchyRes, HttpStatus.OK);
    }
}
