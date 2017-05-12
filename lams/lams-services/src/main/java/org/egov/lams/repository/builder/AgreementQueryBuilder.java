package org.egov.lams.repository.builder;

import java.util.List;
import java.util.Set;

import org.egov.lams.model.AgreementCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgreementQueryBuilder {

	public static final Logger logger = LoggerFactory.getLogger(AgreementQueryBuilder.class);

	public static final String rentIncrementTypeQuery = "SELECT * FROM eglams_rentincrementtype rent WHERE rent.id=?";

	public static final String agreementQuery = "SELECT * FROM eglams_agreement agreement WHERE "
			+ "agreement.acknowledgementnumber=? OR agreement.agreement_no=?";

	public static String agreementSearchQuery(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder(
				"SELECT *,agreement.id as lamsagreementid FROM eglams_agreement agreement "
						+ "LEFT OUTER JOIN eglams_demand demand ON agreement.id=demand.agreementid");

		if (!(agreementsModel.getAgreementId() == null && agreementsModel.getAgreementNumber() == null
				&& (agreementsModel.getFromDate() == null && agreementsModel.getToDate() == null)
				&& agreementsModel.getStatus() == null && agreementsModel.getTenderNumber() == null
				&& agreementsModel.getTinNumber() == null && agreementsModel.getTradelicenseNumber() == null
				&& agreementsModel.getAllottee() == null && agreementsModel.getAsset() == null
				&& agreementsModel.getTenantId() == null && agreementsModel.getAcknowledgementNumber() == null
				&& agreementsModel.getStateId() == null)) {
			
			appendParams(agreementsModel, preparedStatementValues, selectQuery);
		}
		appendLimitAndOffset(agreementsModel, preparedStatementValues, selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings("unchecked")
	private static void appendParams(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues, StringBuilder selectQuery) {

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (agreementsModel.getAgreementId() != null) {
			selectQuery.append(" AGREEMENT.ID IN (" + getIdQuery(agreementsModel.getAgreementId()));
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
		}

		if (agreementsModel.getAgreementNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.AGREEMENT_NO=?");
			preparedStatementValues.add(agreementsModel.getAgreementNumber());
		}

		if (agreementsModel.getStatus() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.STATUS=?");
			preparedStatementValues.add(agreementsModel.getStatus().toString());
		}

		if (agreementsModel.getTenantId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TENANT_ID=?");
			preparedStatementValues.add(agreementsModel.getTenantId());
		}

		if (agreementsModel.getTenderNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TENDER_NUMBER=?");
			preparedStatementValues.add(agreementsModel.getTenderNumber());
		}

		if (agreementsModel.getTinNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TIN_NUMBER=?");
			preparedStatementValues.add(agreementsModel.getTinNumber());
		}

		if (agreementsModel.getTradelicenseNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.TRADE_LICENSE_NUMBER=?");
			preparedStatementValues.add(agreementsModel.getTradelicenseNumber());
		}

		if (agreementsModel.getAcknowledgementNumber() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.acknowledgementnumber=?");
			preparedStatementValues.add(agreementsModel.getAcknowledgementNumber());
		}

		if (agreementsModel.getStateId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.stateid=?");
			preparedStatementValues.add(agreementsModel.getStateId());
		}

		if (agreementsModel.getAsset() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.ASSET IN (" + getIdQuery(agreementsModel.getAsset()));
		}

		if (agreementsModel.getAllottee() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.ALLOTTEE IN (" + getIdQuery(agreementsModel.getAllottee()));
		}

		if (agreementsModel.getFromDate() != null && agreementsModel.getToDate() != null) {

			if (agreementsModel.getToDate().compareTo(agreementsModel.getFromDate()) < 0)
				throw new RuntimeException("ToDate cannot be lesser than fromdate");
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.AGREEMENT_DATE>=?");
			preparedStatementValues.add(agreementsModel.getFromDate());
			addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" AGREEMENT.AGREEMENT_DATE<=?");
			preparedStatementValues.add(agreementsModel.getToDate());
		} else if (agreementsModel.getFromDate() != null || agreementsModel.getToDate() != null)
			throw new RuntimeException("Invalid date Range, please enter Both fromDate and toDate");

	}

	@SuppressWarnings("unchecked")
	private static StringBuilder appendLimitAndOffset(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues, StringBuilder selectQuery) {

		selectQuery.append(" ORDER BY AGREEMENT.ID");
		selectQuery.append(" LIMIT ?");
		if (agreementsModel.getSize() != null)
			preparedStatementValues.add(agreementsModel.getSize());
		else
			preparedStatementValues.add(20);

		selectQuery.append(" OFFSET ?");

		if (agreementsModel.getOffSet() != null)
			preparedStatementValues.add(agreementsModel.getOffSet());
		else
			preparedStatementValues.add(0);
		logger.info("the complete select query for agreement search : " + selectQuery.toString());

		return selectQuery;
	}

	private static boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {

		if (appendAndClauseFlag) {
			queryString.append(" AND");
		}
		return true;
	}

	private static String getIdQuery(Set<Long> idList) {

		StringBuilder query = null;
		if (idList.size() >= 1) {

			Long[] list = idList.toArray(new Long[idList.size()]);
			query = new StringBuilder(list[0].toString());
			for (int i = 1; i < idList.size(); i++) {
				query.append("," + list[i]);
			}
		}
		return query.append(")").toString();
	}
}