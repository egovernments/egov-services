package org.egov.pgr.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.contract.ParamValue;
import org.egov.pgr.contract.ReportRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportQueryBuilder {

	public String getComplaintWiseReportQuery(ReportRequest reportRequest) {
		String query = "SELECT servicecode,          \n"
				+ "       sum(case when tenantId NOTNULL then 1 else 0 end) as total_complaints,\n"
				+ "       sum(case when status IN ('closed','resolved','rejected') then 1 else 0 end) as total_closed_complaints,\n"
				+ "       sum(case when status IN ('open','assigned') then 1 else 0 end) as total_open_complaints,\n"
				+ "       avg(cast(rating as numeric)) as avg_citizen_rating\n" + "  FROM eg_pgr_service $where\n"
				+ "  GROUP BY servicecode";

		query = addWhereClause(query, reportRequest);
		log.info("Complaint Type wise report query: " + query);
		return query;

	}

	public String addWhereClause(String query, ReportRequest reportRequest) {
		List<ParamValue> searchParams = reportRequest.getSearchParams();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("WHERE tenantid = ").append("'" + reportRequest.getTenantId() + "'");
		if(!CollectionUtils.isEmpty(searchParams)) {
			for (ParamValue param : searchParams) {
				if (param.getName().equalsIgnoreCase("fromDate")) {
					if (!StringUtils.isEmpty(param.getInput().toString())) {
						queryBuilder.append("AND createdtime >= ").append(param.getInput());
					}
				}else if (param.getName().equalsIgnoreCase("toDate")) {
					if (!StringUtils.isEmpty(param.getInput().toString())) {
						queryBuilder.append("AND createdtime <= ").append(param.getInput());
					}
				}
			}	
		}
		query = query.replace("$where", queryBuilder.toString());
		return query;
	}

}
