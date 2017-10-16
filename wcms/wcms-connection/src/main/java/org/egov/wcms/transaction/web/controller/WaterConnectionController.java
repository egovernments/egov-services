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
package org.egov.wcms.transaction.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.config.ApplicationProperties;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.service.WaterConnectionService;
import org.egov.wcms.transaction.utils.ConnectionUtils;
import org.egov.wcms.transaction.validator.ConnectionValidator;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.web.errorhandler.ErrorHandler;
import org.egov.wcms.transaction.web.errorhandler.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/connection")
@Slf4j
public class WaterConnectionController {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private ConnectionUtils connectionUtils;

    @Autowired
    private ConnectionValidator connectionValidator;

    @Autowired
    private WaterConnectionService waterConnectionService;
    
    @Autowired
    private ErrorHandler errorHandler;
    
    @PostMapping(value = "/_create")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid final WaterConnectionReq waterConnectionRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = connectionValidator.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        final List<Connection> connectionList = new ArrayList<>();
        log.info("WaterConnectionRequest ::" + waterConnectionRequest);
        try{
        waterConnectionService.beforePersistTasks(waterConnectionRequest);
        final List<ErrorResponse> errorResponses = connectionValidator
                .validateWaterConnectionRequest(waterConnectionRequest);
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
        waterConnectionService.generateIdsForWaterConnectionRequest(waterConnectionRequest);
        waterConnectionService.persistBeforeKafkaPush(waterConnectionRequest);
        
        if (waterConnectionRequest.getConnection().getId() > 0) {
            final Connection connection = waterConnectionService.pushConnectionToKafka(
                    applicationProperties.getCreateNewConnectionTopicName(), "newconnection-create",
                    waterConnectionRequest);
            connectionList.add(waterConnectionService.afterPersistTasks(waterConnectionRequest, connection));
        }
        }
        catch (Exception exception) {
            log.error("Error while processing request ", exception);
            return errorHandler.getResponseEntityForUnexpectedErrors(waterConnectionRequest.getRequestInfo());
        }
        return connectionUtils.errorMessageOnConnectionSuccess(waterConnectionRequest, connectionList);
    }

    @PostMapping(value = "/_update")
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody @Valid final WaterConnectionReq waterConnectionRequest,
            final BindingResult errors) {
        if (errors.hasErrors()) {
            final ErrorResponse errRes = connectionValidator.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
        final List<Connection> connectionList = new ArrayList<>();
       try{ 
        waterConnectionService.validateinUpdate(waterConnectionRequest);
        final List<ErrorResponse> errorResponses = connectionValidator
                .validateWaterConnectionRequest(waterConnectionRequest);
        
        if (!errorResponses.isEmpty())
            return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);

        waterConnectionService.prepareConnectionUpdate(waterConnectionRequest);
       if (waterConnectionRequest.getConnection().getId() > 0) {
           final Connection connection = waterConnectionService.pushConnectionToKafka(
                   applicationProperties.getUpdateNewConnectionTopicName(), "newconnection-update",
                   waterConnectionRequest);
           connectionList.add(waterConnectionService.afterPersistTasks(waterConnectionRequest, connection));
       }
       }
       catch (Exception exception) {
           log.error("Error while processing request ", exception);
           return errorHandler.getResponseEntityForUnexpectedErrors(waterConnectionRequest.getRequestInfo());
       }
       return  connectionUtils.getSuccessResponse(connectionList, waterConnectionRequest.getRequestInfo());
      }
    
    
    @PostMapping(value="/_updateLegacyData") 
    @ResponseBody
    public ResponseEntity<?> updateLegacyData(@RequestBody @Valid final WaterConnectionReq waterConnectionRequest, 
    		final BindingResult errors) { 
    	if (errors.hasErrors()) {
            final ErrorResponse errRes = connectionValidator.populateErrors(errors);
            return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
        }
    	List<ErrorResponse> errorResponses = new ArrayList<>();
    	final List<Connection> connectionList = new ArrayList<>();
    	Connection connection = null; 
		if (waterConnectionService.validateLegacyDataForUpdate(waterConnectionRequest)) {
			waterConnectionService.beforePersistTasks(waterConnectionRequest);
			errorResponses = connectionValidator.validateWaterConnectionRequest(waterConnectionRequest);
			if (!errorResponses.isEmpty())
				return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
			if (waterConnectionRequest.getConnection().getId() > 0) {
				 connection = waterConnectionService.pushConnectionToKafka(
						applicationProperties.getLegacyConnectionUpdateTopicName(),
						applicationProperties.getLegacyConnectionUpdateTopicKey(), waterConnectionRequest);
			}
			connectionList.add(connection); 
			return connectionUtils.getSuccessResponse(connectionList, waterConnectionRequest.getRequestInfo());
		} else { 
			return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
		}
    	
    	
    	
    	
    	
    }
    
    @PostMapping("/_search")
    @ResponseBody
    public ResponseEntity<?> search(@ModelAttribute @Valid final WaterConnectionGetReq waterConnectionGetReq,
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

        if (modelAttributeBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);

        if (requestBodyBindingResult.hasErrors())
            return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);

        log.info("Request Received for Search Water Connection : " + waterConnectionGetReq);

        List<Connection> connectionList = null;
        final String urlToInvoke = connectionUtils.buildUrlToInvoke(waterConnectionGetReq);
        try {
            connectionList = waterConnectionService.getConnectionDetails(waterConnectionGetReq, requestInfo, urlToInvoke);
        } catch (final Exception exception) {
            log.error("Error while processing request " + waterConnectionGetReq, exception);
            return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
        }
        return connectionUtils.getSuccessResponse(connectionList, requestInfo);
    }

}
