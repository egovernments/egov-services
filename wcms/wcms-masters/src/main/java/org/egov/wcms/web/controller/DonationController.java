/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.config.ApplicationProperties;
import org.egov.wcms.model.Donation;
import org.egov.wcms.service.DonationService;
import org.egov.wcms.util.WcmsConstants;
import org.egov.wcms.web.contract.DonationGetRequest;
import org.egov.wcms.web.contract.DonationRequest;
import org.egov.wcms.web.contract.DonationResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.Error;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.egov.wcms.web.errorhandlers.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/donation")
public class DonationController {

    private static final Logger logger = LoggerFactory.getLogger(DonationController.class);

    @Autowired
    private DonationService donationService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ApplicationProperties applicationProperties;

    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final DonationRequest donationRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Donation Create Request::" + donationRequest);

        final List<ErrorResponse> errorResponses = validateDonationRequest(donationRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final Donation donation = donationService.sendMessage(applicationProperties.getCreateDonationTopicName(),
                "donation-create", donationRequest);
        final List<Donation> donationList = new ArrayList<>();
        donationList.add(donation);
        return getSuccessResponse(donationList, donationRequest.getRequestInfo());
    }

    @PostMapping(value = "/{donationId}/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final DonationRequest donationRequest,
            final BindingResult errors,@PathVariable("donationId") final Long donationId) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Donation Update Request::" + donationRequest);
        donationRequest.getDonation().setId(donationId);

        final List<ErrorResponse> errorResponses = validateDonationRequest(donationRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        final Donation donation = donationService.sendMessage(applicationProperties.getCreateDonationTopicName(),
                "donation-update", donationRequest);
        final List<Donation> donationList = new ArrayList<>();
        donationList.add(donation);
        return getSuccessResponse(donationList, donationRequest.getRequestInfo());
    }

    @PostMapping("_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final DonationGetRequest donationGetRequest,
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
        List<Donation> donationList = null;
        // logger.info("+++===Validation Response is something like this +++=== : " +
        // newConnectionValidator.validateNewConnectionBusinessRules());
        try {
            donationList = donationService.getDonationList(donationGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + donationGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(donationList, requestInfo);

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

    private List<ErrorResponse> validateDonationRequest(final DonationRequest donationRequest) {
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        final ErrorResponse errorResponse = new ErrorResponse();
        final Error error = getError(donationRequest);
        errorResponse.setError(error);
        if (!errorResponse.getErrorFields().isEmpty())
            errorResponses.add(errorResponse);
        return errorResponses;
    }

    private Error getError(final DonationRequest donationRequest) {
        final List<ErrorField> errorFields = getErrorFields(donationRequest);
        return Error.builder().code(HttpStatus.BAD_REQUEST.value())
                .message(WcmsConstants.INVALID_DONATION_REQUEST_MESSAGE)
                .errorFields(errorFields)
                .build();
    }

    private List<ErrorField> getErrorFields(final DonationRequest donationRequest) {
        final Donation donation = donationRequest.getDonation();
        final List<ErrorField> errorFields = new ArrayList<>();
        checkPropertyTypeValue(errorFields, donation);
        checkUsageTypeValue(errorFields, donation);
        checkCategoryValue(errorFields, donation);
        checkPipeSizeValues(errorFields, donation);
        checkDonationAmountValues(errorFields, donation);
        checkFromToDateValues(errorFields, donation);
        return errorFields;
    }

    private void checkPropertyTypeValue(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getPropertyType() == null || donation.getPropertyType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PROPERTYTYPE_MANDATORY_CODE)
                    .message(WcmsConstants.PROPERTYTYPE_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PROPERTYTYPE_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkUsageTypeValue(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getUsageType() == null || donation.getUsageType().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.USAGETYPE_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.USAGETYPE_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.USAGETYPE_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkCategoryValue(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getCategory() == null || donation.getCategory().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.CATEGORY_NAME_MANDATORY_CODE)
                    .message(WcmsConstants.CATEGORY_NAME_MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.CATEGORY_NAME_MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkPipeSizeValues(final List<ErrorField> errorFields, final Donation donation) {
        if ((donation.getMaxHSCPipeSize() == null || donation.getMaxHSCPipeSize().isEmpty()) &&
                donation.getMinHSCPipeSize() == null || donation.getMinHSCPipeSize().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.PIPESIZE_SIZEINMM_MANDATORY_CODE)
                    .message(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.PIPESIZE_SIZEINMM__MANADATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkDonationAmountValues(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getDonationAmount() == null || donation.getDonationAmount().isEmpty()) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.DONATION_MANDATORY_CODE)
                    .message(WcmsConstants.DONATION_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.DONATION_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private void checkFromToDateValues(final List<ErrorField> errorFields, final Donation donation) {
        if (donation.getFromDate() == null || donation.getToDate() == null) {
            final ErrorField errorField = ErrorField.builder()
                    .code(WcmsConstants.FROMTO_MANDATORY_CODE)
                    .message(WcmsConstants.FROMTO_MANDATORY_ERROR_MESSAGE)
                    .field(WcmsConstants.FROMTO_MANDATORY_FIELD_NAME)
                    .build();
            errorFields.add(errorField);
        }
    }

    private ResponseEntity<?> getSuccessResponse(final List<Donation> donationList, final RequestInfo requestInfo) {
        final DonationResponse donationResponse = new DonationResponse();
        donationResponse.setDonations(donationList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        donationResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(donationResponse, HttpStatus.OK);

    }

}
