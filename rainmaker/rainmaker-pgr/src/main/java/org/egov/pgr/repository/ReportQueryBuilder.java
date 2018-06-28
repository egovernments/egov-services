package org.egov.pgr.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.contract.ParamValue;
import org.egov.pgr.contract.ReportRequest;
import org.egov.pgr.utils.ReportConstants;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportQueryBuilder {

	public String getComplaintWiseReportQuery(ReportRequest reportRequest) {
		String query = "SELECT servicecode as complaint_type,          \n"
				+ "       sum(case when tenantId NOTNULL then 1 else 0 end) as total_complaints,\n"
				+ "       sum(case when status IN ('closed','resolved','rejected') then 1 else 0 end) as total_closed_complaints,\n"
				+ "       sum(case when status IN ('open','assigned') then 1 else 0 end) as total_open_complaints,\n"
				+ "       avg(cast(rating as numeric)) as avg_citizen_rating\n" + "  FROM eg_pgr_service $where\n"
				+ "  GROUP BY servicecode";

		query = addWhereClause(query, reportRequest);
		log.info("Complaint Type wise report query: " + query);
		return query;

	}

	public String getAOWiseReportQuery(ReportRequest reportRequest) {
		String query = "SELECT by as ao_name,          \n"
				+ "       (select count(*) from eg_pgr_service $subwhere) as total_complaints_received,\n"
				+ "       sum(case when action = 'assign' then 1 else 0 end) as complaints_assigned,\n"
				+ "       sum(case when action = 'reject' then 1 else 0 end) as complaints_rejected \n"
				+ "  FROM eg_pgr_service INNER JOIN eg_pgr_action ON eg_pgr_service.servicerequestid = eg_pgr_action.businesskey $where\n"
				+ "  GROUP BY by";

		query = addWhereClause(query, reportRequest);
		log.info("AO wise report query: " + query);
		return query;

	}
	
	public String getDepartmentWiseReportQuery(ReportRequest reportRequest) {
		String query = "SELECT servicecode as department_name,          \n"
				+ "       sum(case when tenantId NOTNULL then 1 else 0 end) as total_complaints,\n"
				+ "       sum(case when status IN ('closed','resolved','rejected') then 1 else 0 end) as total_closed_complaints,\n"
				+ "       sum(case when status IN ('open','assigned') then 1 else 0 end) as total_open_complaints,\n"
				+ "       avg(cast(rating as numeric)) as avg_citizen_rating\n" + "  FROM eg_pgr_service $where\n"
				+ "  GROUP BY servicecode";

		query = addWhereClause(query, reportRequest);
		log.info("Department name wise report query: " + query);
		return query;

	}

	public String addWhereClause(String query, ReportRequest reportRequest) {
		List<ParamValue> searchParams = reportRequest.getSearchParams();
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("WHERE eg_pgr_service.tenantid = ").append("'" + reportRequest.getTenantId() + "'");
		if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.AO_REPORT)) {
			queryBuilder.append(" AND (by LIKE '%GRO' OR by LIKE '%Grievance Routing Officer') ");
			query = query.replace("$subwhere", "WHERE tenantid = '"+reportRequest.getTenantId()+"'");
		}
		if (!CollectionUtils.isEmpty(searchParams)) {
			for (ParamValue param : searchParams) {
				if (param.getName().equalsIgnoreCase("fromDate")) {
					if (!StringUtils.isEmpty(param.getInput().toString())) {
						queryBuilder.append(" AND createdtime >= ").append(param.getInput());
					}
				} else if (param.getName().equalsIgnoreCase("toDate")) {
					if (!StringUtils.isEmpty(param.getInput().toString())) {
						queryBuilder.append(" AND createdtime <= ").append(param.getInput());
					}
				}
			}
		}
		query = query.replace("$where", queryBuilder.toString());
		return query;
	}

}
