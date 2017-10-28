package org.egov.lcms.repository.builder;

import java.util.List;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.OpinionSearchCriteria;
import org.egov.lcms.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OpinionQueryBuilder {

	@Autowired
	PropertiesManager propertiesManager;

	public static final String BASE_SEARCH_QUERY = "select * from " + ConstantUtility.OPINION_TABLE_NAME + " WHERE";

	public String getOpinionSearchQuery(final OpinionSearchCriteria opinionSearchCriteria,
			final List<Object> preparedStatementValues) throws Exception {
		final StringBuilder selectQuery = new StringBuilder(BASE_SEARCH_QUERY);

		addWhereClause(selectQuery, preparedStatementValues, opinionSearchCriteria);
		addOrderByClause(selectQuery, preparedStatementValues, opinionSearchCriteria);
		addPagingClause(selectQuery, preparedStatementValues, opinionSearchCriteria);
		log.info("selectQuery::" + selectQuery);
		log.info("preparedstmt values : " + preparedStatementValues);
		return selectQuery.toString();
	}

	private void addWhereClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final OpinionSearchCriteria opinionSearchCriteria) throws Exception {

		if (opinionSearchCriteria.getCodes() == null && opinionSearchCriteria.getTenantId() == null
				&& opinionSearchCriteria.getDepartmentName() == null
				&& opinionSearchCriteria.getOpinionRequestDate() == null
				&& opinionSearchCriteria.getOpinionsBy() == null)
			return;

		selectQuery.append(" tenantId=?");
		preparedStatementValues.add(opinionSearchCriteria.getTenantId());

		String[] codes = opinionSearchCriteria.getCodes();
		if (codes != null && codes.length > 0) {

			String searchIds = "";
			int count = 1;
			for (String code : codes) {

				if (count < codes.length)
					searchIds = "'" + searchIds + code + "',";
				else
					searchIds = "'" + searchIds + code + "'";

				count++;
			}
			selectQuery.append(" AND code IN (" + searchIds + ") ");
		}

		if (opinionSearchCriteria.getOpinionRequestDate() != null) {
			selectQuery.append(" AND opinionRequestDate=?");
			preparedStatementValues.add(opinionSearchCriteria.getOpinionRequestDate());
		}
		if (opinionSearchCriteria.getDepartmentName() != null && !opinionSearchCriteria.getDepartmentName().isEmpty()) {
			selectQuery.append(" AND departmentName=?");
			preparedStatementValues.add(opinionSearchCriteria.getDepartmentName());
		}

		if (opinionSearchCriteria.getOpinionsBy() != null && !opinionSearchCriteria.getOpinionsBy().isEmpty()) {
			selectQuery.append(" AND opinionsby->>'name'=?");
			preparedStatementValues.add(opinionSearchCriteria.getOpinionsBy());
		}
	}

	private void addOrderByClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final OpinionSearchCriteria opinionSearchCriteria) {
		String sort = opinionSearchCriteria.getSort();
		selectQuery.append(" ORDER BY ?");
		if (sort != null && !sort.isEmpty()) {
			preparedStatementValues.add(sort);
		} else {
			preparedStatementValues.add(propertiesManager.getSortCode());
		}
	}

	private void addPagingClause(final StringBuilder selectQuery, final List<Object> preparedStatementValues,
			final OpinionSearchCriteria opinionSearchCriteria) {
		selectQuery.append(" LIMIT ?");
		if (opinionSearchCriteria.getPageSize() != null) {
			preparedStatementValues.add(opinionSearchCriteria.getPageSize());
		} else {
			preparedStatementValues.add(500);
		}

		selectQuery.append(" OFFSET ?");
		if (opinionSearchCriteria.getPageNumber() != null) {
			preparedStatementValues.add(opinionSearchCriteria.getPageNumber());
		} else {
			preparedStatementValues.add(0);
		}
	}
}
