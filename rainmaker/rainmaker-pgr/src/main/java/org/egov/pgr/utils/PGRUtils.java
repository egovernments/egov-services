package org.egov.pgr.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pgr.contract.AuditDetails;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class PGRUtils {
	
	@Value("${egov.infra.searcher.host}")
	private String searcherHost;
	
	@Value("${egov.infra.searcher.endpoint}")
	private String searcherEndpoint;
	
	@Value("${egov.mdms.host}")
	private String mdmsHost;
	
	@Value("${egov.mdms.search.endpoint}")
	private String mdmsEndpoint;

	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareSearchRequest(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.SEARCHER_SRSEARCH_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	/**
	 * Prepares request and uri for count search 
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareCountRequest(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.SEARCHER_COUNT_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	
	/**
	 * Prepares request and uri for service code search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareSearchRequestForServiceCodes(StringBuilder uri, String tenantId, 
			String department, RequestInfo requestInfo) {
		
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME).
				filter("[?(@.group=='"+department+"')]."+PGRConstants.SERVICE_CODES).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder()
				.moduleName(PGRConstants.MDMS_PGR_MOD_NAME).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
		
		return mdmsCriteriaReq;
	}
	
	
	public AuditDetails getAuditDetails(String by) {
		
		Long date = new Date().getTime();
		return AuditDetails.builder().createdBy(by).createdTime(date).lastModifiedBy(by).lastModifiedTime(date).build();
	}
	
	/**
	 * Prepares request and uri for service type search from MDMS
	 * 
	 * @param uri
	 * @param tenantId
	 * @param department
	 * @param requestInfo
	 * @return MdmsCriteriaReq
	 * @author vishal
	 */
	public MdmsCriteriaReq prepareSearchRequestForServiceType(StringBuilder uri, String tenantId, String serviceCode, RequestInfo requestInfo) {
		uri.append(mdmsHost).append(mdmsEndpoint);
		MasterDetail masterDetail = org.egov.mdms.model.MasterDetail.builder()
				.name(PGRConstants.MDMS_SERVICETYPE_MASTER_NAME).
				filter("[?(@.serviceCode=='"+serviceCode+"')]."+PGRConstants.SERVICE_NAME).build();
		List<MasterDetail> masterDetails = new ArrayList<>();
		masterDetails.add(masterDetail);
		ModuleDetail moduleDetail = ModuleDetail.builder()
				.moduleName(PGRConstants.MDMS_PGR_MOD_NAME).masterDetails(masterDetails).build();
		List<ModuleDetail> moduleDetails = new ArrayList<>();
		moduleDetails.add(moduleDetail);
		MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
		MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
		
		return mdmsCriteriaReq;
	}
	
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 * @author vishal
	 */
	public SearcherRequest prepareHistoryRequest(StringBuilder uri, ServiceReqSearchCriteria serviceReqSearchCriteria, 
			RequestInfo requestInfo) {
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", PGRConstants.SEARCHER_PGR_MOD_NAME)
				.replace("{searchName}", PGRConstants.SEARCHER_SRHISTORY_DEF_NAME);
		uri.append(endPoint);
		SearcherRequest searcherRequest = SearcherRequest.builder().
				requestInfo(requestInfo).searchCriteria(serviceReqSearchCriteria).build();
		
		return searcherRequest;
	}
	

}
