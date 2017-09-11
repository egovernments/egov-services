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
import org.egov.pgr.domain.model.GrievanceType;
import org.egov.pgr.service.GrievanceTypeService;
import org.egov.pgr.util.CommonValidation;
import org.egov.pgr.util.PgrMasterConstants;
import org.egov.pgr.web.contract.RequestInfoWrapper;
import org.egov.pgr.web.contract.ServiceGetRequest;
import org.egov.pgr.web.contract.ServiceRequest;
import org.egov.pgr.web.contract.ServiceResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/service")
public class GrievanceTypeController {

    private static final Logger logger = LoggerFactory.getLogger(GrievanceTypeController.class);
    public static final String CODE = "code";
    public static final String FIELD = "field";
    public static final String MESSAGE = "message";

    @Autowired
    private GrievanceTypeService grievanceTypeService;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private CommonValidation commonValidation;

    HashMap<String, String> grievanceTypeException = new HashMap<>();

    @PostMapping(value = "/v1/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final ServiceRequest serviceTypeRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Service Create : Request::" + serviceTypeRequest);

        validateServiceRequest(serviceTypeRequest, "create");

        final GrievanceType grievanceType = grievanceTypeService.createServiceType(applicationProperties.getCreateGrievanceTypeTopicName(), applicationProperties.getCreateGrievanceTypeTopicKey(), serviceTypeRequest);
        final List<GrievanceType> grievanceTypes = new ArrayList<>();
        grievanceTypes.add(grievanceType);
        return getSuccessResponse(grievanceTypes, serviceTypeRequest.getRequestInfo());

    }

    @PostMapping(value = "/v1/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final ServiceRequest serviceTypeRequest,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        logger.info("Service Update : Request::" + serviceTypeRequest);
        if (serviceTypeRequest.getService().getServiceCode() == null || serviceTypeRequest.getService().getServiceCode().equals("")) {
            final ErrorResponse errRes = populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        validateUpdateServiceRequest(serviceTypeRequest, "update");

        final GrievanceType service = grievanceTypeService.updateServices(applicationProperties.getUpdateGrievanceTypeTopicName(), applicationProperties.getUpdateGrievanceTypeTopicKey(), serviceTypeRequest);
        final List<GrievanceType> services = new ArrayList<>();
        services.add(service);
        return getSuccessResponse(services, serviceTypeRequest.getRequestInfo());
    }

    @PostMapping("/v1/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final ServiceGetRequest serviceTypeGetRequest,
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
        List<GrievanceType> grievanceTypeList = null;
        try {
            grievanceTypeList = grievanceTypeService.getServiceTypes(serviceTypeGetRequest);
        } catch (final Exception exception) {
            logger.error("Error while processing request " + serviceTypeGetRequest, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }

        return getSuccessResponse(grievanceTypeList, requestInfo);

    }

    private void validateServiceRequest(final ServiceRequest serviceTypeRequest, String mode) {
        addGrievanceCodeNameValidator(serviceTypeRequest, mode);
        addGrievanceNameValidationErrors(serviceTypeRequest);
        addGrievanceNameValidator(serviceTypeRequest, mode);
        addTenantIdValidationErrors(serviceTypeRequest);
        checkMetadataExists(serviceTypeRequest);
        checkCategorySLAValues(serviceTypeRequest);
        checkServiceCodeExists(serviceTypeRequest);
        checkDescriptionLength(serviceTypeRequest);
        commonValidation(serviceTypeRequest.getService());
    }


    private void validateUpdateServiceRequest(final ServiceRequest serviceTypeRequest, String mode) {
        addGrievanceNameValidationErrors(serviceTypeRequest);
        addGrievanceNameValidator(serviceTypeRequest, mode);
        addTenantIdValidationErrors(serviceTypeRequest);
        checkMetadataExists(serviceTypeRequest);
        checkCategorySLAValues(serviceTypeRequest);
        checkDescriptionLength(serviceTypeRequest);
        commonValidation(serviceTypeRequest.getService());
    }

    private void commonValidation(GrievanceType grievanceType) {
        commonValidation.validateCode(grievanceType.getServiceCode());
        commonValidation.validateCodeLength(grievanceType.getServiceCode());
        commonValidation.validateName(grievanceType.getServiceName());
        commonValidation.validateNameLength(grievanceType.getServiceName());
        commonValidation.validateDescriptionLength(grievanceType.getDescription());
    }

    private void addGrievanceNameValidationErrors(final ServiceRequest serviceTypeRequest) {
        final GrievanceType service = serviceTypeRequest.getService();
        if (service.getServiceName() == null || service.getServiceName().isEmpty()) {
            grievanceTypeException.put(CODE, PgrMasterConstants.GRIEVANCETYPE_NAME_MANDATORY_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.GRIEVANCETYPE_NAME_MANADATORY_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.GRIEVANCETYPE_NAME_MANADATORY_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
        }
    }

    private void addGrievanceNameValidator(final ServiceRequest serviceTypeRequest, final String mode) {
        if (grievanceTypeService.checkComplaintNameIfExists(serviceTypeRequest.getService().getServiceName(),
                serviceTypeRequest.getService().getTenantId(), serviceTypeRequest.getService().getServiceCode(), mode)) {
            grievanceTypeException.put(CODE, PgrMasterConstants.GRIEVANCETYPE_NAME_UNIQUE_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.GRIEVANCETYPE_NAME_UNIQUE_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.GRIEVANCETYPE_NAME_UNIQUE_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
        }
    }

    private void addGrievanceCodeNameValidator(final ServiceRequest serviceTypeRequest, final String mode) {
        if (grievanceTypeService.checkComplaintCodeNameIfExists(serviceTypeRequest.getService().getServiceName(),
                serviceTypeRequest.getService().getTenantId(), serviceTypeRequest.getService().getServiceCode(), mode)) {
            grievanceTypeException.put(CODE, PgrMasterConstants.CODENAME_UNIQUE_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.CODENAME_UNIQUE_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.CODENAME_UNIQUE_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
        }
    }

    private void addTenantIdValidationErrors(final ServiceRequest serviceTypeRequest) {
        final GrievanceType grievanceType = serviceTypeRequest.getService();
        if (grievanceType.getTenantId() == null || grievanceType.getTenantId().isEmpty()) {
            grievanceTypeException.put(CODE, PgrMasterConstants.TENANTID_MANDATORY_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.TENANTID_MANADATORY_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.TENANTID_MANADATORY_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
        }
    }


    private void checkMetadataExists(final ServiceRequest serviceTypeRequest) {
        final GrievanceType grievanceType = serviceTypeRequest.getService();
        if (grievanceType.isMetadata()) {
            if (null == grievanceType.getAttributes() || grievanceType.getAttributes().size() <= 0) {
                grievanceTypeException.put(CODE, PgrMasterConstants.ATTRIBUTE_DETAILS_MANDATORY_CODE);
                grievanceTypeException.put(FIELD, PgrMasterConstants.ATTRIBUTE_DETAILS_MANADATORY_FIELD_NAME);
                grievanceTypeException.put(MESSAGE, PgrMasterConstants.ATTRIBUTE_DETAILS_MANADATORY_ERROR_MESSAGE);
                throw new PGRMasterException(grievanceTypeException);
            }
        }
    }

    private void checkCategorySLAValues(final ServiceRequest serviceTypeRequest) {
        final GrievanceType grievanceType = serviceTypeRequest.getService();
        if (null == grievanceType.getCategory()) {
            grievanceTypeException.put(CODE, PgrMasterConstants.CATEGORY_ID_MANDATORY_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.CATEGORY_ID_MANDATORY_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.CATEGORY_ID_MANDATORY_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
        } else if (null == grievanceType.getSlaHours()) {
            grievanceTypeException.put(CODE, PgrMasterConstants.SLA_HOURS_MANDATORY_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.SLA_HOURS_MANDATORY_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.SLA_HOURS_MANDATORY_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
        }
        return;
    }

    private void checkServiceCodeExists(final ServiceRequest serviceTypeRequest) {
        final GrievanceType grievanceType = serviceTypeRequest.getService();
        if (grievanceTypeService.checkServiceCodeIfExists(grievanceType.getServiceCode(), grievanceType.getTenantId())) {
            grievanceTypeException.put(CODE, PgrMasterConstants.SERVICETYPE_TENANTID_NAME_UNIQUE_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.SERVICETYPE_TENANTID_NAME_UNIQUE_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.SERVICETYPE_TENANTID_NAME_UNIQUE_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
        }
    }

    private void checkDescriptionLength(final ServiceRequest serviceTypeRequest) {
        final GrievanceType grievanceType = serviceTypeRequest.getService();
        if (grievanceType.getDescription() != null && !grievanceType.getDescription().isEmpty() && !(grievanceType.getDescription().length() > 0 && grievanceType.getDescription().length() <= 250)) {
            grievanceTypeException.put(CODE, PgrMasterConstants.SERVICETYPE_DESCRIPTION_LENGTH_CODE);
            grievanceTypeException.put(FIELD, PgrMasterConstants.SERVICETYPE_DESCRIPTION_LENGTH_FIELD_NAME);
            grievanceTypeException.put(MESSAGE, PgrMasterConstants.SERVICETYPE_DESCRIPTION_LENGTH_ERROR_MESSAGE);
            throw new PGRMasterException(grievanceTypeException);
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

    private ResponseEntity<?> getSuccessResponse(final List<GrievanceType> grievanceTypeList, final RequestInfo requestInfo) {
        final ServiceResponse serviceTypeResponse = new ServiceResponse();
        serviceTypeResponse.setServices(grievanceTypeList);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        serviceTypeResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<>(serviceTypeResponse, HttpStatus.OK);

    }

}
