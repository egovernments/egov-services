package org.egov.wcms.mdms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Service
@Slf4j
public class MDMSUtils {
	
	@Value("${egov.mdms.host}")
	private String mdmsHost;
	
	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;
	
	@Autowired
	private MDMSConstants mdmsConstants;
	

	/**
	 * Prepares request and uri for all masters search belonging to WaterCharges.
	 * 
	 * @param uri
	 * @param tenantId
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareMDMSReqForCache(StringBuilder uri, String tenantId, RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = null;
		List<MasterDetail> masterDetails = new ArrayList<>();
		String[] masters = {MDMSConstants.APPLICATIONTYPE_MASTER_NAME, MDMSConstants.BILLINGSLAB_MASTER_NAME, MDMSConstants.BILLINGTYPE_MASTER_NAME,
		           MDMSConstants.DOCUMENTTYPE_MASTER_NAME, MDMSConstants.PIPESIZE_MASTER_NAME};
		List<String> masterList = Arrays.asList(masters);
		for(String master: masterList) {
			masterDetail = org.egov.mdms.model.MasterDetail.builder()
					.name(master).build();
			masterDetails.add(masterDetail);
		}
		ModuleDetail moduleDetail = ModuleDetail.builder()
				.moduleName(MDMSConstants.WCMS_MODULE_NAME).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		return  MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
	}

}