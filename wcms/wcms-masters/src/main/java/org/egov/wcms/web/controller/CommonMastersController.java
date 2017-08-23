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
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.model.CommonDataModel;
import org.egov.wcms.model.enums.ApplicationType;
import org.egov.wcms.model.enums.PlantType;
import org.egov.wcms.model.enums.ReservoirType;
import org.egov.wcms.model.enums.ServiceChargeType;
import org.egov.wcms.model.enums.ServiceType;
import org.egov.wcms.web.contract.CommonEnumResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.web.errorhandlers.ErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/master")
public class CommonMastersController {

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @RequestMapping(value = "/_getapplicationtypes")
    public ResponseEntity<?> getApplicationTypeEnum(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult,
                    requestInfoWrapper.getRequestInfo());

        final List<CommonDataModel> modelList = new ArrayList<>();
        for (final ApplicationType key : ApplicationType.values())
            modelList.add(new CommonDataModel(key.name(), key));
        return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
    }

    @RequestMapping(value = "/_getreservoirtypes")
    public ResponseEntity<?> getReservoirTypeEnum(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult,
                    requestInfoWrapper.getRequestInfo());

        final List<CommonDataModel> modelList = new ArrayList<>();
        for (final ReservoirType key : ReservoirType.values())
            modelList.add(new CommonDataModel(key.name(), key));
        return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
    }

    @RequestMapping(value = "/_getplanttypes")
    public ResponseEntity<?> getPlantTypeEnum(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult,
                    requestInfoWrapper.getRequestInfo());

        final List<CommonDataModel> modelList = new ArrayList<>();
        for (final PlantType key : PlantType.values())
            modelList.add(new CommonDataModel(key.name(), key));
        return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
    }
    
    @RequestMapping(value="/_getservicetypes")
    public ResponseEntity<?> getServiceTypes(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            BindingResult requestBodyBindingResult){
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult,
                    requestInfoWrapper.getRequestInfo());
        List<CommonDataModel> modelList = new ArrayList<>();
        for(final ServiceType key : ServiceType.values())
            modelList.add(new CommonDataModel(key.name(),key));
        return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
    }
    
    @RequestMapping(value="/_getservicechargetypes")
    public ResponseEntity<?> getAllServiceChargeTypes(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult){
        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult,
                    requestInfoWrapper.getRequestInfo());
        List<CommonDataModel> modelList = new ArrayList<>();
        for(final ServiceChargeType key : ServiceChargeType.values())
            modelList.add(new CommonDataModel(key.name(),key));
        return getSuccessResponse(modelList, requestInfoWrapper.getRequestInfo());
    }

    private ResponseEntity<?> getSuccessResponse(final List<CommonDataModel> modelList,
            final RequestInfo requestInfo) {
        final CommonEnumResponse response = new CommonEnumResponse();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.CREATED.toString());
        response.setResponseInfo(responseInfo);
        response.setDataModelList(modelList);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

}
