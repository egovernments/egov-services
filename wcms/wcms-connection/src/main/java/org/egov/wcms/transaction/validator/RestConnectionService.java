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

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.demand.contract.Demand;
import org.egov.wcms.transaction.demand.contract.DemandRequest;
import org.egov.wcms.transaction.demand.contract.DemandResponse;
import org.egov.wcms.transaction.exception.FinYearException;
import org.egov.wcms.transaction.exception.IdGenerationException;
import org.egov.wcms.transaction.exception.WaterConnectionException;
import org.egov.wcms.transaction.model.Connection;
import org.egov.wcms.transaction.web.contract.AckIdRequest;
import org.egov.wcms.transaction.web.contract.AckNoGenerationRequest;
import org.egov.wcms.transaction.web.contract.AckNoGenerationResponse;
import org.egov.wcms.transaction.web.contract.BoundaryRequestInfo;
import org.egov.wcms.transaction.web.contract.BoundaryRequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.BoundaryResponse;
import org.egov.wcms.transaction.web.contract.CategoryResponseInfo;
import org.egov.wcms.transaction.web.contract.DonationResponseInfo;
import org.egov.wcms.transaction.web.contract.FinYearReq;
import org.egov.wcms.transaction.web.contract.FinYearRes;
import org.egov.wcms.transaction.web.contract.IdGenErrorRes;
import org.egov.wcms.transaction.web.contract.PipeSizeResponseInfo;
import org.egov.wcms.transaction.web.contract.PropertyCategoryResponseInfo;
import org.egov.wcms.transaction.web.contract.PropertyInfo;
import org.egov.wcms.transaction.web.contract.PropertyResponse;
import org.egov.wcms.transaction.web.contract.PropertyUsageTypeResponseInfo;
import org.egov.wcms.transaction.web.contract.RequestInfoBody;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.SupplyResponseInfo;
import org.egov.wcms.transaction.web.contract.Tenant;
import org.egov.wcms.transaction.web.contract.TenantResponse;
import org.egov.wcms.transaction.web.contract.TreatmentPlantResponse;
import org.egov.wcms.transaction.web.contract.UsageMasterResponse;
import org.egov.wcms.transaction.web.contract.WaterConnectionReq;
import org.egov.wcms.transaction.web.contract.WaterSourceResponseInfo;
import org.egov.wcms.transaction.web.errorhandler.Error;
import org.egov.wcms.transaction.web.errorhandler.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class RestConnectionService {

    @Autowired
    private ConfigurationManager configurationManager;
    
    private static final Logger logger = LoggerFactory.getLogger(RestConnectionService.class);

    public CategoryResponseInfo getCategoryTypeByName(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServiceCategorySearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        CategoryResponseInfo positions = new RestTemplate().postForObject(url.toString(), request, CategoryResponseInfo.class,
                waterConnectionRequest.getConnection().getCategoryType(), waterConnectionRequest.getConnection().getTenantId());
        if (positions != null && !positions.getCategory().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setCategoryId(positions.getCategory() != null && positions.getCategory().get(0) != null
                            ? String.valueOf(positions.getCategory().get(0).getId()) : "");
        }
        return positions;
    }

    public TreatmentPlantResponse getTreateMentPlantName(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterTreatmentSearchTopic());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        TreatmentPlantResponse positions = new RestTemplate().postForObject(url.toString(), request, TreatmentPlantResponse.class,
                waterConnectionRequest.getConnection().getWaterTreatment(), waterConnectionRequest.getConnection().getTenantId());
        if (positions != null && positions.getTreatmentPlants() != null && !positions.getTreatmentPlants().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setWaterTreatmentId(positions.getTreatmentPlants() != null && positions.getTreatmentPlants().get(0) != null
                            ? String.valueOf(positions.getTreatmentPlants().get(0).getId()) : "");
        }
        return positions;
    }

    public PipeSizeResponseInfo getPipesizeTypeByCode(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServicePipesizeSearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        PipeSizeResponseInfo pipesize = new RestTemplate().postForObject(url.toString(), request, PipeSizeResponseInfo.class,
                Double.parseDouble(waterConnectionRequest.getConnection().getHscPipeSizeType()),
                waterConnectionRequest.getConnection().getTenantId());
        if (pipesize != null && pipesize.getPipeSize() != null && !pipesize.getPipeSize().isEmpty()) {
            waterConnectionRequest.getConnection()
                    .setPipesizeId(pipesize.getPipeSize() != null && pipesize.getPipeSize().get(0) != null
                            ? String.valueOf(pipesize.getPipeSize().get(0).getId()) : "");
        }
        return pipesize;
    }

    public WaterSourceResponseInfo getSourceTypeByName(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServiceSourceSearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        WaterSourceResponseInfo sourcetype = new RestTemplate().postForObject(url.toString(), request,
                WaterSourceResponseInfo.class,
                waterConnectionRequest.getConnection().getSourceType(), waterConnectionRequest.getConnection().getTenantId());
        if (sourcetype != null && sourcetype.getWaterSourceType() != null && !sourcetype.getWaterSourceType().isEmpty()) {
            waterConnectionRequest.getConnection().setSourceTypeId(sourcetype.getWaterSourceType() != null
                    && sourcetype.getWaterSourceType().get(0) != null
                            ? String.valueOf(sourcetype.getWaterSourceType().get(0).getId()) : "");
        }
        return sourcetype;
    }

    public SupplyResponseInfo getSupplyTypeByName(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServiceSupplySearchPathTopic());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        SupplyResponseInfo supplytype = new RestTemplate().postForObject(url.toString(), request, SupplyResponseInfo.class,
                waterConnectionRequest.getConnection().getSupplyType(), waterConnectionRequest.getConnection().getTenantId());
        if (supplytype != null && !supplytype.getSupplytypes().isEmpty()) {
            waterConnectionRequest.getConnection().setSupplyTypeId(supplytype.getSupplytypes() != null
                    && supplytype.getSupplytypes().get(0) != null ? String.valueOf(supplytype.getSupplytypes().get(0).getId())
                            : "");
        }
        return supplytype;
    }

    public Boolean validatePropertyCategoryMapping(WaterConnectionReq waterConnectionRequest) {
        Boolean isValidPropAndCategory = Boolean.FALSE;
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterPropCategoryMappingTopic())
                .append("?propertyType=").append(waterConnectionRequest.getConnection().getProperty().getPropertyType())
                .append("&categoryType=").append(waterConnectionRequest.getConnection().getCategoryType())
                .append("&tenantId=").append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(1111111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        PropertyCategoryResponseInfo propCategory = new RestTemplate().postForObject(url.toString(),
                wrapper, PropertyCategoryResponseInfo.class);
        if (propCategory != null && propCategory.getPropCategory() != null && !propCategory.getPropCategory().isEmpty()
                && propCategory.getPropCategory().get(0).getId() != null) {
            waterConnectionRequest.getConnection().getProperty()
                    .setPropertyTypeId(propCategory.getPropCategory().get(0).getPropertyTypeId());
            isValidPropAndCategory = Boolean.TRUE;
        }
        return isValidPropAndCategory;
    }

    /*
     * public Boolean validatePropertyPipesizeMapping(WaterConnectionReq waterConnectionRequest) { Boolean isValidPropAndCategory
     * = Boolean.FALSE; StringBuilder url = new StringBuilder();
     * url.append(configurationManager.getWaterMasterServiceBasePathTopic())
     * .append(configurationManager.getWaterMasterPropPipeSizeMappingTopic())
     * .append("?propertyType=").append(waterConnectionRequest.getConnection().getProperty().getPropertyType())
     * .append("&pipeSizeType=").append(waterConnectionRequest.getConnection().getHscPipeSizeType())
     * .append("&tenantId=").append(waterConnectionRequest.getConnection().getTenantId()); final RequestInfo requestInfo =
     * RequestInfo.builder().ts(111111L).build(); RequestInfoWrapper wrapper =
     * RequestInfoWrapper.builder().requestInfo(requestInfo).build(); PropertyCategoryResponseInfo propCategory = new
     * RestTemplate().postForObject(url.toString(), wrapper, PropertyCategoryResponseInfo.class); if (propCategory != null &&
     * !propCategory.getPropCategory().isEmpty() && propCategory.getPropCategory().get(0).getId() != null) {
     * waterConnectionRequest.getConnection().getProperty().setPropertyTypeId(Long.valueOf(propCategory.getPropCategory().get(0).
     * getPropertyTypeId())); isValidPropAndCategory = Boolean.TRUE; } return isValidPropAndCategory; }
     */

    public Boolean validateSubUsageType(WaterConnectionReq waterConnectionRequest) {
        Boolean isValidSubUsageType = Boolean.FALSE;
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getSerachSubUsageType())
                .append("?code=").append(waterConnectionRequest.getConnection().getSubUsageType())
                .append("&tenantId=").append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(1111111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        UsageMasterResponse usagesubtype = new RestTemplate().postForObject(url.toString(),
                wrapper, UsageMasterResponse.class);
        if (usagesubtype != null && usagesubtype.getUsageMasters() != null && !usagesubtype.getUsageMasters().isEmpty()
                && usagesubtype.getUsageMasters().get(0).getId() != null) {
            waterConnectionRequest.getConnection()
                    .setSubUsageTypeId(usagesubtype.getUsageMasters().get(0).getId());
            isValidSubUsageType = Boolean.TRUE;
        }
        return isValidSubUsageType;
    }
    
    
    public Boolean validatePropertyUsageTypeMapping(WaterConnectionReq waterConnectionRequest) {
        Boolean isValidPropAndCategory = Boolean.FALSE;
        StringBuilder url = new StringBuilder();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterPropUsageTypeMappingTopic())
                .append("?propertyType=").append(waterConnectionRequest.getConnection().getProperty().getPropertyType())
                .append("&usageCode=").append(waterConnectionRequest.getConnection().getProperty().getUsageType())
                .append("&tenantId=").append(waterConnectionRequest.getConnection().getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(1111111L).build();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        PropertyUsageTypeResponseInfo propCategory = new RestTemplate().postForObject(url.toString(),
                wrapper, PropertyUsageTypeResponseInfo.class);
        if (propCategory != null && propCategory.getPropCategory() != null && !propCategory.getPropCategory().isEmpty()
                && propCategory.getPropCategory().get(0).getId() != null) {
            waterConnectionRequest.getConnection().getProperty()
                    .setUsageTypeId(propCategory.getPropCategory().get(0).getUsageTypeId());
            isValidPropAndCategory = Boolean.TRUE;
        }
        return isValidPropAndCategory;
    }

    public PropertyResponse getPropertyDetailsByUpicNo(WaterConnectionReq waterRequestReq) {
        final RequestInfo requestInfo =waterRequestReq.getRequestInfo();
        StringBuilder url = new StringBuilder();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        url.append(configurationManager.getPropertyServiceHostNameTopic())
                .append(configurationManager.getPropertyServiceSearchPathTopic()).append("?upicNo=")
                .append(waterRequestReq.getConnection().getProperty().getPropertyidentifier())
                .append("&tenantId=").append(waterRequestReq.getConnection().getTenantId());
        logger.info("URL to invoke : " + url.toString());
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

        waterRequestReq.getConnection().setPropertyIdentifier(waterRequestReq.getConnection().getProperty().getPropertyidentifier());
        if(propResp.getProperties()!=null && !propResp.getProperties().isEmpty() && !propResp.getProperties().get(0).getOwners().isEmpty()){
            waterRequestReq.getConnection().getProperty().setNameOfApplicant(propResp.getProperties().get(0).getOwners().get(0).getName());
            waterRequestReq.getConnection().getProperty().setEmail(propResp.getProperties().get(0).getOwners().get(0).getEmailId());
            waterRequestReq.getConnection().getProperty().setMobileNumber(propResp.getProperties().get(0).getOwners().get(0).getMobileNumber());
            waterRequestReq.getConnection().getProperty().setZone(propResp.getProperties().get(0).getBoundary()!=null?propResp.getProperties().get(0).getBoundary().getRevenueBoundary().getName():null);
        }
        
        return propResp;
    }

    public List<Long> getPropertyDetailsByParams(RequestInfoWrapper wrapper, String urlToInvoke) {
		logger.info("URL to invoke for PropertyDetails : " + urlToInvoke);
		List<Long> propertyIdentifierList = new ArrayList<>();
		PropertyResponse propResp = invokePropertyAPI(urlToInvoke, wrapper);
		if (propResp != null && !propResp.getProperties().isEmpty()) {
			logger.info("Response obtained from Property Module : " + propResp);
			for (PropertyInfo pInfo : propResp.getProperties()) {
				logger.info("Retrieved UPIC Number : " + pInfo.getUpicNumber() + " from Property Module ");
				propertyIdentifierList.add(Long.valueOf(pInfo.getUpicNumber()));
			}
		}
		return propertyIdentifierList;
	}
    	    
	private PropertyResponse invokePropertyAPI(String url, RequestInfoWrapper wrapper) {
		try {
			return new RestTemplate().postForObject(url.toString(), wrapper, PropertyResponse.class);
		} catch (Exception e) {
			logger.error("Encountered an Exception :" + e);
			return null;
		}
	}

    public DonationResponseInfo validateDonationAmount(WaterConnectionReq waterConnectionRequest) {
        StringBuilder url = new StringBuilder();
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(waterConnectionRequest.getRequestInfo()).build();
        url.append(configurationManager.getWaterMasterServiceBasePathTopic())
                .append(configurationManager.getWaterMasterServiceDonationSearchPathTopic()).append("?propertyType=")
                .append(waterConnectionRequest.getConnection().getProperty().getPropertyType())
                .append("&usageType=").append(waterConnectionRequest.getConnection().getProperty().getUsageType())
                .append("&categoryType=")
                .append(waterConnectionRequest.getConnection().getCategoryType()).append(
                        "&maxHSCPipeSize=")
                .append(
                        waterConnectionRequest.getConnection().getPipesizeId())
                .append(
                        "&minHSCPipeSize=")
                .append(waterConnectionRequest.getConnection().getPipesizeId()).append(
                        "&tenantId=")
                .append(waterConnectionRequest.getConnection().getTenantId());
        DonationResponseInfo donation = new RestTemplate().postForObject(url.toString(), wrapper,
                DonationResponseInfo.class);
        if (donation != null && donation.getDonations() != null && !donation.getDonations().isEmpty()) {
            waterConnectionRequest.getConnection().setDonationCharge(donation.getDonations() != null
                    && donation.getDonations().get(0) != null ? new Double(donation.getDonations().get(0).getDonationAmount())
                            : 0d);
        }
        return donation;
    }

    
    public String getFinancialYear(final String tenantId) { 
            StringBuilder url = new StringBuilder();
            String year = getFiscalYear();
            String finYear = null;
            url.append(configurationManager.getFinanceServiceHostName())
                    .append(configurationManager.getFinanceServiceSearchPath());
            url.append("?tenantId=" + tenantId);
            url.append("&finYearRange=" + year);
            final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
            FinYearReq finYearReq = new FinYearReq(); 
            finYearReq.setRequestInfo(requestInfo);
            String response = null;
            try {
                response = new RestTemplate().postForObject(url.toString(), finYearReq, String.class);
            } catch (Exception ex) {
                throw new FinYearException("Error While obtaining Financial Year", "Error While obtaining Financial Year",
                        requestInfo);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            IdGenErrorRes errorResponse = gson.fromJson(response, IdGenErrorRes.class);
            FinYearRes finYearResponse = gson.fromJson(response, FinYearRes.class);
            if (!errorResponse.getErrors().isEmpty()) {
                throw new IdGenerationException("Error While generating ACK number", "Error While generating ACK number",
                        requestInfo);
            } else if (finYearResponse.getResponseInfo() != null) {
                if (finYearResponse.getResponseInfo().getStatus().toString()
                        .equalsIgnoreCase("SUCCESSFUL")) {
                    if (finYearResponse.getFinancialYear() != null && !finYearResponse.getFinancialYear().isEmpty())
                        finYear = finYearResponse.getFinancialYear();
                }
            }

            return finYear;
    }
    
    public String generateRequestedDocumentNumber(final String tenantId, final String nameServiceTopic, final String formatServiceTopic,RequestInfo requestInfo) {
        StringBuilder url = new StringBuilder();
        String ackNumber = null;
        url.append(configurationManager.getIdGenServiceBasePathTopic())
                .append(configurationManager.getIdGenServiceCreatePathTopic());
//        final RequestInfo RequestInfo = RequestInfo.builder().ts(11111111l).build();
        List<AckIdRequest> idRequests = new ArrayList<>();
        AckIdRequest idrequest = new AckIdRequest();
        
        idrequest.setIdName(nameServiceTopic);
        idrequest.setTenantId(tenantId);
        idrequest.setFormat(formatServiceTopic);
        AckNoGenerationRequest idGeneration = new AckNoGenerationRequest();
        idRequests.add(idrequest);
        idGeneration.setIdRequests(idRequests);
        idGeneration.setRequestInfo(requestInfo);
        String response = null;
        try {
                response = new RestTemplate().postForObject(url.toString(), idGeneration, String.class);
        } catch (Exception ex) {
            throw new IdGenerationException("Error While generating " +nameServiceTopic+" number", "Error While generating " + nameServiceTopic + " number", requestInfo);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        IdGenErrorRes errorResponse = gson.fromJson(response, IdGenErrorRes.class);
        AckNoGenerationResponse idResponse = gson.fromJson(response, AckNoGenerationResponse.class);
        if ( !errorResponse.getErrors().isEmpty()) {
                throw new IdGenerationException("Error While generating " + nameServiceTopic + " number", "Error While generating " + nameServiceTopic + " number", requestInfo);
        } else if (idResponse.getResponseInfo() != null) {
                if (idResponse.getResponseInfo().getStatus().toString()
                                .equalsIgnoreCase("SUCCESSFUL")) {
                        if (idResponse.getIdResponses() != null && idResponse.getIdResponses().size() > 0)
                            ackNumber = idResponse.getIdResponses().get(0).getId();
                }
        }
        
        if(nameServiceTopic.equals(configurationManager.getHscGenNameServiceTopic())) {
        	//Enable the below method call to get financial year from the Finance Service
            // String finYear = getFinancialYear(tenantId);
            /*String finYear = getFiscalYear();
            if(null!=finYear && !finYear.isEmpty()) { 
            	return ackNumber=tenantId.substring(0,4).concat(ackNumber);
            }	*/
        	String ulbName = getULBNameFromTenant(tenantId, requestInfo);
        	if(!ulbName.equals("")){ 
        		return ackNumber = ulbName.substring(0,4).concat(ackNumber);
        	} else {
        		return ackNumber = tenantId.substring(0,4).concat(ackNumber);
        	}
        }

        return ackNumber;
    }
    
	public String getULBNameFromTenant(String tenantId, RequestInfo requestInfo) { 
		StringBuilder url = new StringBuilder(configurationManager.getTenantServiceBasePath() + configurationManager.getTenantServiceSearchPath()); 
		url.append("?code=" + tenantId); 
		RequestInfoBody requestInfoBody = new RequestInfoBody(requestInfo); 
		final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
		logger.info("URL to invoke Tenant Service : " + url.toString());
		logger.info("Request Info to invoke the URL : " + request);
		String ulbCode = ""; 
        TenantResponse tr = new RestTemplate().postForObject(url.toString(), request, TenantResponse.class);
        if(null!=tr) { 
        	logger.info("Tenant Response : " + tr);
        	if(null != tr.getTenant()){ 
        		for(Tenant tenant : tr.getTenant()) { 
        			if(null != tenant.getCity()) { 
        				ulbCode = tenant.getCity().getName();  
        			}
        		}
        	}
        }
		return ulbCode;
	}
    
    public Demand getDemandEstimation(Connection connection) { 
    	StringBuilder url = new StringBuilder();
        url.append(configurationManager.getBillingDemandServiceHostNameTopic())
                .append(configurationManager.getSearchbillingDemandServiceTopic());
        url.append("?tenantId=" + connection.getTenantId());
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
        DemandRequest demandRequest = new DemandRequest();
        demandRequest.setRequestInfo(requestInfo);
        List<Demand> demandList = new ArrayList<>();
        String response = null;
        try {
            response = new RestTemplate().postForObject(url.toString(), demandRequest, String.class);
        } catch (Exception ex) {
            // throw new DemandException("Error While obtaining Demand Estimation Value", "Error While obtaining Demand Estimation Value",requestInfo);
        	logger.info(ex.getMessage());
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        DemandResponse demandResponse = gson.fromJson(response, DemandResponse.class);
		if (null != demandResponse) {
			logger.info("Demand Response : " + demandResponse);
			if (null != demandResponse.getResponseInfo()) {
				if (demandResponse.getResponseInfo().getStatus().toString().equalsIgnoreCase("SUCCESSFUL")) {
					if (demandResponse.getDemands() != null && !demandResponse.getDemands().isEmpty()) {
						demandList = demandResponse.getDemands();
					}

				}
			}
		}
        if(demandList.size() > 0) { 
        	return demandList.get(0); 
        }
        return null;
    }
    
    public BoundaryResponse getBoundaryNum(final String boundaryType, final String boundaryNum, final String tenantId) {
        String url = configurationManager.getLocationServiceBasePathTopic()
                + configurationManager.getLocationServiceBoundarySearchPathTopic();
        url = url.replace("{boundaryType}", boundaryType);
        url = url.replace("{boundaryNum}", boundaryNum);
        url = url.replace("{tenantId}", tenantId);
        final BoundaryResponse boundary = getBoundary(url);
        return boundary;
    }

    public BoundaryResponse getBoundary(final String url) {
        final BoundaryRequestInfo requestInfo = BoundaryRequestInfo.builder().build();
        final BoundaryRequestInfoWrapper wrapper = BoundaryRequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<BoundaryRequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final BoundaryResponse boundary = new RestTemplate().postForObject(url.toString(), request,
                BoundaryResponse.class);
        return boundary;
    }
    
    public BoundaryResponse getBoundaryName(final String boundaryType, final String[] boundaryNum, final String tenantId) {
        String url = configurationManager.getLocationServiceBasePathTopic()
                + configurationManager.getLocationServiceBoundarySearchPathTopic();
        final BoundaryRequestInfo requestInfo = BoundaryRequestInfo.builder().build();
        final BoundaryRequestInfoWrapper wrapper = BoundaryRequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<BoundaryRequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final BoundaryResponse boundary = new RestTemplate().postForObject(url.toString(), request,
                BoundaryResponse.class,boundaryType, boundaryNum,tenantId);
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
    	int month = calendarDate.get(Calendar.MONTH);
        int year = calendarDate.get(Calendar.YEAR);
        int value = (month >= FIRST_FISCAL_MONTH) ? year : year - 1;
        String finYear = Integer.toString(value)+ "-"+Integer.toString(value+1).substring(2, 4);
        return finYear;
    }
}
