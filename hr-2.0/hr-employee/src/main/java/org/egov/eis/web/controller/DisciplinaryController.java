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

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.Disciplinary;
import org.egov.eis.service.DisciplinaryService;
import org.egov.eis.web.contract.DisciplinaryGetRequest;
import org.egov.eis.web.contract.DisciplinaryRequest;
import org.egov.eis.web.contract.DisciplinaryResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandler.ErrorHandler;
import org.egov.eis.web.validator.DisciplinaryValidatorForCreate;
import org.egov.eis.web.validator.DisciplinaryValidatorForUpdate;
import org.egov.eis.web.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/disciplinary")
public class DisciplinaryController {

    @Autowired
    private DisciplinaryService disciplinaryService;

    @Autowired
    private DisciplinaryValidatorForCreate disciplinaryValidatorForCreate;

    @Autowired
    private DisciplinaryValidatorForUpdate disciplinaryValidatorForUpdate;
    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errorHandler;
    @Autowired
    private RequestValidator requestValidator;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final DisciplinaryRequest disciplinaryRequest,
            final BindingResult errors) {
        log.debug("DisciplinaryRequest::" + disciplinaryRequest);
        final ResponseEntity<?> errorResponseEntity = validateDisciplinaryRequest(disciplinaryRequest, errors,false);
        if (!isEmpty(errorResponseEntity))
            return errorResponseEntity;

        final Disciplinary disciplinary = disciplinaryService.create(disciplinaryRequest);

        final List<Disciplinary> disciplinarys = new ArrayList<>();
        disciplinarys.add(disciplinary);
        return getSuccessResponse(disciplinarys, disciplinaryRequest.getRequestInfo());
    }

    @PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final DisciplinaryRequest disciplinaryRequest, final BindingResult errors) {

        log.debug("DisciplinaryRequest::" + disciplinaryRequest);
        final ResponseEntity<?> errorResponseEntity = validateDisciplinaryRequest(disciplinaryRequest, errors,true);
        if (!isEmpty(errorResponseEntity))
            return errorResponseEntity;

        final Disciplinary disciplinary = disciplinaryService.update(disciplinaryRequest);

        final List<Disciplinary> disciplinarys = new ArrayList<>();
        disciplinarys.add(disciplinary);
        return getSuccessResponse(disciplinarys, disciplinaryRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final DisciplinaryGetRequest disciplinaryGetRequest,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        final ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo,
                modelAttributeBindingResult, requestBodyBindingResult);

        if (errorResponseEntity != null)
            return errorResponseEntity;

        // Call service
        List<Disciplinary> disciplinaryList = null;
        try {
            disciplinaryList = disciplinaryService.getDisciplinarys(disciplinaryGetRequest);
        } catch (final Exception exception) {
            log.error("Error while processing request " + disciplinaryGetRequest, exception);
            return errorHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(disciplinaryList, requestInfo);
    }

    /**
     * Populate Response object and returns calendarYears List
     *
     * @param calendarYearsList
     * @return
     */
    private ResponseEntity<?> getSuccessResponse(final List<Disciplinary> disciplinarysList, final RequestInfo requestInfo) {
        final DisciplinaryResponse disciplinaryRes = new DisciplinaryResponse();
        disciplinaryRes.setDisciplinary(disciplinarysList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        disciplinaryRes.setResponseInfo(responseInfo);
        return new ResponseEntity<DisciplinaryResponse>(disciplinaryRes, HttpStatus.OK);

    }

    private ResponseEntity<?> validateDisciplinaryRequest(final DisciplinaryRequest disciplinaryRequest,
            final BindingResult bindingResult,Boolean isUpdate) {
        // validate input params that can be handled by annotations
        if (bindingResult.hasErrors())
            return errorHandler.getErrorResponseEntityForInvalidRequest(bindingResult, disciplinaryRequest.getRequestInfo());
        if(isUpdate)
            ValidationUtils.invokeValidator(disciplinaryValidatorForUpdate, disciplinaryRequest, bindingResult);
        else
            ValidationUtils.invokeValidator(disciplinaryValidatorForCreate, disciplinaryRequest, bindingResult);

        if (bindingResult.hasErrors())
            return errorHandler.getErrorResponseEntityForInvalidRequest(bindingResult, disciplinaryRequest.getRequestInfo());
        return null;
    }

}
