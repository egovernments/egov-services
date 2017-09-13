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

package org.egov.tradelicense.web.controller;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.service.TLConfigurationService;
import org.egov.tradelicense.web.contract.TLConfigurationGetRequest;
import org.egov.tradelicense.web.contract.TLConfigurationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/configurations")
public class TLConfigurationController {

    private static final Logger logger = LoggerFactory.getLogger(TLConfigurationController.class);

    @Autowired
    private TLConfigurationService tlConfigurationService;

    @Autowired
    private ResponseInfoFactory responseInfoFactory;

    @PostMapping("/v1/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid TLConfigurationGetRequest tlConfigurationGetRequest,
            BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
            BindingResult requestBodyBindingResult) {
        RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (modelAttributeBindingResult.hasErrors()) {
            throw new CustomBindException(modelAttributeBindingResult, requestInfo);
        }
        if (requestBodyBindingResult.hasErrors())
            throw new CustomBindException(requestBodyBindingResult, requestInfo);

        // Call service
        Map<String, List<String>> tlConfigurationKeyValuesList = tlConfigurationService
                .getTLConfigurations(tlConfigurationGetRequest);

        return getSuccessResponse(tlConfigurationKeyValuesList, requestInfo);
    }

    /**
     * Populate Response object and return tlConfigurationsList
     * 
     * @param tlConfigurationKeyValuesList
     * @param requestInfo
     * @return
     */
    private ResponseEntity<?> getSuccessResponse(Map<String, List<String>> tlConfigurationKeyValuesList,
            RequestInfo requestInfo) {
        TLConfigurationResponse tlConfigurationRes = new TLConfigurationResponse();
        tlConfigurationRes.setTlConfiguration(tlConfigurationKeyValuesList);
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        tlConfigurationRes.setResponseInfo(responseInfo);
        return new ResponseEntity<TLConfigurationResponse>(tlConfigurationRes, HttpStatus.OK);

    }

}
