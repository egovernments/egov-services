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

package org.egov.wcms.transaction.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.config.ConfigurationManager;
import org.egov.wcms.transaction.web.contract.CommonResponseInfo;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ConnectionMasterAdapter implements ApplicationRunner {
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> supplyTypeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> sourceTypeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> pipeSizeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> treatmentPlantMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> storageReservoirMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> usageTypeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> subUsageTypeMap = new ConcurrentHashMap<>();
	public static volatile String supplyTypeUrl;
	public static volatile String sourceTypeUrl;
	public static volatile String pipeSizeUrl;
	public static volatile String treatmentPlantUrl;
	public static volatile String storageReservoirUrl;
	public static volatile String usageTypeUrl;
	public static volatile String subUsageTypeUrl;

	public static final Logger LOGGER = LoggerFactory.getLogger(ConnectionMasterAdapter.class);

	@Autowired
	private ConfigurationManager config;
	
	/**
	 * Method to fetch Supply Type Masters from WCMS Masters and populate the Supply Type Map
	 * @param eachTenant
	 * @param request
	 */
	private void fetchAllSupplyTypeMasters(String eachTenant, HttpEntity<RequestInfoWrapper> request) { 
		StringBuilder url = new StringBuilder();
		url.append(config.getMastersHostName() + config.getMastersSupplyTypeSearch());
		supplyTypeUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		SupplyResponseInfo supplytype = new RestTemplate().postForObject(url.toString(), request,
				SupplyResponseInfo.class);
		if (null != supplytype && null != supplytype.getSupplytypes() && supplytype.getSupplytypes().size() > 0) {
			for (CommonResponseInfo supplyType : supplytype.getSupplytypes()) {
				supplyTypeMap.put(supplyType.getId(), supplyType);
			}
		}
	}
	
	/**
	 * Method to fetch Source Type Masters from WCMS Masters and populate the Source Type Map
	 * @param eachTenant
	 * @param request
	 */
	private void fetchAllSourceTypeMasters(String eachTenant, HttpEntity<RequestInfoWrapper> request) { 
		StringBuilder url = new StringBuilder();
		url = new StringBuilder();
		url.append(config.getMastersHostName() + config.getMastersSourceTypeSearch());
		sourceTypeUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		WaterSourceResponseInfo sourcetype = new RestTemplate().postForObject(url.toString(), request,
				WaterSourceResponseInfo.class);
		if (null != sourcetype && null != sourcetype.getWaterSourceType()
				&& sourcetype.getWaterSourceType().size() > 0) {
			for (CommonResponseInfo sourceType : sourcetype.getWaterSourceType()) {
				sourceTypeMap.put(sourceType.getId(), sourceType);
			}
		}
	}
	
	/**
	 * Method to fetch Pipe Size Masters from WCMS Masters and populate the Pipe Size Map
	 * @param eachTenant
	 * @param request
	 */
	private void fetchAllPipeSizeMasters(String eachTenant, HttpEntity<RequestInfoWrapper> request) {
		StringBuilder url = new StringBuilder();
		url = new StringBuilder();
		url.append(config.getMastersHostName() + config.getMastersPipeSizeSearch());
		pipeSizeUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		PipeSizeResponseInfo pipesize = new RestTemplate().postForObject(url.toString(), request,
				PipeSizeResponseInfo.class);
		if (null != pipesize && null != pipesize.getPipeSize() && pipesize.getPipeSize().size() > 0) {
			for (CommonResponseInfo pipe : pipesize.getPipeSize()) {
				pipeSizeMap.put(pipe.getId(), pipe);
			}
		}
	}

	/**
	 * Method to fetch Treatment Plant Masters from WCMS Masters and populate the Treatment Plant Map
	 * @param eachTenant
	 * @param request
	 */
	private void fetchAllTreatmentPlantMasters(String eachTenant, HttpEntity<RequestInfoWrapper> request) {
		StringBuilder url = new StringBuilder();
		url = new StringBuilder();
		url.append(config.getMastersHostName() + config.getMastersTreatmentSearch());
		treatmentPlantUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		TreatmentPlantResponse treatmentPlants = new RestTemplate().postForObject(url.toString(), request,
				TreatmentPlantResponse.class);
		if (null != treatmentPlants && null != treatmentPlants.getTreatmentPlants()
				&& treatmentPlants.getTreatmentPlants().size() > 0) {
			for (CommonResponseInfo treatmentPlant : treatmentPlants.getTreatmentPlants()) {
				treatmentPlantMap.put(treatmentPlant.getId(), treatmentPlant);
			}
		}
	}
	
	/**
	 * Method to fetch Storage Reservoir Masters from WCMS Masters and populate the Storage Map
	 * @param eachTenant
	 * @param request
	 */
	private void fetchAllStorageReservoirMasters(String eachTenant, HttpEntity<RequestInfoWrapper> request) {
		StringBuilder url = new StringBuilder();
		url = new StringBuilder();
		url.append(config.getMastersHostName() + config.getMastersReservoirSearch());
		storageReservoirUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		StorageReservoirResponse storageResponse = new RestTemplate().postForObject(url.toString(), request,
				StorageReservoirResponse.class);
		if (null != storageResponse && null != storageResponse.getStorageReservoirs()
				&& storageResponse.getStorageReservoirs().size() > 0) {
			for (CommonResponseInfo storage : storageResponse.getStorageReservoirs()) {
				storageReservoirMap.put(storage.getId(), storage);
			}
		}
	}
	
	/**
	 * Method to fetch Usage Type Masters from WCMS Masters and populate the Usage Type Map
	 * @param eachTenant
	 * @param request
	 */
	private void fetchAllUsageTypeMasters(String eachTenant, HttpEntity<RequestInfoWrapper> request) {
		StringBuilder url = new StringBuilder();
		url = new StringBuilder();
		url.append(config.getMastersHostName() + config.getMastersUsageTypeSearch());
		usageTypeUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request,
				UsageTypeResponse.class);
		if (null != response && null != response.getUsageTypes() && response.getUsageTypes().size() > 0) {
			for (CommonResponseInfo usage : response.getUsageTypes()) {
				usageTypeMap.put(usage.getId(), usage);
			}
		}
	}
	
	/**
	 * Method to fetch Sub Usage Type Masters from WCMS Masters and populate the Sub Usage Type Map
	 * @param eachTenant
	 * @param request
	 */
	private void fetchAllSubUsageTypeMasters(String eachTenant, HttpEntity<RequestInfoWrapper> request) {
		StringBuilder url = new StringBuilder();
		url = new StringBuilder();
		url.append(config.getMastersHostName() + config.getMastersSubUsageTypeSearch());
		subUsageTypeUrl = url.toString();
		url.append("?tenantId=" + eachTenant);
		url.append("&isSubUsageType=true");
		UsageTypeResponse subResponse = new RestTemplate().postForObject(url.toString(), request,
				UsageTypeResponse.class);
		if (null != subResponse && null != subResponse.getUsageTypes() && subResponse.getUsageTypes().size() > 0) {
			for (CommonResponseInfo usage : subResponse.getUsageTypes()) {
				subUsageTypeMap.put(usage.getId(), usage);
			}
		}
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {

		List<String> tenantList = new ArrayList<>();
		tenantList = getAllTenantsInTheSystem();
		final RequestInfo requestInfo = RequestInfo.builder().ts(11111111l).build();
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);

		for (String eachTenant : tenantList) {

			fetchAllSupplyTypeMasters(eachTenant, request);

			fetchAllSourceTypeMasters(eachTenant, request);
			
			fetchAllPipeSizeMasters(eachTenant, request);
			
			fetchAllTreatmentPlantMasters(eachTenant, request);
			
			fetchAllStorageReservoirMasters(eachTenant, request);
			
			fetchAllUsageTypeMasters(eachTenant, request);
			
			fetchAllSubUsageTypeMasters(eachTenant, request);
			
		}
		
		// Printing the Map Report
		LOGGER.info("ApplicationRunner Status ::::: ");
		LOGGER.info(supplyTypeMap.size() + " Supply Type entries loaded");
		LOGGER.info(sourceTypeMap.size() + " Source Type entries loaded");
		LOGGER.info(pipeSizeMap.size() + " Pipe Size entries loaded");
		LOGGER.info(treatmentPlantMap.size() + " Water Treatment Plant entries loaded");
		LOGGER.info(storageReservoirMap.size() + " Water Storage Reservoir entries loaded"); 
		LOGGER.info(usageTypeMap.size() + " Usage Type entries loaded");
		LOGGER.info(subUsageTypeMap.size() + " Sub Usage Type entries loaded");

	}

	private List<String> getAllTenantsInTheSystem() {
		StringBuilder url = new StringBuilder(config.getTenantServiceBasePath() + config.getTenantServiceSearchPath());
		List<String> tenantList = new ArrayList<>();
		RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().ts(11111111l).build());
		final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
		LOGGER.info("URL to invoke Tenant Service : " + url.toString());
		LOGGER.info("Request Info to invoke the URL : " + request);
		TenantResponse tr = new RestTemplate().postForObject(url.toString(), request, TenantResponse.class);
		if (null != tr) {
			LOGGER.info("Tenant Response : " + tr);
			if (null != tr.getTenant()) {
				for (Tenant tenant : tr.getTenant()) {
					tenantList.add(tenant.getCode());
				}
			}
		}
		LOGGER.info("Available Tenant List : " + tenantList.toString());
		return tenantList;
	}

	public static String getSupplyTypeById(String supplyTypeId, String tenantId, RequestInfo requestInfo) {
		if (supplyTypeMap.containsKey(supplyTypeId)) {
			return supplyTypeMap.get(supplyTypeId).getName();
		} else {
			fetchSupplyTypeByIdFromMasters(supplyTypeId, tenantId, requestInfo);
			if (supplyTypeMap.containsKey(supplyTypeId)) {
				return supplyTypeMap.get(supplyTypeId).getName();
			} else {
				return null;
			}
		}
	}

	public static ConcurrentHashMap<String, CommonResponseInfo> fetchSupplyTypeByIdFromMasters(String id,
			String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append(supplyTypeUrl + "?tenantId=" + tenantId + "&id=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		SupplyResponseInfo supplytype = new RestTemplate().postForObject(url.toString(), request,
				SupplyResponseInfo.class);
		if (null != supplytype) {
			if (null != supplytype.getSupplytypes() && supplytype.getSupplytypes().size() > 0) {
				for (CommonResponseInfo supplyType : supplytype.getSupplytypes()) {
					supplyTypeMap.put(supplyType.getId(), supplyType);
				}
			}
		}
		return null;
	}

	public static String getSourceTypeById(String sourceTypeId, String tenantId, RequestInfo requestInfo) {
		if (sourceTypeMap.containsKey(sourceTypeId)
				&& StringUtils.isNotBlank(sourceTypeMap.get(sourceTypeId).getTenantId())
				&& sourceTypeMap.get(sourceTypeId).getTenantId().equals(tenantId)) {
			return sourceTypeMap.get(sourceTypeId).getName();
		} else {
			fetchSourceTypeByIdFromMasters(sourceTypeId, tenantId, requestInfo);
			if (sourceTypeMap.containsKey(sourceTypeId)
					&& StringUtils.isNotBlank(sourceTypeMap.get(sourceTypeId).getTenantId())
					&& sourceTypeMap.get(sourceTypeId).getTenantId().equals(tenantId)) {
				return sourceTypeMap.get(sourceTypeId).getName();
			} else {
				return null;
			}
		}
	}

	public static ConcurrentHashMap<String, CommonResponseInfo> fetchSourceTypeByIdFromMasters(String id,
			String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append(sourceTypeUrl + "?tenantId=" + tenantId + "&id=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		WaterSourceResponseInfo sourceType = new RestTemplate().postForObject(url.toString(), request,
				WaterSourceResponseInfo.class);
		if (null != sourceType) {
			if (null != sourceType.getWaterSourceType() && sourceType.getWaterSourceType().size() > 0) {
				for (CommonResponseInfo source : sourceType.getWaterSourceType()) {
					sourceTypeMap.put(source.getId(), source);
				}
			}
		}
		return null;
	}

	public static String getPipeSizeById(String pipeSizeId, String tenantId, RequestInfo requestInfo) {
		if (pipeSizeMap.containsKey(pipeSizeId) && StringUtils.isNotBlank(pipeSizeMap.get(pipeSizeId).getTenantId())
				&& pipeSizeMap.get(pipeSizeId).getTenantId().equals(tenantId)) {
			return Long.toString(pipeSizeMap.get(pipeSizeId).getValue());
		} else {
			fetchPipeSizeByIdFromMasters(pipeSizeId, tenantId, requestInfo);
			if (pipeSizeMap.containsKey(pipeSizeId) && StringUtils.isNotBlank(pipeSizeMap.get(pipeSizeId).getTenantId())
					&& pipeSizeMap.get(pipeSizeId).getTenantId().equals(tenantId)) {
				return Long.toString(pipeSizeMap.get(pipeSizeId).getValue());
			} else {
				return null;
			}
		}
	}

	public static ConcurrentHashMap<String, CommonResponseInfo> fetchPipeSizeByIdFromMasters(String id, String tenantId,
			RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append(pipeSizeUrl + "?tenantId=" + tenantId + "&id=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		PipeSizeResponseInfo pipeSize = new RestTemplate().postForObject(url.toString(), request,
				PipeSizeResponseInfo.class);
		if (null != pipeSize) {
			if (null != pipeSize.getPipeSize() && pipeSize.getPipeSize().size() > 0) {
				for (CommonResponseInfo pipes : pipeSize.getPipeSize()) {
					pipeSizeMap.put(pipes.getId(), pipes);
				}
			}
		}
		return null;
	}

	public static String getTreatmentPlantById(String treatmentPlantId, String tenantId, RequestInfo requestInfo) {
		if (treatmentPlantMap.containsKey(treatmentPlantId)
				&& StringUtils.isNotBlank(treatmentPlantMap.get(treatmentPlantId).getTenantId())
				&& treatmentPlantMap.get(treatmentPlantId).getTenantId().equals(tenantId)) {
			return treatmentPlantMap.get(treatmentPlantId).getName();
		} else {
			fetchTreatmentPlantByIdFromMasters(treatmentPlantId, tenantId, requestInfo);
			if (treatmentPlantMap.containsKey(treatmentPlantId)
					&& StringUtils.isNotBlank(treatmentPlantMap.get(treatmentPlantId).getTenantId())
					&& treatmentPlantMap.get(treatmentPlantId).getTenantId().equals(tenantId)) {
				return treatmentPlantMap.get(treatmentPlantId).getName();
			} else {
				return null;
			}
		}
	}

	public static ConcurrentHashMap<String, CommonResponseInfo> fetchTreatmentPlantByIdFromMasters(String id,
			String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append(treatmentPlantUrl + "?tenantId=" + tenantId + "&id=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		TreatmentPlantResponse treatmentPlants = new RestTemplate().postForObject(url.toString(), request,
				TreatmentPlantResponse.class);
		if (null != treatmentPlants) {
			if (null != treatmentPlants.getTreatmentPlants() && treatmentPlants.getTreatmentPlants().size() > 0) {
				for (CommonResponseInfo pipes : treatmentPlants.getTreatmentPlants()) {
					treatmentPlantMap.put(pipes.getId(), pipes);
				}
			}
		}
		return null;
	}

	public static String getStorageReservoiById(String storageReservoirId, String tenantId, RequestInfo requestInfo) {
		if (storageReservoirMap.containsKey(storageReservoirId)
				&& StringUtils.isNotBlank(storageReservoirMap.get(storageReservoirId).getTenantId())
				&& storageReservoirMap.get(storageReservoirId).getTenantId().equals(tenantId)) {
			return storageReservoirMap.get(storageReservoirId).getName();
		} else {
			fetchStorageReservoirByIdFromMasters(storageReservoirId, tenantId, requestInfo);
			if (storageReservoirMap.containsKey(storageReservoirId)
					&& StringUtils.isNotBlank(storageReservoirMap.get(storageReservoirId).getTenantId())
					&& storageReservoirMap.get(storageReservoirId).getTenantId().equals(tenantId)) {
				return storageReservoirMap.get(storageReservoirId).getName();
			} else {
				return null;
			}
		}
	}

	public static ConcurrentHashMap<String, CommonResponseInfo> fetchStorageReservoirByIdFromMasters(String id,
			String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append(storageReservoirUrl + "?tenantId=" + tenantId + "&id=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		StorageReservoirResponse storageReservoirs = new RestTemplate().postForObject(url.toString(), request,
				StorageReservoirResponse.class);
		if (null != storageReservoirs) {
			if (null != storageReservoirs.getStorageReservoirs()
					&& storageReservoirs.getStorageReservoirs().size() > 0) {
				for (CommonResponseInfo pipes : storageReservoirs.getStorageReservoirs()) {
					storageReservoirMap.put(pipes.getId(), pipes);
				}
			}
		}
		return null;
	}

	public static String getUsageTypeById(String usageTypeId, String tenantId, RequestInfo requestInfo) {
		if (usageTypeMap.containsKey(usageTypeId) && StringUtils.isNotBlank(usageTypeMap.get(usageTypeId).getTenantId())
				&& usageTypeMap.get(usageTypeId).getTenantId().equals(tenantId)) {
			return usageTypeMap.get(usageTypeId).getName();
		} else {
			fetchUsageTypeByIdFromMasters(usageTypeId, tenantId, requestInfo);
			if (usageTypeMap.containsKey(usageTypeId)
					&& StringUtils.isNotBlank(usageTypeMap.get(usageTypeId).getTenantId())
					&& usageTypeMap.get(usageTypeId).getTenantId().equals(tenantId)) {
				return usageTypeMap.get(usageTypeId).getName();
			} else {
				return null;
			}
		}
	}

	public static ConcurrentHashMap<String, CommonResponseInfo> fetchUsageTypeByIdFromMasters(String id,
			String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append(usageTypeUrl + "?tenantId=" + tenantId + "&ids=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
		if (null != response) {
			if (null != response.getUsageTypes() && response.getUsageTypes().size() > 0) {
				for (CommonResponseInfo usage : response.getUsageTypes()) {
					usageTypeMap.put(usage.getId(), usage);
				}
			}
		}
		return null;
	}

	public static String getSubUsageTypeById(String subUsageTypeId, String tenantId, RequestInfo requestInfo) {
		if (subUsageTypeMap.containsKey(subUsageTypeId)
				&& StringUtils.isNotBlank(subUsageTypeMap.get(subUsageTypeId).getTenantId())
				&& subUsageTypeMap.get(subUsageTypeId).getTenantId().equals(tenantId)) {
			return subUsageTypeMap.get(subUsageTypeId).getName();
		} else {
			fetchSubUsageTypeByIdFromMasters(subUsageTypeId, tenantId, requestInfo);
			if (subUsageTypeMap.containsKey(subUsageTypeId)
					&& StringUtils.isNotBlank(subUsageTypeMap.get(subUsageTypeId).getTenantId())
					&& subUsageTypeMap.get(subUsageTypeId).getTenantId().equals(tenantId)) {
				return subUsageTypeMap.get(subUsageTypeId).getName();
			} else {
				return null;
			}
		}
	}

	public static ConcurrentHashMap<String, CommonResponseInfo> fetchSubUsageTypeByIdFromMasters(String id,
			String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append(subUsageTypeUrl + "?tenantId=" + tenantId + "&ids=" + id + "&isSubUsageType=true");
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
		if (null != response) {
			if (null != response.getUsageTypes() && response.getUsageTypes().size() > 0) {
				for (CommonResponseInfo usage : response.getUsageTypes()) {
					subUsageTypeMap.put(usage.getId(), usage);
				}
			}
		}
		return null;
	}

}
