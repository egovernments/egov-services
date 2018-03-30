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

package org.egov.eis.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.eis.model.Movement;
import org.egov.eis.model.enums.TransferType;
import org.egov.eis.service.MovementService;
import org.egov.eis.web.contract.MovementRequest;
import org.egov.eis.web.contract.MovementResponse;
import org.egov.eis.web.contract.MovementSearchRequest;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private static final Logger logger = LoggerFactory.getLogger(MovementController.class);

    @Autowired
    private ErrorHandler errorHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private MovementService movementService;

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final MovementSearchRequest movementSearchRequest,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        // validate input params
        if (modelAttributeBindingResult.hasErrors())
            return errorHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        // validate input params
        if (requestBodyBindingResult.hasErrors())
            return errorHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        // Call service
        List<Movement> movements = null;
        try {
            movements = movementService.getMovements(movementSearchRequest, requestInfo);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + movementSearchRequest, exception);
            return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(movements, requestInfo);
    }

    /**
     * Maps Post Requests for _create & returns ResponseEntity of either MovementResponse type or ErrorResponse type
     *
     * @param MovementRequest
     * @param BindingResult
     * @return ResponseEntity<?>
     */

    @PostMapping("_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final MovementRequest movementRequest,
            final BindingResult bindingResult, @RequestParam(name = "type", required = false) final String type) {

        final ResponseEntity<?> errorResponseEntity = validateMovementRequests(movementRequest,
                bindingResult);
        if (errorResponseEntity != null)
            return errorResponseEntity;

        return movementService.createMovements(movementRequest, type);
    }

    @PostMapping("/{movementId}/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final MovementRequest movementRequest,
            @PathVariable(required = true, name = "movementId") final Long movementId,
            final BindingResult bindingResult) {
        final ResponseEntity<?> errorResponseEntity = validateMovementRequests(movementRequest,
                bindingResult);
        if (errorResponseEntity != null)
            return errorResponseEntity;

        Movement movement = movementRequest.getMovement().get(0);
        if(movement.getCheckEmployeeExists()) {
            movementService.checkEmployeeExists(movementRequest);
            return movementService.getSuccessResponseForCreate(movementRequest.getMovement(), movementRequest.getRequestInfo());
        }

        movementRequest.getMovement().get(0).setId(movementId);

        return movementService.updateMovement(movementRequest);
    }

    /**
     * Populate Response object and return movementList
     *
     * @param movementList
     * @return
     */
    private ResponseEntity<?> getSuccessResponse(final List<Movement> movements,
            final RequestInfo requestInfo) {
        final MovementResponse movementResponse = new MovementResponse();
        movementResponse.setMovement(movements);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        movementResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<MovementResponse>(movementResponse, HttpStatus.OK);
    }

    /**
     * Validate MovementRequests object & returns ErrorResponseEntity if there are any errors or else returns null
     *
     * @param MovementRequest
     * @param bindingResult
     * @return ResponseEntity<?>
     */
    private ResponseEntity<?> validateMovementRequests(final MovementRequest movementRequest,
            final BindingResult bindingResult) {
        // validate input params that can be handled by annotations
        if (bindingResult.hasErrors())
            return errorHandler.getErrorResponseEntityForBindingErrors(bindingResult,
                    movementRequest.getRequestInfo());
        return null;
    }
}
