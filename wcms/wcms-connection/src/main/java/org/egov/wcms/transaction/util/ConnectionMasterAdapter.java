package org.egov.wcms.transaction.util;

import java.util.concurrent.ConcurrentHashMap;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.transaction.web.contract.CommonResponseInfo;
import org.egov.wcms.transaction.web.contract.PipeSizeResponseInfo;
import org.egov.wcms.transaction.web.contract.RequestInfoWrapper;
import org.egov.wcms.transaction.web.contract.StorageReservoirResponse;
import org.egov.wcms.transaction.web.contract.SupplyResponseInfo;
import org.egov.wcms.transaction.web.contract.TreatmentPlantResponse;
import org.egov.wcms.transaction.web.contract.UsageTypeResponse;
import org.egov.wcms.transaction.web.contract.WaterSourceResponseInfo;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

public class ConnectionMasterAdapter {
	
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> supplyTypeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> sourceTypeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> pipeSizeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> treatmentPlantMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> storageReservoirMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> usageTypeMap = new ConcurrentHashMap<>();
	public static volatile ConcurrentHashMap<String, CommonResponseInfo> subUsageTypeMap = new ConcurrentHashMap<>();
	
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchAllSupplyTypeFromMasters(String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/supplytype/_search?tenantId=" + tenantId);
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
	public static String getSupplyTypeById(String supplyTypeId, String tenantId, RequestInfo requestInfo) { 
		if(supplyTypeMap.containsKey(supplyTypeId)) { 
			return supplyTypeMap.get(supplyTypeId).getName();
		} else { 
			fetchSupplyTypeByIdFromMasters(supplyTypeId, tenantId, requestInfo) ; 
			if(supplyTypeMap.containsKey(supplyTypeId)) { 
				return supplyTypeMap.get(supplyTypeId).getName();
			} else { 
				return null; 
			}
		}
	}
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchSupplyTypeByIdFromMasters(String id, String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/supplytype/_search?tenantId="+ tenantId + "&id=" + id);
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
	
	
	
	
	
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchAllSourceTypeFromMasters(String tenantId, RequestInfo requestInfo) { 
		StringBuilder url = new StringBuilder();
        url.append("http://wcms-masters:8080/wcms/masters/sourcetype/_search?tenantId=" + tenantId);
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        WaterSourceResponseInfo sourcetype = new RestTemplate().postForObject(url.toString(), request,
                WaterSourceResponseInfo.class);
        if(null != sourcetype) { 
        	if(null != sourcetype.getWaterSourceType() && sourcetype.getWaterSourceType().size() > 0) { 
        		for (CommonResponseInfo sourceType : sourcetype.getWaterSourceType()) {
					sourceTypeMap.put(sourceType.getId(), sourceType);
				}
        	}
        }
        return null;
	}
	public static String getSourceTypeById(String sourceTypeId, String tenantId, RequestInfo requestInfo) { 
		if(sourceTypeMap.containsKey(sourceTypeId)) { 
			return sourceTypeMap.get(sourceTypeId).getName();
		} else { 
			fetchSourceTypeByIdFromMasters(sourceTypeId, tenantId, requestInfo) ; 
			if(sourceTypeMap.containsKey(sourceTypeId)) { 
				return sourceTypeMap.get(sourceTypeId).getName();
			} else { 
				return null; 
			}
		}
	}
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchSourceTypeByIdFromMasters(String id, String tenantId,  RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/sourcetype/_search?tenantId=" + tenantId + "&id=" + id);
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
	
	
	
	
	
	
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchAllPipeSizeFromMasters(String tenantId, RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
        url.append("http://wcms-masters:8080/wcms/masters/pipesize/_search?tenantId=" + tenantId);
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        PipeSizeResponseInfo pipesize = new RestTemplate().postForObject(url.toString(), request, PipeSizeResponseInfo.class);
        if(null != pipesize) { 
        	if(null != pipesize.getPipeSize() && pipesize.getPipeSize().size() > 0) { 
        		for(CommonResponseInfo pipe : pipesize.getPipeSize()) { 
        			pipeSizeMap.put(pipe.getId(), pipe); 
        		}
        	}
        }
        return null;
	}
	public static String getPipeSizeById(String pipeSizeId, String tenantId, RequestInfo requestInfo) { 
		if(pipeSizeMap.containsKey(pipeSizeId)) { 
			return pipeSizeMap.get(pipeSizeId).getCode();
		} else { 
			fetchPipeSizeByIdFromMasters(pipeSizeId, tenantId, requestInfo) ; 
			if(pipeSizeMap.containsKey(pipeSizeId)) { 
				return pipeSizeMap.get(pipeSizeId).getCode();
			} else { 
				return null; 
			}
		}
	}
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchPipeSizeByIdFromMasters(String id, String tenantId,  RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/pipesize/_search?tenantId=" +tenantId + "&id=" + id);
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
	
	
	
	
	
	
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchAllTreatmentPlantsFromMasters(String tenantId, RequestInfo requestInfo) { 
		StringBuilder url = new StringBuilder();
        url.append("http://wcms-masters:8080/wcms/masters/treatmentplant/_search?tenantId=" + tenantId) ;  
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        TreatmentPlantResponse treatmentPlants = new RestTemplate().postForObject(url.toString(), request, TreatmentPlantResponse.class); 
        if(null != treatmentPlants) { 
        	if(null != treatmentPlants.getTreatmentPlants() && treatmentPlants.getTreatmentPlants().size() > 0) { 
        		for(CommonResponseInfo treatmentPlant : treatmentPlants.getTreatmentPlants()) { 
        			treatmentPlantMap.put(treatmentPlant.getId(), treatmentPlant);
        		}
        	}
        }
        return null;
	}
	public static String getTreatmentPlantById(String treatmentPlantId, String tenantId, RequestInfo requestInfo) { 
		if(treatmentPlantMap.containsKey(treatmentPlantId)) { 
			return treatmentPlantMap.get(treatmentPlantId).getName();
		} else { 
			fetchTreatmentPlantByIdFromMasters(treatmentPlantId, tenantId, requestInfo) ; 
			if(treatmentPlantMap.containsKey(treatmentPlantId)) { 
				return treatmentPlantMap.get(treatmentPlantId).getName();
			} else { 
				return null; 
			}
		}
	}
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchTreatmentPlantByIdFromMasters(String id, String tenantId,  RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/treatmentplant/_search?tenantId=" + tenantId + "&id=" + id);
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

	
	
	
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchAllStorageReservoirsFromMasters(String tenantId, RequestInfo requestInfo) { 
		StringBuilder url = new StringBuilder();
        url.append("http://wcms-masters:8080/wcms/masters/storagereservoir/_search?tenantId=" + tenantId) ; 
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        StorageReservoirResponse storageResponse = new RestTemplate().postForObject(url.toString(), request, StorageReservoirResponse.class);
        if(null != storageResponse) { 
        	if(null != storageResponse.getStorageReservoirs() && storageResponse.getStorageReservoirs().size() > 0){
        		for(CommonResponseInfo storage : storageResponse.getStorageReservoirs()) { 
        			storageReservoirMap.put(storage.getId(), storage); 
        		}
        	}
        }
        return null;
	}
	public static String getStorageReservoiById(String storageReservoirId, String tenantId, RequestInfo requestInfo) { 
		if(storageReservoirMap.containsKey(storageReservoirId)) { 
			return storageReservoirMap.get(storageReservoirId).getName();
		} else { 
			fetchStorageReservoirByIdFromMasters(storageReservoirId, tenantId, requestInfo) ; 
			if(storageReservoirMap.containsKey(storageReservoirId)) { 
				return storageReservoirMap.get(storageReservoirId).getName();
			} else { 
				return null; 
			}
		}
	}
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchStorageReservoirByIdFromMasters(String id, String tenantId,  RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/storagereservoir/_search?tenantId=" + tenantId + "&id=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		StorageReservoirResponse storageReservoirs = new RestTemplate().postForObject(url.toString(), request,
				StorageReservoirResponse.class);
		if (null != storageReservoirs) {
			if (null != storageReservoirs.getStorageReservoirs() && storageReservoirs.getStorageReservoirs().size() > 0) {
				for (CommonResponseInfo pipes : storageReservoirs.getStorageReservoirs()) {
					storageReservoirMap.put(pipes.getId(), pipes);
				}
			}
		}
		return null;
	}
	
	
	
	
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchAllUsageTypesFromMasters(String tenantId, RequestInfo requestInfo) { 
		StringBuilder url = new StringBuilder();
        url.append("http://wcms-masters:8080/wcms/masters/usagetype/_search?tenantId=" + tenantId) ;  
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
        if(null != response) { 
        	if(null != response.getUsageTypes() && response.getUsageTypes().size() > 0){
        		for(CommonResponseInfo usage : response.getUsageTypes()) { 
        			usageTypeMap.put(usage.getId(), usage); 
        		}
        	}
        }
        return null;
	}
	public static String getUsageTypeById(String usageTypeId, String tenantId, RequestInfo requestInfo) { 
		if(usageTypeMap.containsKey(usageTypeId)) { 
			return usageTypeMap.get(usageTypeId).getName();
		} else { 
			fetchUsageTypeByIdFromMasters(usageTypeId, tenantId, requestInfo) ; 
			if(usageTypeMap.containsKey(usageTypeId)) { 
				return usageTypeMap.get(usageTypeId).getName();
			} else { 
				return null; 
			}
		}
	}
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchUsageTypeByIdFromMasters(String id, String tenantId,  RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/usagetype/_search?tenantId=" + tenantId + "&ids=" + id);
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
        if(null != response) { 
        	if(null != response.getUsageTypes() && response.getUsageTypes().size() > 0){
        		for(CommonResponseInfo usage : response.getUsageTypes()) { 
        			usageTypeMap.put(usage.getId(), usage); 
        		}
        	}
        }
        return null;
	}
	
	
	
	
	
	
	
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchAllSubUsageTypesFromMasters(String tenantId, RequestInfo requestInfo) { 
		StringBuilder url = new StringBuilder();
        url.append("http://wcms-masters:8080/wcms/masters/usagetype/_search?tenantid=" + tenantId);  
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
        if(null != response) { 
        	if(null != response.getUsageTypes() && response.getUsageTypes().size() > 0){
        		for(CommonResponseInfo usage : response.getUsageTypes()) { 
        			subUsageTypeMap.put(usage.getId(), usage); 
        		}
        	}
        }
        return null;
	}
	public static String getSubUsageTypeById(String subUsageTypeId, String tenantId, RequestInfo requestInfo) { 
		if(subUsageTypeMap.containsKey(subUsageTypeId)) { 
			return subUsageTypeMap.get(subUsageTypeId).getName();
		} else { 
			fetchSubUsageTypeByIdFromMasters(subUsageTypeId, tenantId, requestInfo) ; 
			if(subUsageTypeMap.containsKey(subUsageTypeId)) { 
				return subUsageTypeMap.get(subUsageTypeId).getName();
			} else { 
				return null; 
			}
		}
	}
	public static ConcurrentHashMap<String, CommonResponseInfo> fetchSubUsageTypeByIdFromMasters(String id, String tenantId,  RequestInfo requestInfo) {
		StringBuilder url = new StringBuilder();
		url.append("http://wcms-masters:8080/wcms/masters/usagetype/_search?tenantId=" + tenantId + "&ids=" + id + "&parent=COM");
		RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(requestInfo).build();
		final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
		UsageTypeResponse response = new RestTemplate().postForObject(url.toString(), request, UsageTypeResponse.class);
        if(null != response) { 
        	if(null != response.getUsageTypes() && response.getUsageTypes().size() > 0){
        		for(CommonResponseInfo usage : response.getUsageTypes()) { 
        			subUsageTypeMap.put(usage.getId(), usage); 
        		}
        	}
        }
        return null;
	}
	

}
