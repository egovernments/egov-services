package org.egov.infra.indexer.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.IndexerApplicationRunnerImpl;
import org.egov.common.contract.request.RequestInfo;
import org.egov.infra.indexer.bulkindexer.BulkIndexer;
import org.egov.infra.indexer.util.IndexerUtils;
import org.egov.infra.indexer.web.contract.Mapping;
import org.egov.infra.indexer.web.contract.ReindexRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Validator {
	
	@Autowired
	private IndexerApplicationRunnerImpl runner;
	
	@Autowired
	private IndexerUtils indexerUtils;
	
	@Autowired
	private BulkIndexer bulkIndexer;
	
	public void validaterReindexRequest(ReindexRequest reindexRequest) {
		Map<String, String> errorMap = new HashMap<>();
		validateUserRBACProxy(errorMap, reindexRequest.getRequestInfo());
		Map<String, Mapping> mappingsMap = runner.getMappingMaps();
		if(null == mappingsMap.get(reindexRequest.getReindexTopic())) {
			throw new CustomException("EG_INDEXER_MISSING_CONFIG","There is no configuration for this index!");
		}else {
			if(mappingsMap.get(reindexRequest.getReindexTopic()).getIndexes().size() > 1) {
				throw new CustomException("EG_INDEXER_INVALID_REINDEX_CONFIG","Currently more than one reindex configs aren't allowed");
			}
			String uri = indexerUtils.getESSearchURL(reindexRequest);
			Object response = bulkIndexer.getESResponse(uri, null, null);
			if(null == response) {
				throw new CustomException("EG_INDEXER_INAVLID_INDEX","There is no data for this index on elasticsearch!");
			}else {
				Integer total = JsonPath.read(response, "$.hits.total");
				if(total == 0L) {
					throw new CustomException("EG_INDEXER_INAVLID_INDEX","There is no data for this index on elasticsearch!");
				}
			}
		}
	}
	
	/**
	 * Checks the following in regard to role based authentication:
	 * 
	 * 1. Checks if the userInfo of the requester is present in the request
	 * 2. Checks if the details like id, role and tenant are available of the requester
	 * 3. Checks if the requester has a valid role to perform any action on the complaint.
	 * 
	 * NOTE: We go with an assumption that zuul always replaces this info, but sometimes when port forwarding or any such bypass is done,
	 * This information will be missing and application will not function.
	 * 
	 * @param errorMap
	 * @param requestInfo
	 */
	public void validateUserRBACProxy(Map<String, String> errorMap, RequestInfo requestInfo) {
		if (null != requestInfo.getUserInfo()) {
			if (StringUtils.isEmpty(requestInfo.getUserInfo().getUuid())) {
				errorMap.put("EG_INDEXER_MISSING_USERID_CODE", "UUID of the user is missing");
			}
		} else {
			errorMap.put("EG_INDEXER_MISSING_USERINFO_CODE", "UserInfo is missing");
			return;
		}
		if(!CollectionUtils.isEmpty(errorMap.keySet())) {
			throw new CustomException(errorMap);
		}

	}

}
