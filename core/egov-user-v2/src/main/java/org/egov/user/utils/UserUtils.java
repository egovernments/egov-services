package org.egov.user.utils;

import org.egov.common.contract.request.RequestInfo;
import org.egov.user.contract.SearcherRequest;
import org.egov.user.contract.UserSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

	@Autowired
	private UserConfiguration userConf;
	/**
	 * Prepares request and uri for service request search
	 * 
	 * @param uri
	 * @param serviceReqSearchCriteria
	 * @param requestInfo
	 * @return SearcherRequest
	 */
	public SearcherRequest prepareSearchRequestWithDetails(StringBuilder uri, UserSearchCriteria searchCriteria,
			RequestInfo requestInfo, Boolean isUserCitizen) {
		
		String service = null;
		if(isUserCitizen) service = Constants.SEARCHER_DEF_NAME_CITIZEN;
		else service = Constants.SEARCHER_DEF_NAME_EMPLOYEE;
				
		
		uri.append(userConf.getSearcherHost());
		String endPoint = userConf.getSearcherEndpoint().replace(Constants.REPLACE_SEARCH_NAME, service);
		uri.append(endPoint);
		return SearcherRequest.builder().requestInfo(requestInfo).searchCriteria(searchCriteria).build();
	}
}
