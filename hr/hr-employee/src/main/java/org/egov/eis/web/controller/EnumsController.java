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
import java.util.Map;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.eis.model.enums.BloodGroup;
import org.egov.eis.model.enums.CourtOrderType;
import org.egov.eis.model.enums.DisciplinaryActions;
import org.egov.eis.model.enums.DisciplinaryAuthority;
import org.egov.eis.model.enums.MaritalStatus;
import org.egov.eis.web.contract.BloodGroupResponse;
import org.egov.eis.web.contract.CourtOrderTypeResponse;
import org.egov.eis.web.contract.DisciplinaryActionsResponse;
import org.egov.eis.web.contract.DisciplinaryAuthorityResponse;
import org.egov.eis.web.contract.MaritalStatusResponse;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnumsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnumsController.class);

    @Autowired
    private RequestValidator requestValidator;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    /**
     * Maps Post Requests for _search & returns ResponseEntity of either BloodGroupResponse type or ErrorResponse type
     *
     * @param requestInfoWrapper
     * @param bindingResult
     * @return ResponseEntity<?>
     */
    @PostMapping("/bloodgroups/_search")
    @ResponseBody
    public ResponseEntity<?> searchBloodGroup(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        final ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo, null,
                bindingResult);

        if (errorResponseEntity != null)
            return errorResponseEntity;

        // Call service
        final List<Map<String, String>> bloodGroups = BloodGroup.getBloodGroups();
        LOGGER.debug("BloodGroups : " + bloodGroups);

        return getSuccessResponseForSearchBloodGroup(bloodGroups, requestInfo);
    }

    /**
     * Maps Post Requests for _search & returns ResponseEntity of either MaritalStatusResponse type or ErrorResponse type
     *
     * @param requestInfoWrapper
     * @param bindingResult
     * @return ResponseEntity<?>
     */
    @PostMapping("/maritalstatuses/_search")
    @ResponseBody
    public ResponseEntity<?> searchMaritalStatus(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        final ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo, null,
                bindingResult);

        if (errorResponseEntity != null)
            return errorResponseEntity;

        // Call service
        final List<String> maritalStatuses = MaritalStatus.getAllObjectValues();

        LOGGER.debug("maritalStatuses : " + maritalStatuses);

        return getSuccessResponseForSearchMaritalStatus(maritalStatuses, requestInfo);
    }

    /**
     * Maps Post Requests for _search & returns ResponseEntity of either DisciplinaryActionsResponse type or ErrorResponse type
     *
     * @param requestInfoWrapper
     * @param bindingResult
     * @return ResponseEntity<?>
     */

    @PostMapping("/disciplinaryactions/_search")
    @ResponseBody
    public ResponseEntity<?> searchDisciplinaryActions(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        final ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo, null,
                bindingResult);

        if (errorResponseEntity != null)
            return errorResponseEntity;

        // Call service
        final List<Map<String, String>> disciplinaryActions = DisciplinaryActions.getDisciplinaryActions();
        LOGGER.debug("disciplinaryActions : " + disciplinaryActions);

        return getSuccessResponseForDisciplinaryActions(disciplinaryActions, requestInfo);
    }

    /**
     * Maps Post Requests for _search & returns ResponseEntity of either CourtOrderTypeResponse type or ErrorResponse type
     *
     * @param requestInfoWrapper
     * @param bindingResult
     * @return ResponseEntity<?>
     */

    @PostMapping("/courtordertype/_search")
    @ResponseBody
    public ResponseEntity<?> searchCourtOrderType(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult bindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        final ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo, null,
                bindingResult);

        if (errorResponseEntity != null)
            return errorResponseEntity;

        // Call service
        final List<Map<String, String>> courtOrderTypes = CourtOrderType.getCourtOrderTypes();
        LOGGER.debug("courtOrderTypes : " + courtOrderTypes);

        return getSuccessResponseForCourtOrderType(courtOrderTypes, requestInfo);
    }

    /**
     * Populate BloodGroupResponse object & returns ResponseEntity of type BloodGroupResponse containing ResponseInfo & List of
     * BloodGroup
     *
     * @param bloodGroups
     * @param requestInfo
     * @return ResponseEntity<?>
     */
    private ResponseEntity<?> getSuccessResponseForSearchBloodGroup(final List<Map<String, String>> bloodGroups,
            final RequestInfo requestInfo) {
        final BloodGroupResponse bloodGroupResponse = new BloodGroupResponse();
        bloodGroupResponse.setBloodGroup(bloodGroups);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        bloodGroupResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<BloodGroupResponse>(bloodGroupResponse, HttpStatus.OK);
    }

    /**
     * Populate MaritalStatusResponse object & returns ResponseEntity of type MaritalStatusResponse containing ResponseInfo & List
     * of MaritalStatus
     *
     * @param maritalStatuses
     * @param requestInfo
     * @return ResponseEntity<?>
     */
    private ResponseEntity<?> getSuccessResponseForSearchMaritalStatus(final List<String> maritalStatuses,
            final RequestInfo requestInfo) {
        final MaritalStatusResponse maritalStatusResponse = new MaritalStatusResponse();
        maritalStatusResponse.setMaritalStatus(maritalStatuses);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        maritalStatusResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<MaritalStatusResponse>(maritalStatusResponse, HttpStatus.OK);
    }

    /**
     * Populate DisciplinaryActionsResponse object & returns ResponseEntity of type DisciplinaryActionsResponse containing
     * ResponseInfo & List of DisciplinaryActions
     *
     * @param disciplinaryActions
     * @param requestInfo
     * @return ResponseEntity<?>
     */

    private ResponseEntity<?> getSuccessResponseForDisciplinaryActions(final List<Map<String, String>> disciplinaryActions,
            final RequestInfo requestInfo) {
        final DisciplinaryActionsResponse disciplinaryActionsResponse = new DisciplinaryActionsResponse();
        disciplinaryActionsResponse.setDisciplinaryActions(disciplinaryActions);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        disciplinaryActionsResponse.setResponseInfo(responseInfo);
        return new ResponseEntity<DisciplinaryActionsResponse>(disciplinaryActionsResponse, HttpStatus.OK);
    }

    /**
     * Populate CourtOrderTypeResponse object & returns ResponseEntity of type CourtOrderTypeResponse containing ResponseInfo &
     * List of CourtOrderType
     *
     * @param courtOrderType
     * @param requestInfo
     * @return ResponseEntity<?>
     */

    private ResponseEntity<?> getSuccessResponseForCourtOrderType(final List<Map<String, String>> courtOrderTypes,
            final RequestInfo requestInfo) {
        final CourtOrderTypeResponse courtOrderRes = new CourtOrderTypeResponse();
        courtOrderRes.setCourtOrderType(courtOrderTypes);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        courtOrderRes.setResponseInfo(responseInfo);
        return new ResponseEntity<CourtOrderTypeResponse>(courtOrderRes, HttpStatus.OK);
    }

    @PostMapping("/disciplinaryauthority/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final DisciplinaryAuthorityResponse disciplinaryAuthorityResponse,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        final ResponseEntity<?> errorResponseEntity = requestValidator.validateSearchRequest(requestInfo, null,
                requestBodyBindingResult);

        if (errorResponseEntity != null)
            return errorResponseEntity;
        // Call service
        final List<Map<String, String>> disciplinaryAuthorities = DisciplinaryAuthority.getDisciplinaryAuthority();

        return getDisciplianryAuthorityResponse(disciplinaryAuthorities, requestInfo);
    }

    /**
     * Populate DisciplinaryAuthorityResponse object & returns ResponseEntity of type DisciplinaryAuthorityResponse containing
     * ResponseInfo & List of DisciplinaryAuthority
     *
     * @param DisciplinaryAuthorities
     * @param requestInfo
     * @return ResponseEntity<?>
     */
    private ResponseEntity<?> getDisciplianryAuthorityResponse(final List<Map<String, String>> disciplinaryAuthorities,
            final RequestInfo requestInfo) {
        final DisciplinaryAuthorityResponse disciplinaryAuthoritiesRes = new DisciplinaryAuthorityResponse();
        disciplinaryAuthoritiesRes.setDisciplinaryAuthority(disciplinaryAuthorities);
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        disciplinaryAuthoritiesRes.setResponseInfo(responseInfo);
        return new ResponseEntity<DisciplinaryAuthorityResponse>(disciplinaryAuthoritiesRes, HttpStatus.OK);
    }
}