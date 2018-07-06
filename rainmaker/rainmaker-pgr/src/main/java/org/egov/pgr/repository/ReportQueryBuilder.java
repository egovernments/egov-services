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

	public String getCreateViewQuery() {
		String query = "create view slaservicerequestidview as select businesskey,\n"
				+ "case when (max(\"when\") - min(\"when\") > 10) then 'Yes' else 'No' end as has_sla_crossed \n"
				+ "from eg_pgr_action                                                            \n"
				+ "group by businesskey\n" + "";

		log.info("Create view query: " + query);
		return query;

	}

	public String getDropViewQuery() {
		String query = "DROP VIEW slaservicerequestidview";
		return query;

	}

	public String getComplaintWiseReportQuery(ReportRequest reportRequest) {
		String query = "SELECT servicecode as complaint_type,          \n"
				+ "       sum(case when tenantId NOTNULL then 1 else 0 end) as total_complaints,\n"
				//+ "       sum(case when status IN ('closed','resolved','rejected') then 1 else 0 end) as total_closed_complaints,\n"
				+ "       sum(case when status IN ('open','assigned') then 1 else 0 end) as total_open_complaints,\n"
				//+ "       sum(case when has_sla_crossed = 'Yes' then 1 else 0 end) as within_sla,\n"
				+ "       sum(case when has_sla_crossed = 'No' then 1 else 0 end) as outside_sla, \n"
				+ "       avg(cast(rating as numeric)) as avg_citizen_rating\n"
				+ "  from eg_pgr_service INNER JOIN slaservicerequestidview ON servicerequestid = businesskey $where\n"
				+ "  group by servicecode";

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
				//+ "       sum(case when status IN ('closed','resolved','rejected') then 1 else 0 end) as total_closed_complaints,\n"
				+ "       sum(case when status IN ('open','assigned') then 1 else 0 end) as total_open_complaints,\n"
				//+ "       sum(case when has_sla_crossed = 'Yes' then 1 else 0 end) as within_sla,\n"
				+ "       sum(case when has_sla_crossed = 'No' then 1 else 0 end) as outside_sla, \n"
				+ "       avg(cast(rating as numeric)) as avg_citizen_rating\n"
				+ "  from eg_pgr_service INNER JOIN slaservicerequestidview ON servicerequestid = businesskey $where\n"
				+ "  group by servicecode";

		query = addWhereClause(query, reportRequest);
		log.info("Department name wise report query: " + query);
		return query;

	}

	public String getSourceWiseReportQuery(ReportRequest reportRequest) {
		String query = "SELECT sum(case when source = 'mobileapp' then 1 else 0 end) as citizen_mobile_app,\n"
				+ "       sum(case when source = 'web' then 1 else 0 end) as citizen_web_app,\n"
				+ "       sum(case when source = 'ivr' then 1 else 0 end) as customer_service_desk\n" + "FROM eg_pgr_service $where";

		query = addWhereClause(query, reportRequest);
		log.info("Source wise report query: " + query);
		return query;
	}

	public String getFunctionaryWiseReportQuert(ReportRequest reportRequest) {
		String query = "select assignee as employee_name,\n"
				+ "sum(case when eg_pgr_action.businesskey IN (select DISTINCT businesskey from eg_pgr_action where status = 'assigned') then 1 else 0 end) as total_complaints_received,\n"
				//+ "sum(case when eg_pgr_action.when IN (select max(\"when\") from eg_pgr_action where status = 'resolved' group by businessKey) then 1 else 0 end) as total_closed_complaints,\n"
				+ "sum(case when eg_pgr_action.when IN (select max(\"when\") from eg_pgr_action where assignee NOTNULL group by businessKey) then 1 else 0 end) as total_open_complaints,\n"
				//+ "sum(case when has_sla_crossed = 'Yes' then 1 else 0 end) as within_sla,\n"
				+ "sum(case when has_sla_crossed = 'No' then 1 else 0 end) as outside_sla,\n"
				+ "avg(cast(rating as numeric)) as avg_citizen_rating\n"
				+ "from eg_pgr_service INNER JOIN eg_pgr_action ON servicerequestid = eg_pgr_action.businesskey INNER JOIN slaservicerequestidview ON servicerequestid = slaservicerequestidview.businesskey  where eg_pgr_action.businesskey IN (select DISTINCT businesskey from eg_pgr_action where status = 'assigned')\n"
				+ "$where group by assignee";
		
		query = addWhereClause(query, reportRequest);
		log.info("Functionary Wise report query: " + query);
		return query;
	}

	public String addWhereClause(String query, ReportRequest reportRequest) {
		List<ParamValue> searchParams = reportRequest.getSearchParams();
		StringBuilder queryBuilder = new StringBuilder();
		if(reportRequest.getReportName().equalsIgnoreCase(ReportConstants.FUNCTIONARY_REPORT)) {
			queryBuilder.append(" AND eg_pgr_service.tenantid = ").append("'" + reportRequest.getTenantId() + "'");
		}else {
			queryBuilder.append("WHERE eg_pgr_service.tenantid = ").append("'" + reportRequest.getTenantId() + "'");
		}
		if (reportRequest.getReportName().equalsIgnoreCase(ReportConstants.AO_REPORT)) {
			queryBuilder.append(" AND (by LIKE '%GRO' OR by LIKE '%Grievance Routing Officer') ");
			query = query.replace("$subwhere", "WHERE tenantid = '" + reportRequest.getTenantId() + "'");
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
