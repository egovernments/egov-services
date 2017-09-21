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
package org.egov.wcms.transaction.utils;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.EstimationNotice;
import org.egov.wcms.transaction.model.WorkOrderFormat;
import org.egov.wcms.transaction.validator.RestConnectionService;
import org.egov.wcms.transaction.web.contract.BoundaryResponse;
import org.egov.wcms.transaction.web.contract.EstimationNoticeRes;
import org.egov.wcms.transaction.web.contract.WaterChargesConfigRes;
import org.egov.wcms.transaction.web.contract.WaterConnectionGetReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.web.contract.WaterConnectionRes;
import org.egov.wcms.transaction.web.contract.WorkOrderRes;
import org.egov.wcms.transaction.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.transaction.web.errorhandler.Error;
import org.egov.wcms.transaction.web.errorhandler.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ConnectionUtils {

    @Autowired
    private RestConnectionService restConnectionService;

    @Autowired
    private ConfigurationManager configurationManager;
    

    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    
    

    public String buildUrlToInvoke(final WaterConnectionGetReq waterConnectionGetReq) {
        final StringBuilder url = new StringBuilder();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getPropertyServiceSearchPathTopic())
                .append("?tenantId=").append(waterConnectionGetReq.getTenantId());
        if (null != waterConnectionGetReq.getName() && !waterConnectionGetReq.getName().isEmpty())
            url.append("&ownerName=" + waterConnectionGetReq.getName());
        if (null != waterConnectionGetReq.getMobileNumber() && !waterConnectionGetReq.getMobileNumber().isEmpty())
            url.append("&mobileNumber=" + waterConnectionGetReq.getMobileNumber());
        if (null != waterConnectionGetReq.getLocality() && !waterConnectionGetReq.getLocality().isEmpty())
            url.append("&locality=" + waterConnectionGetReq.getLocality());
        if (null != waterConnectionGetReq.getDoorNumber() && !waterConnectionGetReq.getDoorNumber().isEmpty())
            url.append("&houseNoBldgApt=" + waterConnectionGetReq.getDoorNumber());
        if (null != waterConnectionGetReq.getRevenueWard() && !waterConnectionGetReq.getRevenueWard().isEmpty())
            url.append("&revenueWard=" + waterConnectionGetReq.getDoorNumber());
        if (null != waterConnectionGetReq.getAadhaarNumber() && !waterConnectionGetReq.getAadhaarNumber().isEmpty())
            url.append("&aadhaarNumber=" + waterConnectionGetReq.getAadhaarNumber());
       
        return url.toString();
    }

    public Boolean getBoundaryByZone(
            final WaterConnectionReq waterConnectionReq) {
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restConnectionService.getBoundaryNum(
                WcmsConnectionConstants.ZONE,
                waterConnectionReq.getConnection().getAddress().getZone(),
                waterConnectionReq.getConnection().getTenantId());
        return boundaryRespose != null && !boundaryRespose.getBoundarys().isEmpty();
    }

    public Boolean getBoundaryByWard(final WaterConnectionReq waterConnectionReq) {
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restConnectionService.getBoundaryNum(
                WcmsConnectionConstants.WARD,
                waterConnectionReq.getConnection().getAddress().getWard(),
                waterConnectionReq.getConnection().getTenantId());
        return boundaryRespose != null && !boundaryRespose.getBoundarys().isEmpty();
    }

    public Boolean getBoundaryByLocation(final WaterConnectionReq waterConnectionReq) {
        BoundaryResponse boundaryRespose = null;
        boundaryRespose = restConnectionService.getBoundaryNum(
                WcmsConnectionConstants.LOCALITY,
                waterConnectionReq.getConnection().getAddress().getLocality(),
                waterConnectionReq.getConnection().getTenantId());
        return boundaryRespose != null && !boundaryRespose.getBoundarys().isEmpty();
    }

    public Boolean getWaterChargeConfigValues(final String tenantId) {
        Boolean isWaterConfigValues = Boolean.FALSE;

        WaterChargesConfigRes waterChargesConfigRes = null;
        waterChargesConfigRes = restConnectionService.getWaterChargesConfig(
                WcmsConnectionConstants.WORKFLOW_REQUIRED_CONFIG_KEY,
                tenantId);
        if (waterChargesConfigRes != null && !waterChargesConfigRes.getWaterConfigurationValue().isEmpty()
                && waterChargesConfigRes.getWaterConfigurationValue().get(0).getValue().equals("YES"))
            isWaterConfigValues = Boolean.TRUE;

        return isWaterConfigValues;
    }
    

    
    public ResponseEntity<?> errorMessageOnConnectionSuccessAndFailure(final WaterConnectionReq waterConnectionRequest,
            final List<Connection> connectionList) {
        if (waterConnectionRequest.getConnection().getId() > 0)
            return getSuccessResponse(connectionList, waterConnectionRequest.getRequestInfo());
        else {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription(WcmsConnectionConstants.CONNECTION_PERSIST_FAILURE);
            errorResponse.setError(error);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    public ResponseEntity<?> getSuccessResponse(final List<Connection> connectionList,
            final RequestInfo requestInfo) {
        final WaterConnectionRes waterConnectionRes = new WaterConnectionRes();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        waterConnectionRes.setResponseInfo(responseInfo);
        waterConnectionRes.setConnections(connectionList);
        return new ResponseEntity<>(waterConnectionRes, HttpStatus.OK);

    }

    public ResponseEntity<?> getSuccessResponseForEstimationNotice(final EstimationNotice estimationNotice,
            final RequestInfo requestInfo) {
        final EstimationNoticeRes estimationNoticeRes = new EstimationNoticeRes();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        estimationNoticeRes.setResponseInfo(responseInfo);
        estimationNoticeRes.setEstimationNotice(estimationNotice);
        return new ResponseEntity<>(estimationNoticeRes, HttpStatus.OK);

    }

    public ResponseEntity<?> getSuccessResponseForWorkOrder(final WorkOrderFormat workOrder,
            final RequestInfo requestInfo) {
        final WorkOrderRes workOrderRes = new WorkOrderRes();
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        workOrderRes.setResponseInfo(responseInfo);
        workOrderRes.setWorkOrder(workOrder);
        return new ResponseEntity<>(workOrderRes, HttpStatus.OK);

    }
    
}
