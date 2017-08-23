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

package org.egov.wcms.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandDetail;
import org.egov.wcms.transaction.demand.contract.DemandRequest;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.demand.contract.Owner;
import org.egov.wcms.transaction.demand.contract.PeriodCycle;
import org.egov.wcms.transaction.demand.contract.TaxHeadMasterCriteria;
import org.egov.wcms.transaction.demand.contract.TaxHeadMasterResponse;
import org.egov.wcms.transaction.demand.contract.TaxPeriodCriteria;
import org.egov.wcms.transaction.demand.contract.TaxPeriodResponse;
import org.egov.wcms.transaction.exception.WaterConnectionException;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.model.DemandDetailBean;
import org.egov.wcms.transaction.model.EstimationCharge;
import org.egov.wcms.transaction.util.WcmsConnectionConstants;
import org.egov.wcms.transaction.web.contract.DemandBeanGetRequest;
import org.egov.wcms.transaction.web.contract.PropertyOwnerInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DemandConnectionService {

    @Autowired
    private ConfigurationManager configurationManager;

    private static final String BUSINESSSERVICE = "WC";

    public List<Demand> prepareDemand(final Demand demandrequest, final WaterConnectionReq waterConnectionRequest) {

        final List<Demand> demandList = new ArrayList<>();
        final String tenantId = waterConnectionRequest.getConnection().getTenantId();
        final String propertyType = waterConnectionRequest.getConnection().getApplicationType();
        final Owner ownerobj = new Owner();
        ownerobj.setTenantId(tenantId);
        ownerobj.setId(waterConnectionRequest.getRequestInfo().getUserInfo().getId());
        final Map<String, Object> feeDetails = new HashMap<>();
        final Demand demand = new Demand();
        final TaxPeriodResponse taxperiodres = getTaxPeriodByPeriodCycleAndService(tenantId, PeriodCycle.ANNUAL, null);
        if (!waterConnectionRequest.getConnection().getEstimationCharge().isEmpty()) {
            final EstimationCharge estimationCharge = waterConnectionRequest.getConnection().getEstimationCharge().get(0);
            feeDetails.put(WcmsConnectionConstants.SPECIALDEPOSITECHARGEDEMANDREASON, 100d);
            if (estimationCharge.getSpecialSecurityCharges() > 0.0)
                feeDetails.put(WcmsConnectionConstants.SPECIALSECURITYCHARGEDEMANDREASON,
                        estimationCharge.getSpecialSecurityCharges());
            if (estimationCharge.getEstimationCharges() > 0.0)
                feeDetails.put(WcmsConnectionConstants.ESIMATIONCHARGEDEMANDREASON, estimationCharge.getEstimationCharges());
            if (estimationCharge.getRoadCutCharges() > 0.0)
                feeDetails.put(WcmsConnectionConstants.ROADCUTCHARGEDEMANDREASON, estimationCharge.getRoadCutCharges());
            if (estimationCharge.getSupervisionCharges() > 0.0)
                feeDetails.put(WcmsConnectionConstants.SUPERVISIONCHARGEREASON, estimationCharge.getSupervisionCharges());
        }
        demand.setTenantId(tenantId);
        demand.setBusinessService(BUSINESSSERVICE);
        demand.setConsumerType(propertyType);
        demand.setConsumerCode(waterConnectionRequest.getConnection().getAcknowledgementNumber());
        final Set<DemandDetail> dmdDetailSet = new HashSet<>();
        for (final String demandReason : feeDetails.keySet())
            dmdDetailSet.add(createDemandDeatils(tenantId, demandReason,
                    (Double) feeDetails.get(demandReason), 0d));

        demand.setOwner(ownerobj);
        demand.setDemandDetails(new ArrayList<>(dmdDetailSet));
        demand.setMinimumAmountPayable(new BigDecimal(1));
        if (taxperiodres != null && !taxperiodres.getTaxPeriods().isEmpty()) {
            demand.setTaxPeriodFrom(taxperiodres.getTaxPeriods().get(0).getFromDate());
            demand.setTaxPeriodTo(taxperiodres.getTaxPeriods().get(0).getToDate());
        }
        demandList.add(demand);

        return demandList;
    }
    
    public PropertyResponse getPropertyDetailsByUpicNo(String propertyIdentifer,String tenantid, RequestInfo requestInfo) {
        StringBuilder url = new StringBuilder();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getPropertyServiceSearchPathTopic()).append("?upicNo=")
                .append(propertyIdentifer)
                .append("&tenantId=").append(tenantid);
        PropertyResponse propResp = null;
        try {
           propResp = new RestTemplate().postForObject(url.toString(), wrapper,
                    PropertyResponse.class);
            System.out.println(propResp!=null ?( propResp.toString() +""+propResp.getProperties().size()):"iisue while binding pt to watertax");
        } catch (Exception e) {
            
            System.out.println(propResp!=null? propResp.toString():"issue with propResp in exception block in WT");
            
            throw new WaterConnectionException("Error while Fetching Data from PropertyTax",
                    "Error while Fetching Data from PropertyTax", requestInfo);
        }

        
        
        return propResp;
    }

    public List<Demand> prepareDemandForLegacy(final DemandDetailBean demandReason, final Connection connection,
            final RequestInfo requestInfo, final DemandBeanGetRequest demandBeanGetRequest) {

        final List<Demand> demandList = new ArrayList<>();
        final String tenantId = connection.getTenantId();
         Owner ownerobj =null;
        PropertyOwnerInfo prop=new PropertyOwnerInfo();
        if(connection.getPropertyIdentifier()!=null){
            PropertyResponse propResp=getPropertyDetailsByUpicNo(connection.getPropertyIdentifier(),tenantId,requestInfo);
           if(propResp!=null && propResp.getProperties().isEmpty() &&
                   propResp.getProperties().get(0)!=null)
               prop=propResp.getProperties().get(0).getOwners().get(0);;
               ownerobj=new Owner();
               ownerobj.setId(prop.getId());
               ownerobj.setTenantId(prop.getTenantId());

        }
        else
        {
            if(connection.getPropertyIdentifier() ==null &&  connection.getUserid()!=null){
                ownerobj=new Owner();
            ownerobj.setId(connection.getUserid());
            ownerobj.setTenantId(tenantId);
            
            }
            
        }
        
       if(ownerobj !=null){
           System.out.println("user Object="+ownerobj);
           
        final Demand demand = new Demand();
        final TaxPeriodResponse taxperiodres = getTaxPeriodByTaxCodeAndService(demandReason.getTaxPeriodCode(), tenantId);
        demand.setTenantId(tenantId);
        demand.setBusinessService(BUSINESSSERVICE);
        demand.setConsumerType(connection.getApplicationType());
        demand.setConsumerCode(connection.getConsumerNumber());
        final Set<DemandDetail> dmdDetailSet = new HashSet<>();
         final String demandreasoncode = "WATERCHARGE";
            dmdDetailSet.add(createLegacyDemandDeatils(
                    tenantId, demandreasoncode,
                    demandReason.getTaxAmount(), demandReason.getCollectionAmount(), demandReason.getTaxPeriodCode()));
        demand.setOwner(ownerobj);
        demand.setDemandDetails(new ArrayList<>(dmdDetailSet));
        demand.setMinimumAmountPayable(new BigDecimal(1));
        if (taxperiodres != null && !taxperiodres.getTaxPeriods().isEmpty()) {
            demand.setTaxPeriodFrom(taxperiodres.getTaxPeriods().get(0).getFromDate());
            demand.setTaxPeriodTo(taxperiodres.getTaxPeriods().get(0).getToDate());
        }
        demandList.add(demand);
        
    }
       return demandList;
    }

    private DemandDetail createDemandDeatils(final String tenantId, final String demandReason, final double amount,
            final double collectedAmount) {
        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setTaxHeadMasterCode(demandReason);
        demandDetail.setTaxAmount(new BigDecimal(amount));
        demandDetail.setCollectionAmount(new BigDecimal(collectedAmount));
        demandDetail.setTenantId(tenantId);
        return demandDetail;

    }

    private DemandDetail createLegacyDemandDeatils(final String tenantId, final String demandReason, final double amount,
            final double collectedAmount, final String taxPeriodCode) {
        final DemandDetail demandDetail = new DemandDetail();
        // getTaxHeadMasterByCodeAndDates(tenantId,demandReason,taxPeriodCode);
        demandDetail.setTaxHeadMasterCode(demandReason);
        demandDetail.setTaxAmount(new BigDecimal(amount));
        demandDetail.setCollectionAmount(new BigDecimal(collectedAmount));
        demandDetail.setTenantId(tenantId);
        return demandDetail;

    }

    public TaxPeriodResponse getTaxPeriodByPeriodCycleAndService(final String tenantId, final PeriodCycle periodCycle,
            final Long date) {
        final TaxPeriodCriteria taxperiodcriteria = new TaxPeriodCriteria();
        taxperiodcriteria.setTenantId(tenantId);
   //     taxperiodcriteria.setFromDate(fromDate);
        taxperiodcriteria.setService(BUSINESSSERVICE);
        taxperiodcriteria.setPeriodCycle(periodCycle);
        final StringBuilder url = new StringBuilder();
        url.append(configurationManager.getBillingDemandServiceHostNameTopic())
                .append(configurationManager.getTaxperidforfinancialYearTopic());
        url.append("?service=").append(taxperiodcriteria.getService()).append("&tenantId=")
                .append(taxperiodcriteria.getTenantId());
               // url.append("&date=").append(date)
        url.append("&periodCycle=").append(taxperiodcriteria.getPeriodCycle());
        return getTaxPeriodServiceResponse(url);
    }

    public TaxPeriodResponse getTaxPeriodByTaxCodeAndService(final String taxCode, final String tenantId) {
        final TaxPeriodCriteria taxperiodcriteria = new TaxPeriodCriteria();
        taxperiodcriteria.setTenantId(tenantId);
        taxperiodcriteria.setService(BUSINESSSERVICE);
        taxperiodcriteria.setCode(taxCode);
        final StringBuilder url = new StringBuilder();
        url.append(configurationManager.getBillingDemandServiceHostNameTopic())
                .append(configurationManager.getTaxperidforfinancialYearTopic());
        url.append("?service=").append(taxperiodcriteria.getService()).append("&tenantId=")
                .append(taxperiodcriteria.getTenantId())
                .append("&code=").append(taxperiodcriteria.getCode());
        return getTaxPeriodServiceResponse(url);
    }

    protected TaxPeriodResponse getTaxPeriodServiceResponse(final StringBuilder url) {
        TaxPeriodResponse taxPeriod;
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);

        taxPeriod = new RestTemplate().postForObject(url.toString(), request, TaxPeriodResponse.class);

        if (taxPeriod != null && !taxPeriod.getTaxPeriods().isEmpty())
            return taxPeriod;
        else
            throw new WaterConnectionException("No TaxPeriod Present For Current Financial Year",
                    "No TaxPeriod Present For Current Financial Year", requestInfo);
    }

    public TaxHeadMasterResponse getTaxHeadMasterByCodeAndDates(final String tenantId, final String demandReason,
            final String taxPeriodCode) {
        TaxHeadMasterResponse taxHeadMaster = null;

        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final TaxPeriodResponse taxperiodres = getTaxPeriodByTaxCodeAndService(taxPeriodCode, tenantId);
        final Set<String> reasonCode = new HashSet<>();
        reasonCode.add(demandReason);
        final TaxHeadMasterCriteria taxheadcriteria = new TaxHeadMasterCriteria();
        taxheadcriteria.setTenantId(tenantId);
        taxheadcriteria.setValidFrom(taxperiodres.getTaxPeriods().get(0).getFromDate());
        taxheadcriteria.setValidTill(taxperiodres.getTaxPeriods().get(0).getToDate());
        taxheadcriteria.setService(BUSINESSSERVICE);
        taxheadcriteria.setCode(reasonCode);

        final StringBuilder url = new StringBuilder();
        url.append(configurationManager.getBillingDemandServiceHostNameTopic())
                .append(configurationManager.getTaxperidforfinancialYearTopic());
        url.append("?service=").append(taxheadcriteria.getService()).append("&tenantId=")
                .append(taxheadcriteria.getTenantId()).append("&code=").append(taxheadcriteria.getCode());
        taxHeadMaster = new RestTemplate().postForObject(url.toString(), request, TaxHeadMasterResponse.class);

        if (taxHeadMaster != null && taxHeadMaster.getTaxHeadMasters() != null && !taxHeadMaster.getTaxHeadMasters().isEmpty())
            return taxHeadMaster;
        else
            throw new WaterConnectionException("No TaxHeadMatser Present For TaxHeadMaster  " + taxheadcriteria.getCode()
                    + "and TaxHeadStarts from " + taxheadcriteria.getValidFrom(),
                    "No TaxHeadMatser Present For TaxHeadMaster  " + taxheadcriteria.getCode() + "and TaxHeadStarts from "
                            + taxheadcriteria.getValidFrom(),
                    requestInfo);
    }

    public DemandResponse createDemand(final List<Demand> demands, final RequestInfo requestInfo) {
        final DemandRequest demandRequest = new DemandRequest();
        demandRequest.setRequestInfo(requestInfo);
        demandRequest.setDemands(demands);

        final String url = configurationManager.getBillingDemandServiceHostNameTopic() +
                configurationManager.getCreatebillingDemandServiceTopic();

        final DemandResponse demres = new RestTemplate().postForObject(url, demandRequest, DemandResponse.class);

        if (demres.getDemands().isEmpty())
            throw new WaterConnectionException("Error While Demand generation", "Error While Demand generation", requestInfo);
        return demres;
    }

}
