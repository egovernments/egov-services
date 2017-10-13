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
package org.egov.wcms.transaction.validator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandRequest;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.exception.FinYearException;
import org.egov.wcms.transaction.exception.IdGenerationException;
import org.egov.wcms.transaction.exception.WaterConnectionException;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.utils.WcmsConnectionConstants;
import org.egov.wcms.transaction.web.contract.AckIdRequest;
import org.egov.wcms.transaction.web.contract.AckNoGenerationRequest;
import org.egov.wcms.transaction.web.contract.AckNoGenerationResponse;
import org.egov.wcms.transaction.web.contract.BoundaryRequestInfo;
import org.egov.wcms.transaction.web.contract.BoundaryRequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.BoundaryResponse;
import org.egov.wcms.transaction.web.contract.DemandDueResponse;
import org.egov.wcms.transaction.web.contract.DonationResponseInfo;
import org.egov.wcms.transaction.web.contract.FinYearReq;
import org.egov.wcms.transaction.web.contract.FinYearRes;
import org.egov.wcms.transaction.web.contract.IdGenErrorRes;
import org.egov.wcms.transaction.web.contract.NonMeterWaterRates;
import org.egov.wcms.transaction.web.contract.NonMeterWaterRatesResponse;
import org.egov.wcms.transaction.web.contract.PipeSizeResponseInfo;
import org.egov.wcms.transaction.web.contract.PropertyInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.RequestInfoBody;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.StorageReservoirResponse;
import org.egov.wcms.transaction.web.contract.SupplyResponseInfo;
import org.egov.wcms.transaction.web.contract.Tenant;
import org.egov.wcms.transaction.web.contract.TenantResponse;
import org.egov.wcms.transaction.web.contract.TreatmentPlantResponse;
import org.egov.wcms.transaction.web.contract.UsageTypeResponse;
import org.egov.wcms.transaction.web.contract.WaterChargesConfigRes;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.web.contract.WaterSourceResponseInfo;
import org.egov.wcms.transaction.web.errorhandler.Error;
import org.egov.wcms.transaction.web.errorhandler.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestConnectionService {

    @Autowired
    private ConfigurationManager configurationManager;

    public boolean getTreateMentPlantName(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        Boolean isValidTreatmentPlant = Boolean.FALSE;
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterTreatmentSearchTopic())
                .append("?name=").append(waterConnectionRequest.getConnection().getWaterTreatment())
                .append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final TreatmentPlantResponse treatmentPlantRes = new RestTemplate().postForObject(url.toString(), wrapper,
                TreatmentPlantResponse.class);
        if (treatmentPlantRes != null && treatmentPlantRes.getTreatmentPlants() != null
                && !treatmentPlantRes.getTreatmentPlants().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setWaterTreatmentId(treatmentPlantRes.getTreatmentPlants() != null
                            && treatmentPlantRes.getTreatmentPlants().get(0) != null
                                    ? String.valueOf(treatmentPlantRes.getTreatmentPlants().get(0).getId()) : "");
            isValidTreatmentPlant = Boolean.TRUE;
        }
        return isValidTreatmentPlant;
    }

    public boolean getStorageReservoirName(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        Boolean isValidStorageReservoir = Boolean.FALSE;
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getReservoirSearchTopic())
                .append("?name=").append(waterConnectionRequest.getConnection().getStorageReservoir())
                .append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final StorageReservoirResponse storageResponse = new RestTemplate().postForObject(url.toString(), wrapper,
                StorageReservoirResponse.class);
        if (storageResponse != null && storageResponse.getStorageReservoirs() != null
                && !storageResponse.getStorageReservoirs().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setStorageReservoirId(storageResponse.getStorageReservoirs() != null
                            && storageResponse.getStorageReservoirs().get(0) != null
                                    ? String.valueOf(storageResponse.getStorageReservoirs().get(0).getId()) : "");
            isValidStorageReservoir = Boolean.TRUE;
        }
        return isValidStorageReservoir;
    }

    public boolean getUsageTypeName(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        Boolean isValidUsage = Boolean.FALSE;
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getUsageTypeSearchPathTopic())
                .append("?code=").append(waterConnectionRequest.getConnection().getUsageType())
                .append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final UsageTypeResponse usageRes = new RestTemplate().postForObject(url.toString(), wrapper, UsageTypeResponse.class);

        if (usageRes != null && usageRes.getUsageTypes() != null && !usageRes.getUsageTypes().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setUsageTypeId(usageRes.getUsageTypes() != null && usageRes.getUsageTypes().get(0) != null
                            ? String.valueOf(usageRes.getUsageTypes().get(0).getId()) : "");
            isValidUsage = Boolean.TRUE;
        }
        return isValidUsage;
    }

    public boolean getSubUsageTypeName(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        Boolean isValidSubUsage = Boolean.FALSE;
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getUsageTypeSearchPathTopic())
                .append("?code=").append(waterConnectionRequest.getConnection().getSubUsageType())
                .append("&parent=").append(waterConnectionRequest.getConnection().getUsageType())
                .append("&isSubUsageType=").append(Boolean.TRUE)
                .append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final UsageTypeResponse subUsageResponse = new RestTemplate().postForObject(url.toString(), wrapper,
                UsageTypeResponse.class);
        if (subUsageResponse != null && subUsageResponse.getUsageTypes() != null && !subUsageResponse.getUsageTypes().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setSubUsageTypeId(subUsageResponse.getUsageTypes() != null && subUsageResponse.getUsageTypes().get(0) != null
                            ? String.valueOf(subUsageResponse.getUsageTypes().get(0).getId()) : "");
            isValidSubUsage = Boolean.TRUE;
        }
        return isValidSubUsage;
    }

    public boolean getPipesizeTypeByCode(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        Boolean isValidPipeSize = Boolean.FALSE;
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServicePipesizeSearchPathTopic())
                .append("?sizeInMilimeter=")
                .append(Double.parseDouble(waterConnectionRequest.getConnection().getHscPipeSizeType()))
                .append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final PipeSizeResponseInfo pipesize = new RestTemplate().postForObject(url.toString(), wrapper,
                PipeSizeResponseInfo.class);
        if (pipesize != null && pipesize.getPipeSize() != null && !pipesize.getPipeSize().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setPipesizeId(pipesize.getPipeSize() != null && pipesize.getPipeSize().get(0) != null
                            ? String.valueOf(pipesize.getPipeSize().get(0).getId()) : "");
            isValidPipeSize = Boolean.TRUE;
        }
        return isValidPipeSize;
    }

    public boolean getSourceTypeByName(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        Boolean isValidSourceType = Boolean.FALSE;
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServiceSourceSearchPathTopic())
                .append("?name=").append(waterConnectionRequest.getConnection().getSourceType())
                .append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final WaterSourceResponseInfo sourcetype = new RestTemplate().postForObject(url.toString(), wrapper,
                WaterSourceResponseInfo.class);
        if (sourcetype != null && sourcetype.getWaterSourceType() != null && !sourcetype.getWaterSourceType().isEmpty()) {
            waterConnectionRequest.getConnection().setSourceTypeId(sourcetype.getWaterSourceType() != null
                    && sourcetype.getWaterSourceType().get(0) != null
                            ? String.valueOf(sourcetype.getWaterSourceType().get(0).getId()) : "");
            isValidSourceType = Boolean.TRUE;

        }
        return isValidSourceType;
    }

    public boolean getSupplyTypeByName(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        Boolean isValidSupplyType = Boolean.FALSE;
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServiceSupplySearchPathTopic())
                .append("?name=").append(waterConnectionRequest.getConnection().getSupplyType())
                .append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        log.info("URL to validate Supply Type : " + url.toString());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final SupplyResponseInfo supplytype = new RestTemplate().postForObject(url.toString(), wrapper, SupplyResponseInfo.class);
        if (supplytype != null && !supplytype.getSupplytypes().isEmpty()) {
            waterConnectionRequest.getConnection().setSupplyTypeId(supplytype.getSupplytypes() != null
                    && supplytype.getSupplytypes().get(0) != null ? String.valueOf(supplytype.getSupplytypes().get(0).getId())
                            : "");
            isValidSupplyType = Boolean.TRUE;
        }
        return isValidSupplyType;
    }

    public PropertyResponse getPropertyDetailsByUpicNo(final WaterConnectionReq waterRequestReq) {
        final RequestInfo requestInfo = waterRequestReq.getRequestInfo();
        final StringBuilder url = new StringBuilder();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getPropertyServiceSearchPathTopic()); 
        if(StringUtils.isNotBlank(waterRequestReq.getConnection().getOldPropertyIdentifier()))  { 
        	url.append("?oldUpicNo=")
            .append(waterRequestReq.getConnection().getOldPropertyIdentifier())
            .append("&tenantId=").append(waterRequestReq.getConnection().getTenantId());
        } else { 
        	url.append("?upicNumber=")
            .append(waterRequestReq.getConnection().getPropertyIdentifier())
            .append("&tenantId=").append(waterRequestReq.getConnection().getTenantId());
        }
        log.info("URL to invoke : " + url.toString());
        PropertyResponse propResp = null;
        try {
            propResp = new RestTemplate().postForObject(url.toString(), wrapper,
                    PropertyResponse.class);
            System.out.println(propResp != null ? propResp.toString() + "" + propResp.getProperties().size()
                    : "Issue while binding PT to watertax");
        } catch (final Exception e) {

            System.out.println(propResp != null ? propResp.toString() : "issue with propResp in exception block in WT");

            throw new WaterConnectionException("Error while Fetching Data from PropertyTax",
                    "Error while Fetching Data from PropertyTax", requestInfo);
        }

        waterRequestReq.getConnection()
                .setPropertyIdentifier(waterRequestReq.getConnection().getProperty().getPropertyIdentifier());
      /*  if (propResp.getProperties() != null && !propResp.getProperties().isEmpty()
                && !propResp.getProperties().get(0).getOwners().isEmpty()) {
            waterRequestReq.getConnection().getProperty().getPropertyOwner().get(0)
                    .setNameOfApplicant(propResp.getProperties().get(0).getOwners().get(0).getName());
            waterRequestReq.getConnection().getProperty()
                    .setEmail(propResp.getProperties().get(0).getOwners().get(0).getEmailId());
            waterRequestReq.getConnection().getProperty()
                    .setMobileNumber(propResp.getProperties().get(0).getOwners().get(0).getMobileNumber());
            waterRequestReq.getConnection().getProperty().setZone(propResp.getProperties().get(0).getBoundary() != null
                    ? propResp.getProperties().get(0).getBoundary().getRevenueBoundary().getId() : null);
        }*/

        return propResp;
    }
    
    public PropertyResponse getPropertyDetailsByUpicNoForSearch(final WaterConnectionReq waterRequestReq) {
        final RequestInfo requestInfo = waterRequestReq.getRequestInfo();
        final StringBuilder url = new StringBuilder();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getPropertyServiceSearchPathTopic()); 
        if(StringUtils.isNotBlank(waterRequestReq.getConnection().getOldPropertyIdentifier()))  { 
                url.append("?oldUpicNo=")
            .append(waterRequestReq.getConnection().getOldPropertyIdentifier())
            .append("&tenantId=").append(waterRequestReq.getConnection().getTenantId());
        } else { 
                url.append("?upicNumber=")
            .append(waterRequestReq.getConnection().getPropertyIdentifier())
            .append("&tenantId=").append(waterRequestReq.getConnection().getTenantId());
        }
        log.info("URL to invoke : " + url.toString());
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
   
    
	public List<NonMeterWaterRates> getNonMeterWaterRates(WaterConnectionReq waterConnectionReq) {
		final RequestInfo requestInfo = waterConnectionReq.getRequestInfo();
		final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		Connection conn = waterConnectionReq.getConnection();
		String url = prepareUrlForNonMeterWaterRates(conn);
		if (null != url) {
			try {
				NonMeterWaterRatesResponse meterRates = new RestTemplate().postForObject(url, wrapper,
						NonMeterWaterRatesResponse.class);
				if (null != meterRates && null != meterRates.getNonMeterWaterRates()) {
					return meterRates.getNonMeterWaterRates();
				}
			} catch (final Exception e) {
				log.error("Encountered an Exception :" + e);
				return null;
			}
		}
		return null;
	}
    
	private String prepareUrlForNonMeterWaterRates(Connection conn) {
		if (StringUtils.isNotBlank(conn.getSourceTypeId()) && StringUtils.isNotBlank(conn.getConnectionType())
				&& StringUtils.isNotBlank(conn.getUsageTypeId()) && StringUtils.isNotBlank(conn.getSubUsageTypeId())
				&& StringUtils.isNotBlank(conn.getPipesizeId()) && conn.getNumberOfTaps() > 0) {
			StringBuilder url = new StringBuilder(configurationManager.getWaterMasterServiceBasePathTopic()
					+ configurationManager.getNonMeterWaterRatesSearchPath());
			url.append("?tenantId=" + conn.getTenantId());
			url.append("&sourceTypeName=" + conn.getSourceType());
			url.append("&connectionType=" + conn.getConnectionType());
			url.append("&usageTypeCode=" + conn.getUsageType());
			url.append("&subUsageTypeCode=" + conn.getSubUsageType());
			url.append("&pipeSize=" + conn.getHscPipeSizeType());
			url.append("&noOfTaps=" + conn.getNumberOfTaps());
			return url.toString();
		}
		return null; 

	}

    public List<PropertyInfo> getPropertyDetailsByParams(final RequestInfoWrapper wrapper, final String urlToInvoke) {
        log.info("URL to invoke for PropertyDetails : " + urlToInvoke);
        final PropertyResponse propResp = invokePropertyAPI(urlToInvoke, wrapper);
        if (null != propResp && null != propResp.getProperties() && propResp.getProperties().size() > 0) {
            log.info("Response obtained from Property Module : " + propResp);
            return propResp.getProperties();
        }
        return null;
    }

    private PropertyResponse invokePropertyAPI(final String url, final RequestInfoWrapper wrapper) {
        try {
            return new RestTemplate().postForObject(url, wrapper, PropertyResponse.class);
        } catch (final Exception e) {
            log.error("Encountered an Exception :" + e);
            return null;
        }
    }

    public DonationResponseInfo validateDonationAmount(final WaterConnectionReq waterConnectionRequest) {
        final StringBuilder url = new StringBuilder();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(waterConnectionRequest.getRequestInfo())
                .build();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServiceDonationSearchPathTopic())
                .append("?usageTypeCode=").append(waterConnectionRequest.getConnection().getUsageType())
                .append("&subUsageTypeCode=").append(waterConnectionRequest.getConnection().getSubUsageType())
                .append(
                        "&maxPipeSizeId=")
                .append(
                        waterConnectionRequest.getConnection().getPipesizeId())
                .append(
                        "&minPipeSizeId=")
                .append(waterConnectionRequest.getConnection().getPipesizeId()).append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        log.info("URL For Donation Validation : " + url.toString());
        final DonationResponseInfo donation = new RestTemplate().postForObject(url.toString(), wrapper,
                DonationResponseInfo.class);
        if (donation != null && donation.getDonations() != null && !donation.getDonations().isEmpty())
            waterConnectionRequest.getConnection().setDonationCharge(donation.getDonations() != null
                    && donation.getDonations().get(0) != null ? new Double(donation.getDonations().get(0).getDonationAmount())
                            : 0d);
        return donation;
    }

    public String getFinancialYear(final String tenantId) {
        final StringBuilder url = new StringBuilder();
        final String year = getFiscalYear();
        String finYear = null;
        url.append(configurationManager.getFinanceServiceHostName())
                .append(configurationManager.getFinanceServiceSearchPath());
        url.append("?tenantId=" + tenantId);
        url.append("&finYearRange=" + year);
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
        final FinYearReq finYearReq = new FinYearReq();
        finYearReq.setRequestInfo(requestInfo);
        String response = null;
        try {
            response = new RestTemplate().postForObject(url.toString(), finYearReq, String.class);
        } catch (final Exception ex) {
            throw new FinYearException("Error While obtaining Financial Year", "Error While obtaining Financial Year",
                    requestInfo);
        }
        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        final IdGenErrorRes errorResponse = gson.fromJson(response, IdGenErrorRes.class);
        final FinYearRes finYearResponse = gson.fromJson(response, FinYearRes.class);
        if (!errorResponse.getErrors().isEmpty())
            throw new IdGenerationException("Error While generating ACK number", "Error While generating ACK number",
                    requestInfo);
        else if (finYearResponse.getResponseInfo() != null)
            if (finYearResponse.getResponseInfo().getStatus().toString()
                    .equalsIgnoreCase("SUCCESSFUL"))
                if (finYearResponse.getFinancialYear() != null && !finYearResponse.getFinancialYear().isEmpty())
                    finYear = finYearResponse.getFinancialYear();

        return finYear;
    }

    public void generateEstimationNumber(final WaterConnectionReq waterConnectionRequest) {
        waterConnectionRequest.getConnection().setEstimationNumber(
                generateRequestedDocumentNumber("default",
                        configurationManager.getEstimateGenNameServiceTopic(),
                        configurationManager.getEstimateGenFormatServiceTopic(),
                        waterConnectionRequest.getRequestInfo()));
    }

    public void prepareWorkOrderNumberFormat(final WaterConnectionReq waterConnectionRequest) {
        waterConnectionRequest.getConnection().setWorkOrderNumber(
                generateRequestedDocumentNumber("default",
                        configurationManager.getWorkOrderGenNameServiceTopic(),
                        configurationManager.getWorkOrderGenFormatServiceTopic(),
                        waterConnectionRequest.getRequestInfo()));
    }

    public String generateRequestedDocumentNumber(final String tenantId, final String nameServiceTopic,
            final String formatServiceTopic, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        String ackNumber = null;
        url.append(configurationManager.getIdGenServiceBasePathTopic())
                .append(configurationManager.getIdGenServiceCreatePathTopic());
        // final RequestInfo RequestInfo = RequestInfo.builder().ts(11111111l).build();
        final List<AckIdRequest> idRequests = new ArrayList<>();
        final AckIdRequest idrequest = new AckIdRequest();

        idrequest.setIdName(nameServiceTopic);
        idrequest.setTenantId(tenantId);
        idrequest.setFormat(formatServiceTopic);
        final AckNoGenerationRequest idGeneration = new AckNoGenerationRequest();
        idRequests.add(idrequest);
        idGeneration.setIdRequests(idRequests);
        idGeneration.setRequestInfo(requestInfo);
        String response = null;
        try {
            response = new RestTemplate().postForObject(url.toString(), idGeneration, String.class);
        } catch (final Exception ex) {
            throw new IdGenerationException("Error While generating " + nameServiceTopic + " number",
                    "Error While generating " + nameServiceTopic + " number", requestInfo);
        }
        final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        final IdGenErrorRes errorResponse = gson.fromJson(response, IdGenErrorRes.class);
        final AckNoGenerationResponse idResponse = gson.fromJson(response, AckNoGenerationResponse.class);
        if (!errorResponse.getErrors().isEmpty())
            throw new IdGenerationException("Error While generating " + nameServiceTopic + " number",
                    "Error While generating " + nameServiceTopic + " number", requestInfo);
        else if (idResponse.getResponseInfo() != null)
            if (idResponse.getResponseInfo().getStatus().toString()
                    .equalsIgnoreCase("SUCCESSFUL"))
                if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
                    ackNumber = idResponse.getIdResponses().get(0).getId();

        if (nameServiceTopic.equals(configurationManager.getHscGenNameServiceTopic())) {
            // Enable the below method call to get financial year from the Finance Service
            // String finYear = getFinancialYear(tenantId);
            /*
             * String finYear = getFiscalYear(); if(null!=finYear && !finYear.isEmpty()) { return
             * ackNumber=tenantId.substring(0,4).concat(ackNumber); }
             */
            final String ulbName = getULBNameFromTenant(tenantId, requestInfo);
            if (!ulbName.equals(""))
                return ackNumber = ulbName.substring(0, 4).concat(ackNumber);
            else
                return ackNumber = tenantId.substring(0, 4).concat(ackNumber);
        }

        return ackNumber;
    }

    public String getULBNameFromTenant(final String tenantId, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder(
                configurationManager.getTenantServiceBasePath() + configurationManager.getTenantServiceSearchPath());
        url.append("?code=" + tenantId);
        final RequestInfoBody requestInfoBody = new RequestInfoBody(requestInfo);
        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
        log.info("URL to invoke Tenant Service : " + url.toString());
        log.info("Request Info to invoke the URL : " + request);
        String ulbCode = "";
        final TenantResponse tr = new RestTemplate().postForObject(url.toString(), request, TenantResponse.class);
        if (null != tr) {
            log.info("Tenant Response : " + tr);
            if (null != tr.getTenant())
                for (final Tenant tenant : tr.getTenant())
                    if (null != tenant.getCity())
                        ulbCode = tenant.getCity().getCode();
        }
        return ulbCode;
    }

    public Demand getDemandEstimation(final Connection connection) {
        final StringBuilder url = new StringBuilder();
        url.append(configurationManager.getBillingDemandServiceHostNameTopic())
                .append(configurationManager.getSearchbillingDemandServiceTopic());
        url.append("?tenantId=" + connection.getTenantId());
        url.append("&businessService=WC");
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
        final DemandRequest demandRequest = new DemandRequest();
        demandRequest.setRequestInfo(requestInfo);
        List<Demand> demandList = new ArrayList<>();
        DemandResponse demandResponse = new DemandResponse();
        try {
             demandResponse =new RestTemplate().postForObject(url.toString(), demandRequest, DemandResponse.class);
        } catch (final Exception ex) {
            // throw new DemandException("Error While obtaining Demand Estimation Value", "Error While obtaining Demand Estimation
            // Value",requestInfo);
            log.info(ex.getMessage());
        }
       
        if (null != demandResponse) {
            log.info("Demand Response : " + demandResponse);
            if (null != demandResponse.getResponseInfo())
                if (demandResponse.getResponseInfo().getStatus().toString().equals("200"))
                    if (demandResponse.getDemands() != null && !demandResponse.getDemands().isEmpty())
                        demandList = demandResponse.getDemands();
        }
        if (demandList.size() > 0)
            return demandList.get(0);
        return null;
    }

    public BoundaryResponse getBoundaryCode( final String code, final String tenantId) {
        String url = configurationManager.getLocationServiceBasePathTopic()
                + configurationManager.getLocationServiceBoundarySearchPathTopic();
        url = url.replace("{codes}", code);
        url = url.replace("{tenantId}", tenantId);
        final BoundaryResponse boundary = getBoundary(url);
        return boundary;
    }

    public BoundaryResponse getBoundary(final String url) {
        final BoundaryRequestInfo requestInfo = BoundaryRequestInfo.builder().build();
        final BoundaryRequestInfoWrapper wrapper = BoundaryRequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<BoundaryRequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final BoundaryResponse boundary = new RestTemplate().postForObject(url, request,
                BoundaryResponse.class);
        return boundary;
    }

    public BoundaryResponse getBoundaryName(final String boundaryType, final String[] boundaryNum, final String tenantId) {
        final String url = configurationManager.getLocationServiceBasePathTopic()
                + configurationManager.getLocationServiceBoundarySearchPathTopic();
        final BoundaryRequestInfo requestInfo = BoundaryRequestInfo.builder().build();
        final BoundaryRequestInfoWrapper wrapper = BoundaryRequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<BoundaryRequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final BoundaryResponse boundary = new RestTemplate().postForObject(url.toString(), request,
                BoundaryResponse.class, boundaryType, boundaryNum, tenantId);
        return boundary;

    }

    public List<ErrorResponse> populateErrors() {
        final ErrorResponse errRes = new ErrorResponse();
        final Error error = new Error();
        error.setCode(1);
        error.setDescription("Error while generating ack number");
        errRes.setError(error);
        final List<ErrorResponse> errorResponses = new ArrayList<>();
        errorResponses.add(errRes);
        return errorResponses;
    }

    private String getFiscalYear() {
        Calendar calendarDate;
        final int FIRST_FISCAL_MONTH = Calendar.MARCH;
        calendarDate = Calendar.getInstance();
        final int month = calendarDate.get(Calendar.MONTH);
        final int year = calendarDate.get(Calendar.YEAR);
        final int value = month >= FIRST_FISCAL_MONTH ? year : year - 1;
        final String finYear = Integer.toString(value) + "-" + Integer.toString(value + 1).substring(2, 4);
        return finYear;
    }

    public WaterChargesConfigRes getWaterChargesConfig(final String name, final String tenantId) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic());
        url.append(configurationManager.getWaterMasterServiceWaterChargesConfigSearchPathTopic());
        url.append("?name=").append(WcmsConnectionConstants.AADHARNUMBER_REQUIRED);
        url.append("&tenantId=").append(tenantId);
        final WaterChargesConfigRes waterChargesConfig = getWaterConfigValues(url.toString());
        return waterChargesConfig;
    }

    public WaterChargesConfigRes getWaterConfigValues(final String url) {
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(getRequestInfoWrapperWithoutAuth());
        final WaterChargesConfigRes waterConfig = new RestTemplate().postForObject(url, request,
                WaterChargesConfigRes.class);
        return waterConfig;
    }

    public RequestInfoWrapper getRequestInfoWrapperWithoutAuth() {
        return RequestInfoWrapper.builder().requestInfo(RequestInfo.builder().ts(111111111L).build()).build();
    }

    public String getUserServiceSearchPath() {
        final StringBuffer searchUrl = new StringBuffer();
        searchUrl.append(configurationManager.getUserHostName());
        searchUrl.append(configurationManager.getUserBasePath());
        searchUrl.append(configurationManager.getUserSearchPath());
        return searchUrl.toString();
    }

    public String getUserServiceCreatePath() {
        final StringBuffer createUrl = new StringBuffer();
        createUrl.append(configurationManager.getUserHostName());
        createUrl.append(configurationManager.getUserBasePath());
        createUrl.append(configurationManager.getUserCreatePath());
        return createUrl.toString();
    }

    public Boolean getWaterChargeConfigValuesForAadhar(final String tenantId) {
        Boolean isWaterConfigValues = Boolean.FALSE;

        WaterChargesConfigRes waterChargesConfigRes = null;
        waterChargesConfigRes = getWaterChargesConfig(
                WcmsConnectionConstants.AADHARNUMBER_REQUIRED,
                tenantId);
        if (waterChargesConfigRes != null && !waterChargesConfigRes.getWaterConfigurationValue().isEmpty()
                && waterChargesConfigRes.getWaterConfigurationValue().get(0).getValue().equals("YES"))
            isWaterConfigValues = Boolean.TRUE;

        return isWaterConfigValues;
    }
    public String generateAcknowledgementNumber(final WaterConnectionReq waterConnectionRequest) {
        return generateRequestedDocumentNumber(
                waterConnectionRequest.getConnection().getTenantId(), configurationManager.getIdGenNameServiceTopic(),
                configurationManager.getIdGenFormatServiceTopic(), waterConnectionRequest.getRequestInfo());
    }
    
    public DemandDueResponse getPropertyTaxDueResponse(final String propertyIdentifier, final String tenantId) {
        final StringBuilder urlToInvoke = new StringBuilder();
        urlToInvoke.append(configurationManager.getBillingDemandServiceHostNameTopic())
                .append(configurationManager.getBillingServiceSearchDuesTopic()).append("?tenantId=").append(tenantId)
                .append("&businessService=").append(configurationManager.getBusinessService())
                .append("&consumerCode=").append(propertyIdentifier);
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        final RequestInfoWrapper requestInfoWrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        try {
            return new RestTemplate().postForObject(urlToInvoke.toString(), requestInfoWrapper, DemandDueResponse.class);
        } catch (final Exception e) {
            log.error("Exception encountered:" + e);
        }
        return null;

    }

   
    
}
