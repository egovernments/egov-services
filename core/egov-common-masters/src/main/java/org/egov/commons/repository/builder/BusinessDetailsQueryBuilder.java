package org.egov.commons.repository.builder;

import java.util.List;

import org.egov.commons.model.BusinessDetailsCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;

@Component
@EqualsAndHashCode
public class BusinessDetailsQueryBuilder {
	private static final Logger logger = LoggerFactory.getLogger(BusinessDetailsQueryBuilder.class);

	private static final String BASE_QUERY = "Select bd.id as bd_id,bd.name as bd_name,bd.businessurl as bd_url,"
			+ "bd.isenabled as bd_enabled,bd.code as bd_code,bd.businesstype as bd_type,"
			+ "bd.fund as bd_fund,bd.function as bd_function,bd.fundsource as bd_fundsource,"
			+ "bd.functionary as bd_functionary,bd.department as bd_department,bd.callbackforapportioning as bd_callback,"
			+ "bd.vouchercreation as bd_vouc_creation,bd.businesscategory as bd_category,"
			+ "bd.isvoucherapproved as bd_is_Vou_approved,bd.vouchercutoffdate as bd_vou_cutoffdate,"
			+ "bd.ordernumber as bd_ordernumber,bd.tenantid as bd_tenant,bd.createdby as bd_createdby,"
			+ "bd.createddate as bd_createddate,bd.lastmodifiedby as bd_lastmodifiedby,bd.lastmodifieddate"
			+ " as bd_lastmodifieddate,bad.id as bad_id,bad.businessdetails as bd_id,bad.chartofaccount"
			+ " as bad_chartofacc,bad.amount as bad_amount,bad.tenantid as bad_tenant,basd.id  as basd_id,"
			+ "basd.accountdetailtype as basd_detailtype,basd.accountdetailkey as basd_detailkey,basd.amount"
			+ " as basd_amount,basd.businessaccountdetail"
			+ " as basd_id,basd.tenantid as basd_tenant from eg_businesscategory bc FULL JOIN eg_businessdetails"
			+ " bd ON bc.id=bd.businesscategory FULL JOIN"
			+ " eg_business_accountdetails bad ON bd.id=bad.businessdetails"
			+ " FULL JOIN eg_business_subledgerinfo basd ON bad.id=basd.businessaccountdetail";

	@SuppressWarnings("rawtypes")
	public String getQuery(BusinessDetailsCriteria detailsCriteria, List preparedStatementValues) {
		StringBuilder selectQuery = new StringBuilder(BASE_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, detailsCriteria);
		addOrderByClause(selectQuery, detailsCriteria);

		logger.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addWhereClause(StringBuilder selectQuery, List preparedStatementValues,
			BusinessDetailsCriteria criteria) {

		if (criteria.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (criteria.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" bd.tenantId = ?");
			preparedStatementValues.add(criteria.getTenantId());
		}

		if (criteria.getIds() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bd.id IN " + getIdQuery(criteria.getIds()));
		}

		if (criteria.getBusinessCategoryCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bc.code = ?");
			preparedStatementValues.add(criteria.getBusinessCategoryCode());
		}

		if (criteria.getActive() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bd.isenabled = ?");
			preparedStatementValues.add(criteria.getActive());
		}

		if (criteria.getBusinessDetailsCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" bd.code = ?");
			preparedStatementValues.add(criteria.getBusinessDetailsCode());
		}
	}

	private void addOrderByClause(StringBuilder selectQuery, BusinessDetailsCriteria criteria) {
		String sortBy = (criteria.getSortBy() == null ? "bd.name" : "bd." + criteria.getSortBy());
		String sortOrder = (criteria.getSortOrder() == null ? "ASC" : criteria.getSortOrder());
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");
		return true;
	}

	private static String getIdQuery(List<Long> idList) {
		StringBuilder query = new StringBuilder("(");
		if (idList.size() >= 1) {
			query.append(idList.get(0).toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append(", " + idList.get(i));
			}
		}
		return query.append(")").toString();
	}

}
