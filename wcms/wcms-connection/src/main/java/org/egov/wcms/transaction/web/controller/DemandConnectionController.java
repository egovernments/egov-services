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
import org.egov.wcms.transaction.demand.contract.DemandDetail;
import org.egov.wcms.transaction.demand.contract.PeriodCycle;
import org.egov.wcms.transaction.demand.contract.TaxPeriod;
import org.egov.wcms.transaction.demand.contract.TaxPeriodResponse;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.DemandDetailBean;
import org.egov.wcms.transaction.service.DemandConnectionService;
import org.egov.wcms.transaction.service.WaterConnectionService;
import org.egov.wcms.transaction.utils.WcmsConnectionConstants;
import org.egov.wcms.transaction.web.contract.DemandBeanGetRequest;
import org.egov.wcms.transaction.web.contract.DemandDetailBeanReq;
import org.egov.wcms.transaction.web.contract.DemandDetailBeanRes;
import org.egov.wcms.transaction.web.contract.DemandResponse;
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
            final BindingResult modelAttributeBindingResult, @RequestBody @Valid final RequestInfoWrapper requestInfoWrapper) {
        final RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
        final List<DemandDetailBean> dmdDetailBeanList = new ArrayList<>();
        Connection waterConn = null;
        if (demandBeanGetRequest != null &&
                demandBeanGetRequest.getConsumerNumber() != null)
            waterConn = waterConnectionService
                    .getWaterConnectionByConsumerNumber(demandBeanGetRequest.getConsumerNumber(), null,
                            demandBeanGetRequest.getTenantId());
        else if (demandBeanGetRequest != null &&
                demandBeanGetRequest.getLegacyConsumerNumber() != null)
            waterConn = waterConnectionService
                    .getWaterConnectionByConsumerNumber(null, demandBeanGetRequest.getLegacyConsumerNumber(),
                            demandBeanGetRequest.getTenantId());
        if (waterConn == null) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription("Entered ConsumerNumber is not valid Or Duplicate Enties are Present");
            errorResponse.setError(error);
        } else {
            final TaxPeriodResponse taxperiodres = demandConnectionService.getTaxPeriodByPeriodCycleAndService(
                    demandBeanGetRequest.getTenantId(), PeriodCycle.ANNUAL,
                    waterConn.getExecutionDate());
            final List<TaxPeriod> taxPeriodList = taxperiodres.getTaxPeriods();

            if (taxPeriodList.isEmpty())
                return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
            DemandDetailBean dmdDtl = null;
            String demandId = null;
            final DemandDetail demandDet = null;
            List<Demand> savedDemList = new ArrayList<>();
            try {
                savedDemList = demandConnectionService.getDemandsByConsumerCode(demandBeanGetRequest.getConsumerNumber(),
                        demandBeanGetRequest.getTenantId(), requestInfoWrapper);
            } catch (final Exception e) {
                // TODO Auto-generated catch block
                return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
            }
            try {
                for (final TaxPeriod tax : taxPeriodList) {
                    DemandDetail saveddemandDet = null;

                    if (savedDemList.size() > 0)
                        for (final Demand saveddem : savedDemList) {
                            saveddemandDet = null;
                            DemandDetailBean saveddmdDtl = null;
                            if (saveddem.getTaxPeriodFrom().equals(tax.getFromDate())) {
                                demandId = saveddem.getId();
                                for (final String demandReason : WcmsConnectionConstants.DEMAND_REASON_ORDER_MAP.keySet()) {
                                    saveddemandDet = demandConnectionService.demandDetailExist(demandReason,
                                            demandBeanGetRequest.getTenantId(),
                                            tax.getFromDate(), tax.getToDate(), requestInfoWrapper, saveddem.getId());

                                    if (saveddemandDet != null && !dmdDetailBeanList.contains(saveddemandDet))
                                        saveddmdDtl = createDemandDeatils(demandBeanGetRequest.getTenantId(),
                                                demandReason, tax.getFinancialYear(),
                                                saveddemandDet.getTaxAmount().doubleValue(),
                                                saveddemandDet.getCollectionAmount().doubleValue(),
                                                tax.getCode(), saveddemandDet.getId(), demandId);
                                    if (saveddemandDet != null && null != saveddmdDtl && saveddmdDtl.getTaxPeriod() != null)
                                        dmdDetailBeanList.add(saveddmdDtl);

                                }
                            }
                        }
                    else if (demandDet == null)
                        for (final String demandReason : WcmsConnectionConstants.DEMAND_REASON_ORDER_MAP_WITHOUTAVANCE.keySet()) {
                            dmdDtl = createDemandDeatils(demandBeanGetRequest.getTenantId(),
                                    demandReason, tax.getFinancialYear(),
                                    0d, 0d, tax.getCode(), null, null);
                            if (null != dmdDtl && dmdDtl.getTaxPeriod() != null)
                                dmdDetailBeanList.add(dmdDtl);

                        }

                }
            } catch (final Exception exception) {

                return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
            }
        }

        return getSuccessResponse(dmdDetailBeanList, requestInfo);
    }

    @PostMapping(value = "/_leacydemand")
    @ResponseBody
    public ResponseEntity<?> updateDemandForLegacy(
            @ModelAttribute @Valid final DemandBeanGetRequest demandBeanGetRequest,
            final BindingResult requestBodyBindingResult,
            @RequestBody final DemandDetailBeanReq demandDetailBeanReq) {
        final List<Demand> demandList = new ArrayList<>();
        new ArrayList<>();
        Connection waterConn = null;
        if (demandBeanGetRequest != null &&
                demandBeanGetRequest.getConsumerNumber() != null)
            waterConn = waterConnectionService
                    .getWaterConnectionByConsumerNumber(demandBeanGetRequest.getConsumerNumber(), null,
                            demandBeanGetRequest.getTenantId());
        else if (demandBeanGetRequest != null &&
                demandBeanGetRequest.getLegacyConsumerNumber() != null)
            waterConn = waterConnectionService
                    .getWaterConnectionByConsumerNumber(null, demandBeanGetRequest.getLegacyConsumerNumber(),
                            demandBeanGetRequest.getTenantId());
        if (waterConn == null) {
            final ErrorResponse errorResponse = new ErrorResponse();
            final Error error = new Error();
            error.setDescription("Entered ConsumerNumber is not valid Or Duplicate Enties are Present");
            errorResponse.setError(error);
        } else {
            final List<DemandDetailBean> demandDetalBeanList = new ArrayList<DemandDetailBean>();
            for (final DemandDetailBean demandBean : demandDetailBeanReq.getDemandDetailBeans()){
                int retval = Double.compare(demandBean.getTaxAmount(),demandBean.getCollectionAmount());
                if ((!demandDetalBeanList.contains(demandBean)) && ((!demandBean.getTaxHeadMasterCode().equals(WcmsConnectionConstants.WATERCHARGEADVANCE)) && retval >=0))
                    demandDetalBeanList.add(demandBean);
                if((!demandDetalBeanList.contains(demandBean)) && (demandBean.getTaxHeadMasterCode().equals(WcmsConnectionConstants.WATERCHARGEADVANCE)))
                    demandDetalBeanList.add(demandBean);
            }
            final RequestInfo requestInfo = demandDetailBeanReq.getRequestInfo();
            for (final DemandDetailBean demanddetBean : demandDetalBeanList) {
                List<Demand> demand = new ArrayList<>();
                demand = demandConnectionService.prepareDemandForLegacy(demanddetBean, waterConn, requestInfo,
                        demandBeanGetRequest, demandDetailBeanReq);
                demandList.addAll(demand);
            }
        }
        return getDemandSuccessResponse(demandList, demandDetailBeanReq.getRequestInfo());
    }

    private DemandDetailBean createDemandDeatils(final String tenantId,
            final String demandReason, final String taxperiod, final double amount,
            final double collection, final String taxPeriodCode, final String demanddetId, final String demandId) {
        final DemandDetailBean demandDetail = new DemandDetailBean();
        demandDetail.setId(demanddetId);
        demandDetail.setTaxHeadMasterCode(demandReason);
        demandDetail.setTaxPeriod(taxperiod);
        demandDetail.setDemandId(demandId);
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

    private ResponseEntity<?> getDemandSuccessResponse(final List<Demand> dmdDetailBeanList,
            final RequestInfo requestInfo) {
        final DemandResponse demandDetailBean = new DemandResponse();
        ;
        final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
        if(dmdDetailBeanList.isEmpty()){
            System.out.println("dmdDetailBeanList size in empty = " +dmdDetailBeanList.size());
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        }
        else
            responseInfo.setStatus(HttpStatus.OK.toString());
        
        System.out.println("dmdDetailBeanList size in else= " +dmdDetailBeanList.size());
        demandDetailBean.setResponseInfo(responseInfo);
        demandDetailBean.setDemand(dmdDetailBeanList);
        return new ResponseEntity<>(demandDetailBean, HttpStatus.OK);

    }

}