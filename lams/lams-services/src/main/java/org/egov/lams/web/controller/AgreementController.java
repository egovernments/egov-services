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
package org.egov.lams.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.enums.Source;
import org.egov.lams.service.AgreementService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AgreementResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.errorhandlers.Error;
import org.egov.lams.web.errorhandlers.ErrorResponse;
import org.egov.lams.web.validator.AgreementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("agreements")
@Slf4j
public class AgreementController {

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private AgreementValidator agreementValidator;

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final AgreementCriteria agreementCriteria,
            @RequestBody final RequestInfoWrapper requestInfoWrapper, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        log.info("AgreementController:getAgreements():searchAgreementsModel:" + agreementCriteria);
        final List<Agreement> agreements = agreementService.searchAgreement(agreementCriteria, requestInfo);
        log.info("before sending for response success");
        return getSuccessResponse(agreements, requestInfo);
    }

    @PostMapping("_advancesearch")
    @ResponseBody
    public ResponseEntity<?> searchAgreementByAgreementNo(@ModelAttribute @Valid final AgreementCriteria agreementCriteria,
                                                          @RequestBody final RequestInfoWrapper requestInfoWrapper, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        log.info("AgreementController:getAgreements():searchAgreementsModel:" + agreementCriteria);
        final List<Agreement> agreements = agreementService.searchAgreementByAgreementNo(agreementCriteria, requestInfo);
        log.info("before sending for response success");
        return getSuccessResponse(agreements, requestInfo);
    }

    private ResponseEntity<?> getSuccessResponse(final List<Agreement> agreements, final RequestInfo requestInfo) {
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, requestInfo);
        log.info("before returning from getsucces resposne ::" + agreementResponse);
        return new ResponseEntity<>(agreementResponse, HttpStatus.OK);
    }

    @PostMapping("_commonsearch")
    @ResponseBody
    public ResponseEntity<?> commonSearch(@ModelAttribute @Valid final AgreementCriteria agreementCriteria,
            @RequestBody final RequestInfoWrapper requestInfoWrapper, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        List<Agreement> agreements = null;
        final AgreementRequest agreementRequest = new AgreementRequest();
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if (agreementCriteria.getAgreementNumber() != null && agreementCriteria.getTenantId() != null)
            agreements = agreementService.getAgreementsByAgreementNumber(agreementCriteria, requestInfo);
        if (agreements != null && !agreements.isEmpty()) {
            agreements.sort((agreement1, agreement2) -> agreement2.getId().compareTo(agreement1.getId()));
            agreementRequest.setRequestInfo(requestInfo);
            agreementRequest.setAgreement(agreements.get(0));
            agreementValidator.validateAgreementForWorkFLow(agreementRequest, bindingResult,
                    agreementCriteria.getAction());

            if (bindingResult.hasErrors()) {
                final ErrorResponse errorResponse = populateValidationErrors(bindingResult);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            } else
                return getSuccessResponse(agreements, requestInfo);
        } else {
            final Error error = new Error();
            error.setCode(1);
            error.setDescription("No Agreements Found!");
            final ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final AgreementRequest agreementRequest, final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        log.info("agreementRequest::" + agreementRequest);
        agreementValidator.validateCreate(agreementRequest, errors);

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        } else if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Agreement agreement = agreementService.createAgreement(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info(agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("/_modify")
    @ResponseBody
    public ResponseEntity<?> modify(@RequestBody @Valid final AgreementRequest agreementRequest, final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        log.info("agreementRequest::" + agreementRequest);
        agreementValidator.validateModify(agreementRequest, errors);
        agreementValidator.validateModifiedData(agreementRequest, errors);

        if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Agreement agreement = agreementService.modifyAgreement(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info(agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("/_renew")
    @ResponseBody
    public ResponseEntity<?> renew(@RequestBody @Valid final AgreementRequest agreementRequest, final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        log.info("agreementRequest::" + agreementRequest);
        agreementValidator.validateRenewal(agreementRequest, errors);

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        } else if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Agreement agreement = agreementService.createRenewal(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("agreement renewal response:" + agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("/_eviction")
    @ResponseBody
    public ResponseEntity<?> eviction(@RequestBody @Valid final AgreementRequest agreementRequest, final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        log.info("agreementRequest::" + agreementRequest);
        agreementValidator.validateEviction(agreementRequest, errors);

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        } else if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Agreement agreement = agreementService.createEviction(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("agreement eviction response:" + agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("/_cancel")
    @ResponseBody
    public ResponseEntity<?> cancel(@RequestBody @Valid final AgreementRequest agreementRequest, final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        log.info("agreementRequest cancel ::" + agreementRequest);
        agreementValidator.validateCancel(agreementRequest, errors);

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        } else if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Agreement agreement = agreementService.createCancellation(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("agreement cancellation response :" + agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("/_objection")
    @ResponseBody
    public ResponseEntity<?> objection(@RequestBody @Valid final AgreementRequest agreementRequest, final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        log.info("agreementRequest cancel ::" + agreementRequest);
        agreementValidator.validateObjection(agreementRequest, errors);

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        } else if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Agreement agreement = agreementService.createObjection(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("agreement objection response:" + agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("/_courtjudgement")
    @ResponseBody
    public ResponseEntity<?> courtjudgement(@RequestBody @Valid final AgreementRequest agreementRequest,
            final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        log.info("agreementRequest cancel ::" + agreementRequest);
        agreementValidator.validateJudgement(agreementRequest, errors);

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        } else if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        final Agreement agreement = agreementService.createJudgement(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("agreement judgement response:" + agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("/_remission")
    @ResponseBody
    public ResponseEntity<?> remission(@RequestBody @Valid final AgreementRequest agreementRequest, final BindingResult errors) {

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        agreementValidator.validateRemissionDetails(agreementRequest, errors);

        if (errors.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        } else if (errors.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(errors);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        final Agreement agreement = agreementService.createRemission(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("agreement remission response:" + agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("_update/{code}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable("code") final String code, @RequestBody final AgreementRequest agreementRequest,
            final BindingResult bindingResult) {

        if (agreementRequest.getAgreement().getSource().equals(Source.DATA_ENTRY))
            agreementValidator.validatePartialCollection(agreementRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateValidationErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        if (agreementRequest.getAgreement().getSource().equals(Source.SYSTEM))
            agreementValidator.validateUpdate(agreementRequest, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(bindingResult);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        if (!(code.equals(agreementRequest.getAgreement().getAcknowledgementNumber())
                || code.equals(agreementRequest.getAgreement().getAgreementNumber())
                        && agreementService.isAgreementExist(code)))
            throw new RuntimeException("code mismatch or no agreement found for this value");

        Agreement agreement = null;
        agreement = agreementService.updateAgreement(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("the response form update agreement call : " + agreementResponse);
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("renewal/_update")
    @ResponseBody
    public ResponseEntity<?> updateRenewal(@RequestBody final AgreementRequest agreementRequest,
            final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
            throw new RuntimeException(" no agreement found with given AcknowledgementNumber");

        agreementValidator.validateUpdate(agreementRequest, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(bindingResult);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        Agreement agreement = null;
        agreement = agreementService.updateRenewal(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("the response form update agreement call : " + agreementResponse);
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("cancel/_update")
    @ResponseBody
    public ResponseEntity<?> updateCancellation(@RequestBody final AgreementRequest agreementRequest,
            final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
            throw new RuntimeException("no agreement found with given AcknowledgementNumber");

        agreementValidator.validateUpdate(agreementRequest, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(bindingResult);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        Agreement agreement = null;
        agreement = agreementService.updateCancellation(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("the response form update agreement call : " + agreementResponse);
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("eviction/_update")
    @ResponseBody
    public ResponseEntity<?> updateEviction(@RequestBody final AgreementRequest agreementRequest,
            final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
            throw new RuntimeException("no agreement found with given AcknowledgementNumber");

        agreementValidator.validateUpdate(agreementRequest, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(bindingResult);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        Agreement agreement = null;
        agreement = agreementService.updateEviction(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("the response form update agreement call : " + agreementResponse);
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("objection/_update")
    @ResponseBody
    public ResponseEntity<?> updateObjection(@RequestBody final AgreementRequest agreementRequest,
            final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
            throw new RuntimeException("no agreement found with given AcknowledgementNumber");

        agreementValidator.validateUpdate(agreementRequest, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(bindingResult);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        Agreement agreement = null;
        agreement = agreementService.updateObjectionAndJudgement(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("the response form update agreement call : " + agreementResponse);
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("judgement/_update")
    @ResponseBody
    public ResponseEntity<?> updateJudgement(@RequestBody final AgreementRequest agreementRequest,
            final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
            throw new RuntimeException("no agreement found with given AcknowledgementNumber");

        agreementValidator.validateUpdate(agreementRequest, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(bindingResult);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        Agreement agreement = null;
        agreement = agreementService.updateObjectionAndJudgement(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info("the response form update agreement call : " + agreementResponse);
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("remission/_update")
    @ResponseBody
    public ResponseEntity<?> updateRmission(@RequestBody final AgreementRequest agreementRequest,
            final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
            throw new RuntimeException(" no agreement found with given AcknowledgementNumber");

        agreementValidator.validateUpdate(agreementRequest, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            final ErrorResponse errRes = populateErrors(bindingResult);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        Agreement agreement = null;
        agreement = agreementService.updateRemission(agreementRequest);
        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("demands/_prepare")
    @ResponseBody
    public ResponseEntity<?> prepareDemand(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult errors, @RequestParam(name = "agreementNumber", required = false) final String agreementNumber,
            @RequestParam(name = "tenantId", required = true) final String tenantId) {

        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }

        final AgreementRequest agreementRequest = new AgreementRequest();
        agreementRequest.setRequestInfo(requestInfoWrapper.getRequestInfo());

        final AgreementCriteria agreementCriteria = new AgreementCriteria();
        agreementCriteria.setTenantId(tenantId);
        agreementCriteria.setAgreementNumber(agreementNumber);

        log.info("before search : " + agreementNumber);
        final Agreement agreement = agreementService.searchAgreement(agreementCriteria, requestInfoWrapper.getRequestInfo())
                .get(0);
        log.info("after search " + agreement);
        agreementRequest.setAgreement(agreement);
        if (Source.DATA_ENTRY.equals(agreement.getSource()))
            agreement.setLegacyDemands(agreementService.prepareLegacyDemands(agreementRequest));
        else
            agreement.setLegacyDemands(agreementService.prepareDemands(agreementRequest));
        log.info("after prepare denmands : " + agreement.getLegacyDemands());

        final List<Agreement> agreements = new ArrayList<>();
        agreements.add(agreement);
        final AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
        log.info(agreementResponse.toString());
        return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
    }

    @PostMapping("dcb/_view")
    @ResponseBody
    public ResponseEntity<?> viewDcb(@ModelAttribute @Valid final AgreementCriteria agreementCriteria,
            @RequestBody final RequestInfoWrapper requestInfoWrapper, final BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            final ErrorResponse errorResponse = populateErrors(bindingResult);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        List<Agreement> agreements = null;
        final AgreementRequest agreementRequest = new AgreementRequest();
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        if ((agreementCriteria.getAgreementNumber() != null || agreementCriteria.getAcknowledgementNumber() != null)
                && agreementCriteria.getTenantId() != null)
            agreements = agreementService.getAgreementsByAgreementNumber(agreementCriteria, requestInfo);
        if (agreements != null && !agreements.isEmpty()) {
            final Agreement agreement = agreements.get(0);
            agreementRequest.setRequestInfo(requestInfo);
            agreementRequest.setAgreement(agreement);
            agreement.setLegacyDemands(agreementService.getDemands(agreementRequest));

            return getSuccessResponse(agreements, requestInfo);
        } else {
            final Error error = new Error();
            error.setCode(1);
            error.setDescription("No Agreements Found!");
            final ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError(error);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private ErrorResponse populateErrors(final BindingResult errors) {
        final ErrorResponse errRes = new ErrorResponse();

        /*
         * ResponseInfo responseInfo = new ResponseInfo(); responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
         * responseInfo.setApi_id(""); errRes.setResponseInfo(responseInfo);
         */
        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while binding request");
        if (errors.hasFieldErrors())
            for (final FieldError errs : errors.getFieldErrors())
                error.getFields().put(errs.getField(), errs.getDefaultMessage());
        errRes.setError(error);
        return errRes;
    }

    private AgreementResponse getAgreementResponse(final List<Agreement> agreements, final RequestInfo requestInfo) {
        final AgreementResponse agreementResponse = new AgreementResponse();
        agreementResponse.setAgreement(agreements);
        agreementResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
        return agreementResponse;
    }

    private ErrorResponse populateValidationErrors(final BindingResult errors) {
        String errorMsg = "";
        if (errors.getFieldError() != null)
            errorMsg = errors.getFieldError().getDefaultMessage();
        final ErrorResponse errRes = new ErrorResponse();
        final ObjectError validationError = errors.getGlobalError();

        final Error error = new Error();
        error.setCode(1);
        error.setMessage(validationError != null ? validationError.getDefaultMessage() : errorMsg);
        error.setDescription(validationError != null ? validationError.getDefaultMessage() : errorMsg);
        errRes.setError(error);
        return errRes;
    }
}
