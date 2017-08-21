package org.egov.mr.repository.querybuilder;

import java.util.Set;

import org.egov.mr.web.contract.FeeCriteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FeeQueryBuilder {

	private final String BASE_QUERY ="SELECT * FROM egmr_fee WHERE ";
	
	public final static String CREATE_QUERY="INSERT INTO egmr_fee(id, tenantid, feecriteria, fee, fromdate,"
			+ " todate, createddate,lastmodifieddate, createdby, lastmodifiedby)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	public final static String UPDATE_QUERY="UPDATE egmr_fee SET  feecriteria=?, fee=?, fromdate=?, todate=?,"
			+ "  lastmodifieddate=?, lastmodifiedby=? WHERE id=?, tenantid=?;";
	public String getQuery(FeeCriteria feeCriteria) {
		
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		addWhereClause(selectQuery, feeCriteria);
		log.info("::Query:::::::::"+selectQuery);
		return selectQuery.toString();
	}
	
	private void addWhereClause(StringBuilder selectQuery, FeeCriteria feeCriteria) {

		if (feeCriteria.getTenantId() == null && feeCriteria.getId() == null && feeCriteria.getFromDate() == null && feeCriteria.getToDate() == null
				&& feeCriteria.getFeeCriteria() == null)
			return;

		if (feeCriteria.getTenantId() != null)
			selectQuery.append(" tenantId = '" + feeCriteria.getTenantId() + "'");
		if (feeCriteria.getId() != null)
			selectQuery.append(" AND id IN ( " + getIdQuery(feeCriteria.getId()));
		if (feeCriteria.getFromDate() != null)
			selectQuery.append(" AND fromDate = " + feeCriteria.getFromDate());
		if (feeCriteria.getToDate() != null)
			selectQuery.append(" AND toDate = " + feeCriteria.getToDate());
		if (feeCriteria.getFeeCriteria() != null)
			selectQuery.append(" AND feeCriteria = '" + feeCriteria.getFeeCriteria() + "'");
	}	
	
	private String getIdQuery(Set<String> idList) {

		StringBuilder query = new StringBuilder();
		if (!idList.isEmpty()) {
			String[] list = idList.toArray(new String[idList.size()]);
			query.append("'" + list[0] + "'");
			for (int i = 1; i < idList.size(); i++)
				query.append("," + "'" + list[i] + "'");
		}
		return query.append(")").toString();
	}
}
