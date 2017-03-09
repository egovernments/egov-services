package org.egov.lams.repository.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.springframework.stereotype.Component;
@Component
public class AllotteeHelper {

	public String getAllotteeUrl(AgreementCriteria searchAllottee) {

		if (searchAllottee.getAllottee() == null && searchAllottee.getMobilenumber() == null)
			throw new RuntimeException("all asset search fields are null");

		boolean isAppendAndClause = false;
		StringBuilder allotteeParams = new StringBuilder();

		if (searchAllottee.getAllottee() != null) {
			allotteeParams.append("allotteeId=" + getIdParams(searchAllottee.getAllottee()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, allotteeParams);
		}

		if (searchAllottee.getMobilenumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, allotteeParams);
			allotteeParams.append("mobileNumber=" + searchAllottee.getMobilenumber());
		}
		return allotteeParams.toString();
	}

	public List<Long> getAllotteeIdList(List<Allottee> allotteeList) {
		return allotteeList.stream().map(allottee -> allottee.getId()).collect(Collectors.toList());
	}

	public List<Long> getAllotteeIdListByAgreements(List<Agreement> agreementList) {
		return agreementList.stream().map(agreement -> agreement.getAllottee().getId()).collect(Collectors.toList());
	}

	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append(" &");
		}
		return true;
	}

	private String getIdParams(List<Long> idList) {
		StringBuilder query = new StringBuilder(Long.toString(idList.get(0)));
		for (int i = 1; i < idList.size(); i++) {
			query.append("," + idList.get(i));
		}
		return query.toString();
	}
}
