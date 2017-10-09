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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.web.contract.CommonResponseInfo;
import org.egov.wcms.transaction.web.contract.NonMeterWaterRates;
import org.egov.wcms.transaction.web.contract.NonMeterWaterRatesResponse;
import org.egov.wcms.transaction.web.contract.PipeSizeResponseInfo;
import org.egov.wcms.transaction.web.contract.RequestInfoBody;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.StorageReservoirResponse;
import org.egov.wcms.transaction.web.contract.SupplyResponseInfo;
import org.egov.wcms.transaction.web.contract.Tenant;
import org.egov.wcms.transaction.web.contract.TenantResponse;
import org.egov.wcms.transaction.web.contract.TreatmentPlantResponse;
import org.egov.wcms.transaction.web.contract.UsageTypeResponse;
import org.egov.wcms.transaction.web.contract.WaterSourceResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConnectionMasterAdapter implements ApplicationRunner {

    public static volatile ConcurrentHashMap<String, CommonResponseInfo> supplyTypeMap = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, CommonResponseInfo> sourceTypeMap = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, CommonResponseInfo> pipeSizeMap = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, CommonResponseInfo> treatmentPlantMap = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, CommonResponseInfo> storageReservoirMap = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, CommonResponseInfo> usageTypeMap = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, CommonResponseInfo> subUsageTypeMap = new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String, List<NonMeterWaterRates>> nonMeterWaterRatesMap = new ConcurrentHashMap<>(); 
    public static volatile String supplyTypeUrl;
    public static volatile String sourceTypeUrl;
    public static volatile String pipeSizeUrl;
    public static volatile String treatmentPlantUrl;
    public static volatile String storageReservoirUrl;
    public static volatile String usageTypeUrl;
    public static volatile String subUsageTypeUrl;
    public static volatile String nonMeterWaterRatesUrl; 

    @Autowired
    private ConfigurationManager config;

    /**
     * Method to fetch Supply Type Masters from WCMS Masters and populate the Supply Type Map
     * @param eachTenant
     * @param request
     */
    private void fetchAllSupplyTypeMasters(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
        final StringBuilder url = new StringBuilder();
        url.append(config.getWaterMasterServiceBasePathTopic() + config.getWaterMasterServiceSupplySearchPathTopic());
        supplyTypeUrl = url.toString();
        url.append("?tenantId=" + eachTenant);
        final SupplyResponseInfo supplytype = new RestTemplate().postForObject(url.toString(), request,
                SupplyResponseInfo.class);
        if (null != supplytype && null != supplytype.getSupplytypes() && supplytype.getSupplytypes().size() > 0)
            for (final CommonResponseInfo supplyType : supplytype.getSupplytypes())
                supplyTypeMap.put(supplyType.getId(), supplyType);
    }

    /**
     * Method to fetch Source Type Masters from WCMS Masters and populate the Source Type Map
     * @param eachTenant
     * @param request
     */
    private void fetchAllSourceTypeMasters(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
        StringBuilder url = new StringBuilder();
        url = new StringBuilder();
        url.append(config.getWaterMasterServiceBasePathTopic() + config.getWaterMasterServiceSourceSearchPathTopic());
        sourceTypeUrl = url.toString();
        url.append("?tenantId=" + eachTenant);
        final WaterSourceResponseInfo sourcetype = new RestTemplate().postForObject(url.toString(), request,
                WaterSourceResponseInfo.class);
        if (null != sourcetype && null != sourcetype.getWaterSourceType()
                && sourcetype.getWaterSourceType().size() > 0)
            for (final CommonResponseInfo sourceType : sourcetype.getWaterSourceType())
                sourceTypeMap.put(sourceType.getId(), sourceType);
    }

    /**
     * Method to fetch Pipe Size Masters from WCMS Masters and populate the Pipe Size Map
     * @param eachTenant
     * @param request
     */
    private void fetchAllPipeSizeMasters(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
        StringBuilder url = new StringBuilder();
        url = new StringBuilder();
        url.append(config.getWaterMasterServiceBasePathTopic() + config.getWaterMasterServicePipesizeSearchPathTopic());
        pipeSizeUrl = url.toString();
        url.append("?tenantId=" + eachTenant);
        final PipeSizeResponseInfo pipesize = new RestTemplate().postForObject(url.toString(), request,
                PipeSizeResponseInfo.class);
        if (null != pipesize && null != pipesize.getPipeSize() && pipesize.getPipeSize().size() > 0)
            for (final CommonResponseInfo pipe : pipesize.getPipeSize())
                pipeSizeMap.put(pipe.getId(), pipe);
    }

    /**
     * Method to fetch Treatment Plant Masters from WCMS Masters and populate the Treatment Plant Map
     * @param eachTenant
     * @param request
     */
    private void fetchAllTreatmentPlantMasters(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
        StringBuilder url = new StringBuilder();
        url = new StringBuilder();
        url.append(config.getWaterMasterServiceBasePathTopic() + config.getWaterTreatmentSearchTopic());
        treatmentPlantUrl = url.toString();
        url.append("?tenantId=" + eachTenant);
        final TreatmentPlantResponse treatmentPlants = new RestTemplate().postForObject(url.toString(), request,
                TreatmentPlantResponse.class);
        if (null != treatmentPlants && null != treatmentPlants.getTreatmentPlants()
                && treatmentPlants.getTreatmentPlants().size() > 0)
            for (final CommonResponseInfo treatmentPlant : treatmentPlants.getTreatmentPlants())
                treatmentPlantMap.put(treatmentPlant.getId(), treatmentPlant);
    }

    /**
     * Method to fetch Storage Reservoir Masters from WCMS Masters and populate the Storage Map
     * @param eachTenant
     * @param request
     */
    private void fetchAllStorageReservoirMasters(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
        StringBuilder url = new StringBuilder();
        url = new StringBuilder();
        url.append(config.getWaterMasterServiceBasePathTopic() + config.getReservoirSearchTopic());
        storageReservoirUrl = url.toString();
        url.append("?tenantId=" + eachTenant);
        final StorageReservoirResponse storageResponse = new RestTemplate().postForObject(url.toString(), request,
                StorageReservoirResponse.class);
        if (null != storageResponse && null != storageResponse.getStorageReservoirs()
                && storageResponse.getStorageReservoirs().size() > 0)
            for (final CommonResponseInfo storage : storageResponse.getStorageReservoirs())
                storageReservoirMap.put(storage.getId(), storage);
    }

    /**
     * Method to fetch Usage Type Masters from WCMS Masters and populate the Usage Type Map
     * @param eachTenant
     * @param request
     */
    private void fetchAllUsageTypeMasters(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
        StringBuilder url = new StringBuilder();
        url = new StringBuilder();
        url.append(config.getWaterMasterServiceBasePathTopic() + config.getUsageTypeSearchPathTopic());
        usageTypeUrl = url.toString();
        url.append("?tenantId=" + eachTenant);
        final UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request,
                UsageTypeResponse.class);
        if (null != response && null != response.getUsageTypes() && response.getUsageTypes().size() > 0)
            for (final CommonResponseInfo usage : response.getUsageTypes())
                usageTypeMap.put(usage.getId(), usage);
    }

    /**
     * Method to fetch Sub Usage Type Masters from WCMS Masters and populate the Sub Usage Type Map
     * @param eachTenant
     * @param request
     */
    private void fetchAllSubUsageTypeMasters(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
        StringBuilder url = new StringBuilder();
        url = new StringBuilder();
        url.append(config.getWaterMasterServiceBasePathTopic() + config.getUsageTypeSearchPathTopic());
        subUsageTypeUrl = url.toString();
        url.append("?tenantId=" + eachTenant);
        url.append("&isSubUsageType=true");
        final UsageTypeResponse subResponse = new RestTemplate().postForObject(url.toString(), request,
                UsageTypeResponse.class);
        if (null != subResponse && null != subResponse.getUsageTypes() && subResponse.getUsageTypes().size() > 0)
            for (final CommonResponseInfo usage : subResponse.getUsageTypes())
                subUsageTypeMap.put(usage.getId(), usage);
    }
    
    /**
     * Method to fetch Non Meter Water Rates from WCMS Masters and populate the Map
     * @param eachTenant
     * @param request
     */
	private void fetchAllNonMeterWaterRates(final String eachTenant, final HttpEntity<RequestInfoWrapper> request) {
		StringBuilder url = new StringBuilder();
		url = new StringBuilder();
		url.append(config.getWaterMasterServiceBasePathTopic() + config.getNonMeterWaterRatesSearchPath());
		nonMeterWaterRatesUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		final NonMeterWaterRatesResponse meterRates = new RestTemplate().postForObject(url.toString(), request,
				NonMeterWaterRatesResponse.class);
		if (null != meterRates && null != meterRates.getNonMeterWaterRates()
				&& meterRates.getNonMeterWaterRates().size() > 0)
			nonMeterWaterRatesMap.put(eachTenant, meterRates.getNonMeterWaterRates());
	}

    @Override
    public void run(final ApplicationArguments arg0) throws Exception {

        List<String> tenantList = new ArrayList<>();
        tenantList = getAllTenantsInTheSystem();
        final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);

        for (final String eachTenant : tenantList) {

            fetchAllSupplyTypeMasters(eachTenant, request);

            fetchAllSourceTypeMasters(eachTenant, request);

            fetchAllPipeSizeMasters(eachTenant, request);

            fetchAllTreatmentPlantMasters(eachTenant, request);

            fetchAllStorageReservoirMasters(eachTenant, request);

            fetchAllUsageTypeMasters(eachTenant, request);

            fetchAllSubUsageTypeMasters(eachTenant, request);
            
            fetchAllNonMeterWaterRates(eachTenant, request); 

        }

        // Printing the Map Report
        log.info("ApplicationRunner Status ::::: ");
        log.info(supplyTypeMap.size() + " Supply Type entries loaded");
        log.info(sourceTypeMap.size() + " Source Type entries loaded");
        log.info(pipeSizeMap.size() + " Pipe Size entries loaded");
        log.info(treatmentPlantMap.size() + " Water Treatment Plant entries loaded");
        log.info(storageReservoirMap.size() + " Water Storage Reservoir entries loaded");
        log.info(usageTypeMap.size() + " Usage Type entries loaded");
        log.info(subUsageTypeMap.size() + " Sub Usage Type entries loaded");
        log.info(nonMeterWaterRatesMap.size() + " Non Meter Water Rates loaded"); 

    }

    /**
     * This method returns all the tenants available in the system
     * Further, Masters for all the tenants will be fetched in the above listed methods
     * @return List of String
     */
    private List<String> getAllTenantsInTheSystem() {
        final StringBuilder url = new StringBuilder(config.getTenantServiceBasePath() + config.getTenantServiceSearchPath());
        final List<String> tenantList = new ArrayList<>();
        final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().ts(11111111l).build());
        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
        log.info("URL to invoke Tenant Service : " + url.toString());
        log.info("Request Info to invoke the URL : " + request);
        final TenantResponse tr = new RestTemplate().postForObject(url.toString(), request, TenantResponse.class);
        if (null != tr) {
            log.info("Tenant Response : " + tr);
            if (null != tr.getTenant())
                for (final Tenant tenant : tr.getTenant())
                    tenantList.add(tenant.getCode());
        }
        log.info("Available Tenant List : " + tenantList.toString());
        return tenantList;
    }

    /**
     * This method receives the Supply Type in ID and Returns back the corresponding Name for that 
     * @param supplyTypeId
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static String getSupplyTypeById(final String supplyTypeId, final String tenantId, final RequestInfo requestInfo) {
        if (supplyTypeMap.containsKey(supplyTypeId))
            return supplyTypeMap.get(supplyTypeId).getName();
        else {
            fetchSupplyTypeByIdFromMasters(supplyTypeId, tenantId, requestInfo);
            if (supplyTypeMap.containsKey(supplyTypeId))
                return supplyTypeMap.get(supplyTypeId).getName();
            else
                return null;
        }
    }
    
    /**
     * This method receives Supply Type in Name and returns back the corresponding ID for that
     * @param supplyType
     * @param tenantId
     * @return
     */
    public static Long getSupplyTypeIdByName(final String supplyType, final String tenantId) { 
    	Iterator<java.util.Map.Entry<String, CommonResponseInfo>> itr = supplyTypeMap.entrySet().iterator();
    	Long id = 0L ; 
    	while(itr.hasNext()) { 
    		java.util.Map.Entry<String, CommonResponseInfo> entry = itr.next();
    		CommonResponseInfo eachRecord = entry.getValue();
    		if(eachRecord.getName().equals(supplyType) && eachRecord.getTenantId().equals(tenantId)) { 
    			id = Long.parseLong(entry.getKey()); 
    		}
    	}
    	return id; 
    }

    /**
     * If not available in Masters HashMaps, this method fetches the Supply Type from WCMS Masters
     * @param id
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static ConcurrentHashMap<String, CommonResponseInfo> fetchSupplyTypeByIdFromMasters(final String id,
            final String tenantId, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        url.append(supplyTypeUrl + "?tenantId=" + tenantId + "&id=" + id);
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final SupplyResponseInfo supplytype = new RestTemplate().postForObject(url.toString(), request,
                SupplyResponseInfo.class);
        if (null != supplytype && null != supplytype.getSupplytypes() && supplytype.getSupplytypes().size() > 0)
                for (final CommonResponseInfo supplyType : supplytype.getSupplytypes())
                    supplyTypeMap.put(supplyType.getId(), supplyType);
        return null;
    }

    /**
     * This method receives the Source Type in ID and Returns back the corresponding Name for that
     * @param sourceTypeId
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static String getSourceTypeById(final String sourceTypeId, final String tenantId, final RequestInfo requestInfo) {
        if (sourceTypeMap.containsKey(sourceTypeId)
                && StringUtils.isNotBlank(sourceTypeMap.get(sourceTypeId).getTenantId())
                && sourceTypeMap.get(sourceTypeId).getTenantId().equals(tenantId))
            return sourceTypeMap.get(sourceTypeId).getName();
        else {
            fetchSourceTypeByIdFromMasters(sourceTypeId, tenantId, requestInfo);
            if (sourceTypeMap.containsKey(sourceTypeId)
                    && StringUtils.isNotBlank(sourceTypeMap.get(sourceTypeId).getTenantId())
                    && sourceTypeMap.get(sourceTypeId).getTenantId().equals(tenantId))
                return sourceTypeMap.get(sourceTypeId).getName();
            else
                return null;
        }
    }
    
    /**
     * This method receives Source Type in Name and returns back the corresponding ID for that
     * @param sourceType
     * @param tenantId
     * @return
     */
    public static Long getSourceTypeIdByName(final String sourceType, final String tenantId) { 
    	Iterator<java.util.Map.Entry<String, CommonResponseInfo>> itr = sourceTypeMap.entrySet().iterator();
    	Long id = 0L ; 
    	while(itr.hasNext()) { 
    		java.util.Map.Entry<String, CommonResponseInfo> entry = itr.next();
    		CommonResponseInfo eachRecord = entry.getValue();
    		if(eachRecord.getName().equals(sourceType) && eachRecord.getTenantId().equals(tenantId)) { 
    			id = Long.parseLong(entry.getKey()); 
    		}
    	}
    	return id; 
    }

    /**
     * If not available in Masters HashMaps, this method fetches the Source Type from WCMS Masters
     * @param id
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static ConcurrentHashMap<String, CommonResponseInfo> fetchSourceTypeByIdFromMasters(final String id,
            final String tenantId, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        url.append(sourceTypeUrl + "?tenantId=" + tenantId + "&id=" + id);
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final WaterSourceResponseInfo sourceType = new RestTemplate().postForObject(url.toString(), request,
                WaterSourceResponseInfo.class);
        if (null != sourceType && null != sourceType.getWaterSourceType() && sourceType.getWaterSourceType().size() > 0)
                for (final CommonResponseInfo source : sourceType.getWaterSourceType())
                    sourceTypeMap.put(source.getId(), source);
        return null;
    }

    /**
     * This method receives the PipeSizeType in ID and Returns back the corresponding Name for that
     * @param pipeSizeId
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static String getPipeSizeById(final String pipeSizeId, final String tenantId, final RequestInfo requestInfo) {
        if (pipeSizeMap.containsKey(pipeSizeId) && StringUtils.isNotBlank(pipeSizeMap.get(pipeSizeId).getTenantId())
                && pipeSizeMap.get(pipeSizeId).getTenantId().equals(tenantId)) 
        	return String.valueOf(pipeSizeMap.get(pipeSizeId).getSizeInMilimeter());
        else {
            fetchPipeSizeByIdFromMasters(pipeSizeId, tenantId, requestInfo);
            if (pipeSizeMap.containsKey(pipeSizeId) && StringUtils.isNotBlank(pipeSizeMap.get(pipeSizeId).getTenantId())
                    && pipeSizeMap.get(pipeSizeId).getTenantId().equals(tenantId))
            	return String.valueOf(pipeSizeMap.get(pipeSizeId).getSizeInMilimeter());
            else
                return null;
        }
    }
    
    /**
     * This method receives PipeSize Type in Name and returns back the corresponding ID for that
     * @param pipeSize
     * @param tenantId
     * @return
     */
    public static Long getPipeSizeTypeIdByName(final String pipeSize, final String tenantId) {  
    	Iterator<java.util.Map.Entry<String, CommonResponseInfo>> itr = pipeSizeMap.entrySet().iterator();
    	Long id = 0L ; 
    	while(itr.hasNext()) { 
    		java.util.Map.Entry<String, CommonResponseInfo> entry = itr.next();
    		CommonResponseInfo eachRecord = entry.getValue();
    		if(String.valueOf(eachRecord.getSizeInMilimeter()).equals(pipeSize) && eachRecord.getTenantId().equals(tenantId)) { 
    			id = Long.parseLong(entry.getKey()); 
    		}
    	}
    	return id; 
    }

    /**
     * If not available in Masters HashMaps, this method fetches the Pipe Size Type from WCMS Masters
     * @param id
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static ConcurrentHashMap<String, CommonResponseInfo> fetchPipeSizeByIdFromMasters(final String id,
            final String tenantId,
            final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        url.append(pipeSizeUrl + "?tenantId=" + tenantId + "&id=" + id);
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final PipeSizeResponseInfo pipeSize = new RestTemplate().postForObject(url.toString(), request,
                PipeSizeResponseInfo.class);
        if (null != pipeSize && null != pipeSize.getPipeSize() && pipeSize.getPipeSize().size() > 0)
                for (final CommonResponseInfo pipes : pipeSize.getPipeSize())
                    pipeSizeMap.put(pipes.getId(), pipes);
        return null;
    }
    
    /**
     * This method receives the Treatment Plant Type in ID and Returns back the corresponding Name for that
     * @param treatmentPlantId
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static String getTreatmentPlantById(final String treatmentPlantId, final String tenantId,
            final RequestInfo requestInfo) {
        if (treatmentPlantMap.containsKey(treatmentPlantId)
                && StringUtils.isNotBlank(treatmentPlantMap.get(treatmentPlantId).getTenantId())
                && treatmentPlantMap.get(treatmentPlantId).getTenantId().equals(tenantId))
            return treatmentPlantMap.get(treatmentPlantId).getName();
        else {
            fetchTreatmentPlantByIdFromMasters(treatmentPlantId, tenantId, requestInfo);
            if (treatmentPlantMap.containsKey(treatmentPlantId)
                    && StringUtils.isNotBlank(treatmentPlantMap.get(treatmentPlantId).getTenantId())
                    && treatmentPlantMap.get(treatmentPlantId).getTenantId().equals(tenantId))
                return treatmentPlantMap.get(treatmentPlantId).getName();
            else
                return null;
        }
    }
    
    /**
     * This method receives Treatment Plant Type in Name and returns back the corresponding ID for that
     * @param treatmentPlant
     * @param tenantId
     * @return
     */
    public static Long getTreatmentPlantIdByName(final String treatmentPlant, final String tenantId) { 
    	Iterator<java.util.Map.Entry<String, CommonResponseInfo>> itr = treatmentPlantMap.entrySet().iterator();
    	Long id = 0L ; 
    	while(itr.hasNext()) { 
    		java.util.Map.Entry<String, CommonResponseInfo> entry = itr.next();
    		CommonResponseInfo eachRecord = entry.getValue();
    		if(eachRecord.getName().equals(treatmentPlant) && eachRecord.getTenantId().equals(tenantId)) { 
    			id = Long.parseLong(entry.getKey()); 
    		}
    	}
    	return id; 
    }

    /**
     * If not available in Masters HashMaps, this method fetches the Treatment Plant Type from WCMS Masters
     * @param id
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static ConcurrentHashMap<String, CommonResponseInfo> fetchTreatmentPlantByIdFromMasters(final String id,
            final String tenantId, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        url.append(treatmentPlantUrl + "?tenantId=" + tenantId + "&id=" + id);
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final TreatmentPlantResponse treatmentPlants = new RestTemplate().postForObject(url.toString(), request,
                TreatmentPlantResponse.class);
        if (null != treatmentPlants && null != treatmentPlants.getTreatmentPlants() && treatmentPlants.getTreatmentPlants().size() > 0)
                for (final CommonResponseInfo pipes : treatmentPlants.getTreatmentPlants())
                    treatmentPlantMap.put(pipes.getId(), pipes);
        return null;
    }
    
    /**
     * This method receives the Storage Reservoir Type in ID and Returns back the corresponding Name for that
     * @param storageReservoirId
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static String getStorageReservoirById(final String storageReservoirId, final String tenantId,
            final RequestInfo requestInfo) {
        if (storageReservoirMap.containsKey(storageReservoirId)
                && StringUtils.isNotBlank(storageReservoirMap.get(storageReservoirId).getTenantId())
                && storageReservoirMap.get(storageReservoirId).getTenantId().equals(tenantId))
            return storageReservoirMap.get(storageReservoirId).getName();
        else {
            fetchStorageReservoirByIdFromMasters(storageReservoirId, tenantId, requestInfo);
            if (storageReservoirMap.containsKey(storageReservoirId)
                    && StringUtils.isNotBlank(storageReservoirMap.get(storageReservoirId).getTenantId())
                    && storageReservoirMap.get(storageReservoirId).getTenantId().equals(tenantId))
                return storageReservoirMap.get(storageReservoirId).getName();
            else
                return null;
        }
    }
    
    /**
     * This method receives Storage Reservoir Type in Name and returns back the corresponding ID for that
     * @param storageReservoir
     * @param tenantId
     * @return
     */
    public static Long getStorageReservoirIdByName(final String storageReservoir, final String tenantId) { 
    	Iterator<java.util.Map.Entry<String, CommonResponseInfo>> itr = storageReservoirMap.entrySet().iterator();
    	Long id = 0L ; 
    	while(itr.hasNext()) { 
    		java.util.Map.Entry<String, CommonResponseInfo> entry = itr.next();
    		CommonResponseInfo eachRecord = entry.getValue();
    		if(eachRecord.getName().equals(storageReservoir) && eachRecord.getTenantId().equals(tenantId)) { 
    			id = Long.parseLong(entry.getKey()); 
    		}
    	}
    	return id; 
    }

    /**
     * If not available in Masters HashMaps, this method fetches the Storage Reservoir Type from WCMS Masters
     * @param id
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static ConcurrentHashMap<String, CommonResponseInfo> fetchStorageReservoirByIdFromMasters(final String id,
            final String tenantId, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        url.append(storageReservoirUrl + "?tenantId=" + tenantId + "&id=" + id);
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final StorageReservoirResponse storageReservoirs = new RestTemplate().postForObject(url.toString(), request,
                StorageReservoirResponse.class);
        if (null != storageReservoirs && null != storageReservoirs.getStorageReservoirs()
                && storageReservoirs.getStorageReservoirs().size() > 0)
            for (final CommonResponseInfo pipes : storageReservoirs.getStorageReservoirs())
                storageReservoirMap.put(pipes.getId(), pipes);
        return null;
    }

    /**
     * This method receives the Usage Type in ID and Returns back the corresponding Name for that
     * @param usageTypeId
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static String getUsageTypeById(final String usageTypeId, final String tenantId, final RequestInfo requestInfo) {
        if (usageTypeMap.containsKey(usageTypeId) && StringUtils.isNotBlank(usageTypeMap.get(usageTypeId).getTenantId())
                && usageTypeMap.get(usageTypeId).getTenantId().equals(tenantId))
            return usageTypeMap.get(usageTypeId).getName()+"~"+usageTypeMap.get(usageTypeId).getCode();
        else {
            fetchUsageTypeByIdFromMasters(usageTypeId, tenantId, requestInfo);
            if (usageTypeMap.containsKey(usageTypeId)
                    && StringUtils.isNotBlank(usageTypeMap.get(usageTypeId).getTenantId())
                    && usageTypeMap.get(usageTypeId).getTenantId().equals(tenantId))
                return usageTypeMap.get(usageTypeId).getName()+"~"+usageTypeMap.get(usageTypeId).getCode();
            else
                return null;
        }
    }
    
    /**
     * This method receives Usage Type in Name and returns back the corresponding ID for that
     * @param usageType
     * @param tenantId
     * @return
     */
    public static Long getUsageTypeIdByName(final String usageType, final String tenantId) { 
    	Iterator<java.util.Map.Entry<String, CommonResponseInfo>> itr = usageTypeMap.entrySet().iterator();
    	Long id = 0L ; 
    	while(itr.hasNext()) { 
    		java.util.Map.Entry<String, CommonResponseInfo> entry = itr.next();
    		CommonResponseInfo eachRecord = entry.getValue();
    		if(eachRecord.getCode().equals(usageType) && eachRecord.getTenantId().equals(tenantId)) { 
    			id = Long.parseLong(entry.getKey()); 
    		}
    	}
    	return id; 
    }

    /**
     * If not available in Masters HashMaps, this method fetches the Usage Type from WCMS Masters
     * @param id
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static ConcurrentHashMap<String, CommonResponseInfo> fetchUsageTypeByIdFromMasters(final String id,
            final String tenantId, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        url.append(usageTypeUrl + "?tenantId=" + tenantId + "&ids=" + id);
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
        if (null != response && null != response.getUsageTypes() && response.getUsageTypes().size() > 0)
            for (final CommonResponseInfo usage : response.getUsageTypes())
                usageTypeMap.put(usage.getId(), usage);
        return null;
    }

    /**
     * This method receives the Sub Usage Type in ID and Returns back the corresponding Name for that
     * @param subUsageTypeId
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static String getSubUsageTypeById(final String subUsageTypeId, final String tenantId, final RequestInfo requestInfo) {
        if (subUsageTypeMap.containsKey(subUsageTypeId)
                && StringUtils.isNotBlank(subUsageTypeMap.get(subUsageTypeId).getTenantId())
                && subUsageTypeMap.get(subUsageTypeId).getTenantId().equals(tenantId))
            return subUsageTypeMap.get(subUsageTypeId).getName()+"~"+subUsageTypeMap.get(subUsageTypeId).getCode();
        else {
            fetchSubUsageTypeByIdFromMasters(subUsageTypeId, tenantId, requestInfo);
            if (subUsageTypeMap.containsKey(subUsageTypeId)
                    && StringUtils.isNotBlank(subUsageTypeMap.get(subUsageTypeId).getTenantId())
                    && subUsageTypeMap.get(subUsageTypeId).getTenantId().equals(tenantId))
                return subUsageTypeMap.get(subUsageTypeId).getName()+"~"+subUsageTypeMap.get(subUsageTypeId).getCode();
            else
                return null;
        }
    }
    
    /**
     * This method receives Sub Usage Type in Name and returns back the corresponding ID for that
     * @param subUsageType
     * @param tenantId
     * @return
     */
    public static Long getSubUsageTypeIdByName(final String subUsageType, final String tenantId) { 
    	Iterator<java.util.Map.Entry<String, CommonResponseInfo>> itr = subUsageTypeMap.entrySet().iterator();
    	Long id = 0L ; 
    	while(itr.hasNext()) { 
    		java.util.Map.Entry<String, CommonResponseInfo> entry = itr.next();
    		CommonResponseInfo eachRecord = entry.getValue();
    		if(eachRecord.getCode().equals(subUsageType) && eachRecord.getTenantId().equals(tenantId)) { 
    			id = Long.parseLong(entry.getKey()); 
    		}
    	}
    	return id; 
    }

    /**
     * If not available in Masters HashMaps, this method fetches the Sub Usage Type from WCMS Masters
     * @param id
     * @param tenantId
     * @param requestInfo
     * @return
     */
    public static ConcurrentHashMap<String, CommonResponseInfo> fetchSubUsageTypeByIdFromMasters(final String id,
            final String tenantId, final RequestInfo requestInfo) {
        final StringBuilder url = new StringBuilder();
        url.append(subUsageTypeUrl + "?tenantId=" + tenantId + "&ids=" + id + "&isSubUsageType=true");
        final RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        final UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
        if (null != response && null != response.getUsageTypes() && response.getUsageTypes().size() > 0)
            for (final CommonResponseInfo usage : response.getUsageTypes())
                subUsageTypeMap.put(usage.getId(), usage);
        return null;
    }
    
    /**
     * This method fetches all the Non Meter Water Rates which have been configured in WCMS Masters
     * @param sourceType
     * @param connectionType
     * @param usageType
     * @param subUsageType
     * @param hscPipeSizeType
     * @param noOfTaps
     * @param tenantId
     * @param requestInfo
     * @return
     */
	public static Double getNonMeterWaterRatesByParams(final String sourceType, final String connectionType,
			final String usageType, final String subUsageType, final String hscPipeSizeType, final int noOfTaps,
			final String tenantId, final RequestInfo requestInfo) {
		
		if(nonMeterWaterRatesMap.containsKey(tenantId)){ 
			List<NonMeterWaterRates> rateList = nonMeterWaterRatesMap.get(tenantId); 
			for(NonMeterWaterRates eachRate : rateList) { 
				if(sourceType.equals(eachRate.getSourceTypeName()) && connectionType.equals(eachRate.getConnectionType()) 
						&& usageType.equals(eachRate.getUsageTypeCode()) && subUsageType.equals(eachRate.getSubUsageTypeCode())
						&& hscPipeSizeType.equals(String.valueOf(eachRate.getPipeSize())) && noOfTaps == eachRate.getNoOfTaps()) { 
					return eachRate.getAmount();
				}
			}
		}
		return null; 
	}

}
