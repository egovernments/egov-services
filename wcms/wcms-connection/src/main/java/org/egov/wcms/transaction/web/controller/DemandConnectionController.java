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
package org.egov.wcms.transaction.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.demand.contract.PeriodCycle;
import org.egov.wcms.transaction.demand.contract.TaxPeriod;
import org.egov.wcms.transaction.demand.contract.TaxPeriodResponse;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.DemandDetailBean;
import org.egov.wcms.transaction.service.DemandConnectionService;
import org.egov.wcms.transaction.service.WaterConnectionService;
import org.egov.wcms.transaction.util.WcmsConnectionConstants;
import org.egov.wcms.transaction.web.contract.DemandBeanGetRequest;
import org.egov.wcms.transaction.web.contract.DemandDetailBeanReq;
import org.egov.wcms.transaction.web.contract.DemandDetailBeanRes;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.factory.ResponseInfoFactory;
import org.egov.wcms.transaction.web.errorhandler.Error;
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

@RestController
@RequestMapping("/connection")
public class DemandConnectionController {

    @Autowired
    private ResponseInfoFactory responseInfoFactory;
    @Autowired
    private ErrorHandler errHandler;

    @Autowired
    private DemandConnectionService demandConnectionService;

    @Autowired
    private WaterConnectionService waterConnectionService;

    @PostMapping(value = "/getLegacyDemandDetailBeanListByExecutionDate")
    @ResponseBody
    public ResponseEntity<?> getDemandDetailForLegacyAddDemandDetail(
            @ModelAttribute @Valid final DemandBeanGetRequest demandBeanGetRequest,
            @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
            final BindingResult requestBodyBindingResult) {
        final List<DemandDetailBean> dmdDetailBeanList = new ArrayList<>();
        if (demandBeanGetRequest != null && demandBeanGetRequest.getConsumerNumber() != null) {
            final Connection waterConn = waterConnectionService
                    .getWaterConnectionByConsumerNumber(demandBeanGetRequest.getConsumerNumber());
            if (waterConn == null) {
                final ErrorResponse errorResponse = new ErrorResponse();
                final Error error = new Error();
                error.setDescription("Entered ConsumerNumber is not valid");
                errorResponse.setError(error);
            } else {
                final TaxPeriodResponse taxperiodres = demandConnectionService.getTaxPeriodByPeriodCycleAndService(
                        demandBeanGetRequest.getTenantId(), PeriodCycle.HALFYEAR,
                        demandBeanGetRequest.getExecutionDate());
                final List<TaxPeriod> taxPeriodList = taxperiodres.getTaxPeriods();

                try {
                    for (final TaxPeriod tax : taxPeriodList)
                        dmdDetailBeanList.add(createDemandDeatils(demandBeanGetRequest.getTenantId(),
                                WcmsConnectionConstants.WATERDEMANDREASONNAME ,
                                tax.getFinancialYear(),
                                0d, 0d, tax.getCode()));

                } catch (final Exception exception) {

                    return errHandler.getResponseEntityForUnexpectedErrors(requestInfoWrapper.getRequestInfo());
                }
            }
        }
        return getSuccessResponse(dmdDetailBeanList, requestInfoWrapper.getRequestInfo());
    }

    @PostMapping(value = "/_leacydemand")
    @ResponseBody
    public ResponseEntity<?> updateDemandForLegacy(
            @ModelAttribute @Valid final DemandBeanGetRequest demandBeanGetRequest,
            final BindingResult requestBodyBindingResult,
            @RequestBody final DemandDetailBeanReq demandDetailBeanReq) {
        final Connection waterConn = waterConnectionService
                .getWaterConnectionByConsumerNumber(demandBeanGetRequest.getConsumerNumber());
        if (waterConn == null) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription("Entered ConsumerNumber is not valid");
            errorResponse.setError(error);
        } else {
            final RequestInfo requestInfo = demandDetailBeanReq.getRequestInfo();
            for (final DemandDetailBean demanddetBean : demandDetailBeanReq.getDemandDetailBeans()) {
            final List<Demand> pros = demandConnectionService.prepareDemandForLegacy(demanddetBean, waterConn, requestInfo,
                    demandBeanGetRequest);
            if(pros !=null && (!pros.isEmpty() )){
            final DemandResponse demandRes = demandConnectionService.createDemand(pros, demandDetailBeanReq.getRequestInfo());
            if (demandRes != null && demandRes.getDemands() != null && !demandRes.getDemands().isEmpty())
                waterConnectionService.updateConnectionOnChangeOfDemand(demandRes.getDemands().get(0).getId(), waterConn,
                        requestInfo);
            }
            }
        }
        return getSuccessResponse(demandDetailBeanReq.getDemandDetailBeans(), demandDetailBeanReq.getRequestInfo());
    }

    private DemandDetailBean createDemandDeatils(final String tenantId, final String demandReason, final String taxperiod,
            final double amount, final double collection, final String taxPeriodCode) {
        final DemandDetailBean demandDetail = new DemandDetailBean();
        demandDetail.setTaxHeadMasterCode(demandReason);
        demandDetail.setTaxPeriod(taxperiod);
        demandDetail.setTaxPeriodCode(taxPeriodCode);
        demandDetail.setTaxAmount(amount);
        demandDetail.setCollectionAmount(collection);
        demandDetail.setTenantId(tenantId);
        return demandDetail;
    }

    private ResponseEntity<?> getSuccessResponse(final List<DemandDetailBean> dmdDetailBeanList,
            final RequestInfo requestInfo) {
        final DemandDetailBeanRes demandDetailBean = new DemandDetailBeanRes();
        ;
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        responseInfo.setStatus(HttpStatus.OK.toString());
        demandDetailBean.setResponseInfo(responseInfo);
        demandDetailBean.setDemandDetailBeans(dmdDetailBeanList);
        return new ResponseEntity<>(demandDetailBean, HttpStatus.OK);

    }

}
