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
import org.egov.wcms.transaction.demand.contract.DemandDetailResponse;
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
import org.egov.wcms.transaction.model.ConnectionOwner;
import org.egov.wcms.transaction.model.DemandDetailBean;
import org.egov.wcms.transaction.utils.WcmsConnectionConstants;
import org.egov.wcms.transaction.web.contract.DemandBeanGetRequest;
import org.egov.wcms.transaction.web.contract.DemandDetailBeanReq;
import org.egov.wcms.transaction.web.contract.PropertyOwnerInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;;

@Service
public class DemandConnectionService {

    @Autowired
    private ConfigurationManager configurationManager;

    @Autowired
    private WaterConnectionService waterConnectionService;

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
        final TaxPeriodResponse taxperiodres = getTaxPeriodByPeriodCycleAndService(tenantId, PeriodCycle.ANNUAL, 1491004800000l);
           feeDetails.put(WcmsConnectionConstants.WATERCONNECTIONDEPOSITETAXHEADREASON, waterConnectionRequest.getConnection().getDonationCharge());
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
        System.out.println("demand for Deposite"+ demand.getConsumerCode());
        return demandList;
    }

    public DemandDetail demandDetailExist(final String demandreasoncode, final String tenantId,
            final Long fromDate, final Long toDate, final RequestInfoWrapper requestInfoWrapper, final String demandId) {
        DemandDetail demandDetExist = null;
        final StringBuilder builder = new StringBuilder();
        builder.append(configurationManager.getBillingDemandServiceHostNameTopic() +
                configurationManager.getSearchDemandDEtailExist());
        builder.append("?taxHeadCode=").append(demandreasoncode).append("&periodFrom=").append(fromDate).append("&periodTo=")
                .append(toDate).append("&demandId=").append(demandId).append("&tenantId=").append(tenantId);
        final DemandDetailResponse demres = new RestTemplate().postForObject(builder.toString(),
                requestInfoWrapper, DemandDetailResponse.class);

        if (!demres.getDemandDetails().isEmpty())
            demandDetExist = demres.getDemandDetails().get(0);
        return demandDetExist;
    }

    public PropertyResponse getPropertyDetailsByUpicNo(final String propertyIdentifer, final String tenantid,
            final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getPropertyServiceSearchPathTopic()).append("?upicNumber=")
                .append(propertyIdentifer)
                .append("&tenantId=").append(tenantid);
        PropertyResponse propResp = null;
        try {
            propResp = new RestTemplate().postForObject(url.toString(), wrapper,
                    PropertyResponse.class);
            System.out.println(propResp != null ? propResp.toString() + "" + propResp.getProperties().size()
                    : "iisue while binding pt to watertax");
        } catch (final Exception e) {

            System.out.println(propResp != null ? propResp.toString() : "issue with propResp in exception block in WT");

            throw new WaterConnectionException("Error while Fetching Data from PropertyTax",
                    "Error while Fetching Data from PropertyTax", requestInfo);
        }

        return propResp;
    }

    public List<Demand> prepareDemandForLegacy(final DemandDetailBean demandReason, final Connection connection,
            final RequestInfo requestInfo, final DemandBeanGetRequest demandBeanGetRequest,
            final DemandDetailBeanReq demandDetailBeanReq) {
        DemandResponse demandRes = null;
        final List<Demand> demandList = new ArrayList<>();
        final List<Demand> demandResList = new ArrayList<>();
        final String tenantId = connection.getTenantId();
        Owner ownerobj = null;
        Demand demand = null;
        PropertyOwnerInfo prop=new PropertyOwnerInfo();
        if (connection.getPropertyIdentifier() != null) {
            final PropertyResponse propResp = getPropertyDetailsByUpicNo(connection.getPropertyIdentifier(), tenantId,
                    requestInfo);
            if (propResp != null && !propResp.getProperties().isEmpty() &&
                    propResp.getProperties().get(0) != null) {
                prop = propResp.getProperties().get(0).getOwners().get(0);
                ;
                ownerobj = new Owner();
                ownerobj.setId(prop.getId());
                ownerobj.setTenantId(prop.getTenantId());
            }

        } else {
            ConnectionOwner connOwner=  waterConnectionService.getConnectionOwner(connection.getId(),connection.getTenantId());
            if (connection.getPropertyIdentifier() == null && connOwner !=null  ) {
            System.out.println("connOwner is Present"+ connOwner.getId());
            ownerobj = new Owner();
            ownerobj.setId(connOwner.getOwnerid());
            ownerobj.setTenantId(tenantId);

        }
            }

        if (ownerobj != null) {
            System.out.println("user Object=" + ownerobj);
            System.out.println("connOwner is Present"+ ownerobj.getId());
            final TaxPeriodResponse taxperiodres = getTaxPeriodByTaxCodeAndService(demandReason.getTaxPeriodCode(), tenantId);
            Boolean savedDeamnd = Boolean.FALSE;
            final RequestInfoWrapper requestInfowrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();

            final List<Demand> demsavedList = getDemandsByConsumerCodeAndTaxPeriod(demandBeanGetRequest.getConsumerNumber(),
                    demandBeanGetRequest.getTenantId(), requestInfowrapper);
            for (final Demand dem : demsavedList){
                if (dem.getTaxPeriodFrom().equals(taxperiodres.getTaxPeriods().get(0).getFromDate())
                        && dem.getTaxPeriodTo().equals(taxperiodres.getTaxPeriods().get(0).getToDate())) {
                    savedDeamnd = Boolean.TRUE;
                    demand = dem;
                }
            }
            if (demand == null && demandReason.getDemandId() != null)
                demand = getDemandById(demandReason.getDemandId(),
                        demandBeanGetRequest.getTenantId(), requestInfo);
            else if (demand == null)
                demand = new Demand();

            if (demand != null && demand.getId() == null) {
                demand.setTenantId(tenantId);
                demand.setBusinessService(BUSINESSSERVICE);
                demand.setConsumerType(connection.getApplicationType());
                demand.setConsumerCode(connection.getConsumerNumber());
                demand.setOwner(ownerobj);
            }
            Boolean demandDetExist=Boolean.FALSE;
            final Set<DemandDetail> dmdDetailSet = new HashSet<>();
            for(DemandDetail demTemp:(!demand.getDemandDetails().isEmpty() ? demand.getDemandDetails():dmdDetailSet)){
                if(demTemp.getTaxHeadMasterCode().equals(demandReason.getTaxHeadMasterCode()))
                    demandDetExist=Boolean.TRUE;
            }
            
            if(!demandDetExist)
            dmdDetailSet.add(createLegacyDemandDeatils(
                    tenantId, demandReason.getTaxHeadMasterCode(),
                    demandReason.getTaxAmount(), demandReason.getCollectionAmount(), demandReason.getTaxPeriodCode(),
                    demandReason.getId(),
                    demandReason));
            
            if(!dmdDetailSet.isEmpty()){
            demand.getAuditDetail();
            if (savedDeamnd)
                dmdDetailSet.addAll(demand.getDemandDetails());
            demand.setDemandDetails(new ArrayList<>(dmdDetailSet));
            if (demand != null && demand.getId() == null) {
                demand.setMinimumAmountPayable(new BigDecimal(1));
                if (taxperiodres != null && !taxperiodres.getTaxPeriods().isEmpty()) {
                    demand.setTaxPeriodFrom(taxperiodres.getTaxPeriods().get(0).getFromDate());
                    demand.setTaxPeriodTo(taxperiodres.getTaxPeriods().get(0).getToDate());
                }
            }
            demandList.add(demand);

            if (savedDeamnd) {
                for (final DemandDetail demDet : demand.getDemandDetails()){
                    if (demandReason.getTaxHeadMasterCode().equals(demDet.getTaxHeadMasterCode()))
                        if (demDet.getId() == null) {
                            demandRes = updateDemand(demandList, demandDetailBeanReq.getRequestInfo());
                        } else{
                            demandRes = updateDemandCollection(demandList, demandDetailBeanReq.getRequestInfo());
                        }
                }
            } else if (demand != null && demand.getId() == null) {
                demandRes = createDemand(demandList, demandDetailBeanReq.getRequestInfo());
                waterConnectionService.updateConnectionOnChangeOfDemand(demandRes.getDemands().get(0).getId(), connection,
                        requestInfo);
            } else if (!savedDeamnd && demand != null && demand.getId() != null)
                demandRes = updateDemandCollection(demandList, demandDetailBeanReq.getRequestInfo());

       
        demandResList.addAll(demandRes.getDemands());
            }
        }
        else
        {
            System.out.println("Owner is not present for this Record");
            throw new WaterConnectionException("Owner is not present for this Record", "Owner is not present for this Record", requestInfo);
        }
        return demandResList;
    }

    private DemandDetail createDemandDeatils(final String tenantId, final String demandReason, final double amount,
            final double collectedAmount) {
        final DemandDetail demandDetail = new DemandDetail();
        demandDetail.setTaxHeadMasterCode(demandReason);
        demandDetail.setTaxAmount(BigDecimal.valueOf(amount));
        demandDetail.setCollectionAmount(BigDecimal.valueOf(collectedAmount));
        demandDetail.setTenantId(tenantId);
        System.out.println("demand for Deposite demandReason"+ demandReason);
        return demandDetail;

    }

    private DemandDetail createLegacyDemandDeatils(final String tenantId, final String demandReasoncode, final double amount,
            final double collectedAmount, final String taxPeriodCode, final String demandDetId,
            final DemandDetailBean demandReason) {
        DemandDetail demandDetail = null;
        if (demandDetId != null) {
            demandDetail = new DemandDetail();
            demandDetail.setId(demandReason.getId());
            demandDetail.setDemandId(demandReason.getDemandId());
            demandDetail.setTaxHeadMasterCode(demandReasoncode);
            demandDetail.setTaxAmount(BigDecimal.valueOf(demandReason.getTaxAmount()));
            demandDetail.setCollectionAmount(BigDecimal.valueOf(demandReason.getCollectionAmount()));
            demandDetail.setTenantId(demandReason.getTenantId());

        }

        else {
            demandDetail = new DemandDetail();
            demandDetail.setId(demandDetId);
            demandDetail.setDemandId(demandReason.getDemandId());
            demandDetail.setTaxHeadMasterCode(demandReasoncode);
            demandDetail.setTaxAmount(BigDecimal.valueOf(amount));
            demandDetail.setCollectionAmount(BigDecimal.valueOf(collectedAmount));
            demandDetail.setTenantId(tenantId);
        }
        return demandDetail;

    }

    public TaxPeriodResponse getTaxPeriodByPeriodCycleAndService(final String tenantId, final PeriodCycle periodCycle,
            final Long date) {
        final TaxPeriodCriteria taxperiodcriteria = new TaxPeriodCriteria();
        taxperiodcriteria.setTenantId(tenantId);
        taxperiodcriteria.setService(BUSINESSSERVICE);
        taxperiodcriteria.setPeriodCycle(periodCycle);
        final StringBuilder url = new StringBuilder();
        url.append(configurationManager.getBillingDemandServiceHostNameTopic())
                .append(configurationManager.getTaxperidforfinancialYearTopic());
        url.append("?service=").append(taxperiodcriteria.getService()).append("&tenantId=")
                .append(taxperiodcriteria.getTenantId());
        url.append("&fromDate=").append(date);
        url.append("&toDate=").append(1522454400000l);
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

    public DemandResponse updateDemandCollection(final List<Demand> demands, final RequestInfo requestInfo) {
        final DemandRequest demandRequest = new DemandRequest();
        demandRequest.setRequestInfo(requestInfo);
        demandRequest.setDemands(demands);

        final String url = configurationManager.getBillingDemandServiceHostNameTopic() +
                configurationManager.getUpdateDemandServiceTopic();

        final DemandResponse demres = new RestTemplate().postForObject(url, demandRequest, DemandResponse.class);

        if (demres.getDemands().isEmpty())
            throw new WaterConnectionException("Error While Demand generation", "Error While Demand generation", requestInfo);
        return demres;
    }

    public DemandResponse updateDemand(final List<Demand> demands, final RequestInfo requestInfo) {
        final DemandRequest demandRequest = new DemandRequest();
        demandRequest.setRequestInfo(requestInfo);
        demandRequest.setDemands(demands);

        final String url = configurationManager.getBillingDemandServiceHostNameTopic() +
                configurationManager.getBillingUpdateDemand();

        final DemandResponse demres = new RestTemplate().postForObject(url, demandRequest, DemandResponse.class);

        if (demres.getDemands().isEmpty())
            throw new WaterConnectionException("Error While Demand generation", "Error While Demand generation", requestInfo);
        return demres;
    }

    public List<Demand> getDemandsByConsumerCode(final String consumerCode, final String tenantId,
            final RequestInfoWrapper requestInfo) {
        List<Demand> demandList = new ArrayList<>();
        final StringBuilder demandUrl = prepareDemandSearchUrl(consumerCode, tenantId);
        try {
            demandList = getDemands(demandUrl.toString(), requestInfo);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return demandList;
    }

    public List<Demand> getDemandsByConsumerCodeAndTaxPeriod(final String consumerCode, final String tenantId,
            final RequestInfoWrapper requestInfo) {
        List<Demand> demandList = new ArrayList<>();
        final StringBuilder demandUrl = prepareDemandSearchUrl(consumerCode, tenantId);
        // demandUrl.append("&demandFrom=").append(new BigDecimal(taxperiodFrom));
        // demandUrl.append("&demandTo=").append(new BigDecimal(taxPeriodTo));

        try {
            demandList = getDemands(demandUrl.toString(), requestInfo);
        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return demandList;
    }

    public List<Demand> getDemands(final String demandUrl, final RequestInfoWrapper requestInfo) {
        DemandResponse demandResponse = null;
        final List<Demand> demandList = new ArrayList<>();
        try {
            demandResponse = new RestTemplate().postForObject(demandUrl.toString(), requestInfo, DemandResponse.class);

            System.out.println("Get demand response is :" + demandResponse);
            if (demandResponse != null && demandResponse.getDemands() != null)
                demandList.addAll(demandResponse.getDemands());
        } catch (final HttpClientErrorException exception) {
            /*
             * throw new ValidationUrlNotFoundException(propertiesManager.getInvalidDemandValidation(), exception.getMessage(),
             * requestInfo.getRequestInfo());
             */
        }
        return demandList;
    }

    protected StringBuilder prepareDemandSearchUrl(final String consumerCode, final String tenantId) {
        final StringBuilder demandUrl = new StringBuilder();
        demandUrl.append(configurationManager.getBillingDemandServiceHostNameTopic());
        demandUrl.append(configurationManager.getSearchbillingDemandServiceTopic());
        demandUrl.append("?tenantId=").append(tenantId);
        demandUrl.append("&consumerCode=").append(consumerCode);
        demandUrl.append("&businessService=").append(BUSINESSSERVICE);
        return demandUrl;
    }

    public Demand getDemandById(final String id, final String tenantId, final RequestInfo requestInfo) {
        DemandResponse demandResponse = null;
        Demand demObj = null;
        final StringBuilder demandUrl = new StringBuilder();
        demandUrl.append(configurationManager.getBillingDemandServiceHostNameTopic());
        demandUrl.append(configurationManager.getSearchbillingDemandServiceTopic());
        demandUrl.append("?tenantId=").append(tenantId);
        demandUrl.append("&demandId=").append(id);
        demandResponse = new RestTemplate().postForObject(demandUrl.toString(), requestInfo, DemandResponse.class);

        System.out.println("Get demand response is :" + demandResponse);
        if (demandResponse != null && demandResponse.getDemands() != null)
            demObj = demandResponse.getDemands().get(0);

        return demObj;
    }

}
