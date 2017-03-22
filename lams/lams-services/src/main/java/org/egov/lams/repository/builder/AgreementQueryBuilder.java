package org.egov.lams.repository.builder;

import java.util.List;
import java.util.Set;

import org.egov.lams.model.AgreementCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgreementQueryBuilder {

	public static final Logger logger = LoggerFactory.getLogger(AgreementQueryBuilder.class);

	@SuppressWarnings("unchecked")
	public static String agreementQueryBuilder(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder("SELECT * FROM EGLAMS_AGREEMENT AGREEMENT");

		if (!(agreementsModel.getAgreementId() == null && agreementsModel.getAgreementNumber() == null
				&& (agreementsModel.getFromDate() == null && agreementsModel.getToDate() == null)
				&& agreementsModel.getStatus() == null && agreementsModel.getTenderNumber() == null
				&& agreementsModel.getTinNumber() == null && agreementsModel.getTradelicenseNumber() == null
				&& agreementsModel.getAllottee() == null && agreementsModel.getAsset() == null
				&& agreementsModel.getTenantId() == null))
		{
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
			
			if (agreementsModel.getTenantId() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" AGREEMENT.tenantId=?");
				preparedStatementValues.add(agreementsModel.getTenantId());
			}
		}/*else{
			selectQuery.append(" WHERE");
			selectQuery.append(" agreement_date>=?");
			preparedStatementValues.add(new Timestamp(new Date().getTime()));
			System.err.println(new Timestamp(new Date().getTime()));
			Calendar toDate= Calendar.getInstance();
			toDate.setTime(new Date());
			toDate.add(Calendar.YEAR,-1);
			selectQuery.append(" AND agreement_date<=?");
			preparedStatementValues.add(new Timestamp(toDate.getTime().getTime()));
			System.err.println(new Timestamp(toDate.getTime().getTime()));
		}*/

		selectQuery.append(" ORDER BY AGREEMENT.ID");
		selectQuery.append(" LIMIT ?");
		if (agreementsModel.getSize() != null)
			preparedStatementValues.add(Integer.parseInt(agreementsModel.getSize()));
		else
			preparedStatementValues.add(20);

		selectQuery.append(" OFFSET ?");

		if (agreementsModel.getOffSet() != null)
			preparedStatementValues.add(Integer.parseInt(agreementsModel.getOffSet()));
		else
			preparedStatementValues.add(0);
		System.err.println(selectQuery.toString());
		return selectQuery.toString();
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
	
	public static String findRentIncrementTypeQuery() {
		return "SELECT * FROM eglams_rentincrementtype rent WHERE rent.id=?";
	}
}