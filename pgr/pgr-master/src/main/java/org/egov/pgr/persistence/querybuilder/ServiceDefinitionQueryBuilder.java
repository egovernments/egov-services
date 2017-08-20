package org.egov.pgr.persistence.querybuilder;

import org.egov.pgr.domain.model.ServiceDefinition;
import org.springframework.stereotype.Component;

@Component
public class ServiceDefinitionQueryBuilder {

	public String getInsertQuery() {
		return "INSERT INTO service_definition (code, tenantid, createddate, createdby)"
				+ "VALUES (:code, :tenantid, :createddate, :createdby)";
	}

	public String updateQuery(){
		return "UPDATE service_definition SET code = :code, tenantid = :tenantid, lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate" +
				" WHERE code = :code AND tenantid = :tenantid";
	}

	private StringBuilder addWhereClauseWithAnd(StringBuilder query, String fieldName, String paramName) {
		return query.append(" AND ").append(fieldName).append("= :").append(paramName);
	}

	public String getSearchQuery() {
		return "SELECT * FROM service_definition WHERE code = :serviceCode AND tenantid = :tenantid";
	}

	public String getSubmissionData(ServiceDefinition serviceDefinition) {

		StringBuilder query = new StringBuilder("SELECT * FROM service_definition WHERE tenantid = :tenantid");

		if (!serviceDefinition.isCodeAbsent())
			addWhereClauseWithAnd(query, "upper(code)", "code");

		return query.toString();
	}

	public String buildSearchQuery(ServiceDefinition serviceDefinition) {

		StringBuilder query = new StringBuilder("SELECT * FROM egpgr_complainttype WHERE tenantid = :tenantid");

		if (!serviceDefinition.isCodeAbsent())
			addWhereClauseWithAnd(query, "upper(code)", "code");

		return query.toString();
	}
}
