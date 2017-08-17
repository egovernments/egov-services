package org.egov.wcms.repository.builder;

import java.util.Map;

import org.egov.wcms.web.contract.MeterStatusGetRequest;
import org.springframework.stereotype.Component;

@Component
public class MeterStatusQueryBuilder {

	public static final String BASE_QUERY = "Select ms.id as ms_id,ms.code as ms_code,"
			+ "ms.status as ms_status,ms.description as ms_description,ms.createdby as"
			+ " ms_createdby,ms.createddate as ms_createddate,ms.lastmodifiedby as ms_"
			+ "lastmodifiedby,ms.lastmodifieddate as ms_lastmodifieddate,ms.tenantid as"
			+ " ms_tenantid FROM egwtr_meterstatus ms";

	public String getCreateMeterStatusQuery() {
		return "Insert into egwtr_meterstatus(id,code,status,description,createdby,createddate,"
				+ "lastmodifiedby,lastmodifieddate,tenantId)"
				+ " values(:id,:code,:status,:description,:createdby,:createddate,"
				+ ":lastmodifiedby,:lastmodifieddate,:tenantId)";
	}

	public String getUpdateMeterStatusQuery() {
		return "Update egwtr_meterstatus set status= :status,description= :description,lastmodifiedby="
				+ " :lastmodifiedby,lastmodifieddate= :lastmodifieddate where code =:code and tenantId" + " =:tenantId";

	}

	public String getQuery(MeterStatusGetRequest meterStatusGetRequest, Map<String, Object> preparedStatementValues) {
		StringBuilder queryString = new StringBuilder(BASE_QUERY);
		addWhereClause(meterStatusGetRequest, preparedStatementValues, queryString);
		addOrderByClause(meterStatusGetRequest, queryString);
		return queryString.toString();
	}

	private void addOrderByClause(MeterStatusGetRequest meterStatusGetRequest, StringBuilder queryString) {
		String sortBy = (meterStatusGetRequest.getSortBy() == null ? "ms.id"
				: "ms." + meterStatusGetRequest.getSortBy());
		String sortOrder = (meterStatusGetRequest.getSortOrder() == null ? "DESC"
				: meterStatusGetRequest.getSortOrder());
		queryString.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	private void addWhereClause(MeterStatusGetRequest meterStatusGetRequest,
			Map<String, Object> preparedStatementValues, StringBuilder queryString) {
		if (meterStatusGetRequest.getTenantId() == null)
			return;
		queryString.append(" WHERE");
		boolean isAppendAndClause = false;
		if (meterStatusGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			queryString.append(" ms.tenantid = :tenantId");
			preparedStatementValues.put("tenantId", meterStatusGetRequest.getTenantId());
		}
		if (meterStatusGetRequest.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, queryString);
			queryString.append(" ms.code = :code");
			preparedStatementValues.put("code", meterStatusGetRequest.getCode());
		}
		if (meterStatusGetRequest.getMeterStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, queryString);
			queryString.append(" ms.status = :status");
			preparedStatementValues.put("status", meterStatusGetRequest.getMeterStatus());
		}
		if (meterStatusGetRequest.getIds() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, queryString);
			queryString.append(" ms.id IN (:ids)");
			preparedStatementValues.put("ids", meterStatusGetRequest.getIds());
		}
	}

	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");
		return true;
	}

}
