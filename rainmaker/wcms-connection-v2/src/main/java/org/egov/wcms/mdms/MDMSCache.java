package org.egov.wcms.mdms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.wcms.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Component
@Slf4j
public class MDMSCache {
		
	public static Map<String, List<Map<String, Object>>> mastersMap = new HashMap<>();
	
	@Autowired
	private Repository repository;
	
	@Autowired
	private MDMSUtils mdmsUtils;
	
	public void buildCache(String tenantId, RequestInfo requestInfo) {
		log.info("Building MDMSCache..........");
		StringBuilder uri = new StringBuilder();
		MdmsCriteriaReq request = mdmsUtils.prepareMDMSReqForCache(uri, tenantId, requestInfo);
		String[] masters = {MDMSConstants.APPLICATIONTYPE_MASTER_NAME, MDMSConstants.BILLINGSLAB_MASTER_NAME, MDMSConstants.BILLINGTYPE_MASTER_NAME,
		           MDMSConstants.DOCUMENTTYPE_MASTER_NAME, MDMSConstants.PIPESIZE_MASTER_NAME};
		try {
			Object mdmsResponse = repository.fetchResult(uri, request);
			if(null != mdmsResponse) {
				for(String master: Arrays.asList(masters)) {
					List<Object> masterDataList = new ArrayList<>();
					try{
						masterDataList = JsonPath.read(mdmsResponse, MDMSConstants.WCMS_MDMS_RES_JSONPATH + master);
					}catch(Exception e) {
						log.error(master+" master couldn't be fetched");
						continue;
					}
					List<Map<String, Object>> masterData = masterDataList.parallelStream()
							.map(obj -> {
								return (Map<String, Object>) obj;
							}).collect(Collectors.toList());
					mastersMap.put(master, masterData);
				}
			}
			log.debug("Cached Data: "+mastersMap);
		}catch(Exception e) {
			log.error("Failed to build MDMS Cache: "+e);
		}		
	}
}
