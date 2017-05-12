package org.egov.lams.repository.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.service.AgreementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AllotteeHelper {

	public static final Logger logger = LoggerFactory.getLogger(AllotteeHelper.class);

	public String getAllotteeParams(AgreementCriteria searchAllottee) {

		if (searchAllottee.getAllottee() == null && searchAllottee.getMobileNumber() == null)
			throw new RuntimeException("all asset search fields are null");

		boolean isAppendAndClause = false;
		StringBuilder allotteeParams = new StringBuilder();

		if (searchAllottee.getAllottee() != null) {
			allotteeParams.append("id=" + getIdParams(searchAllottee.getAllottee()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, allotteeParams);
		}

		if (searchAllottee.getMobileNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, allotteeParams);
			allotteeParams.append("mobileNumber=" + searchAllottee.getMobileNumber());
		}
		return allotteeParams.toString();
	}

	public Set<Long> getAllotteeIdList(List<Allottee> allotteeList) {

		Set<Long> idSet = new HashSet<>();
		idSet.addAll(allotteeList.stream().map(allottee -> allottee.getId()).collect(Collectors.toList()));
		return idSet;
	}

	public Set<Long> getAllotteeIdListByAgreements(List<Agreement> agreementList) {

		Set<Long> allotteeIdList = new HashSet<>();
		allotteeIdList.addAll(
				agreementList.stream().map(agreement -> agreement.getAllottee().getId()).collect(Collectors.toList()));
		return allotteeIdList;
	}

	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append("&");
		}
		return true;
	}

	private String getIdParams(Set<Long> idList) {
		Long[] list = idList.toArray(new Long[idList.size()]);
		StringBuilder query = new StringBuilder(Long.toString(list[0]));
		for (int i = 1; i < list.length; i++) {
			query.append("," + list[i]);
		}
		return query.toString();
	}
}
