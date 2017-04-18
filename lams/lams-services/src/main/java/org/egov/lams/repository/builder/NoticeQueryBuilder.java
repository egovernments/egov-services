package org.egov.lams.repository.builder;

import java.util.List;
import java.util.Set;

import org.egov.lams.model.AgreementCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NoticeQueryBuilder {
	
	public static final Logger logger = LoggerFactory.getLogger(NoticeQueryBuilder.class);
	
	public final String INSERT_NOTICE_QUERY = "INSERT INTO eglams_notice"
			+ " (id, noticeno, noticedate, agreementno, assetcategory, acknowledgementnumber, assetno, allotteename,"
			+ " allotteeaddress, allotteemobilenumber, agreementperiod, commencementdate, templateversion, expirydate, rent,"
			+ " securitydeposit, commissionername, zone, ward, street, electionward, locality, block, createdby,"
			+ " createddate, lastmodifiedby ,lastmodifieddate, tenantId, rentInWord)" + " VALUES (nextval('seq_eglams_notice'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public final String SEQ_NOTICE_NO = "SELECT nextval('seq_eglams_noticeno')";
	
	public final String SEQ_NOTICE_ID = "SELECT nextval('seq_eglams_notice')";
	

	@SuppressWarnings("unchecked")
	public static String getNoticeQuery(AgreementCriteria agreementsModel,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder("SELECT *,agreement.id as agreementid FROM EGLAMS_AGREEMENT AGREEMENT INNER JOIN eglams_demand demand ON agreement.id=demand.agreementid ");

		if (!(agreementsModel.getAgreementId() == null && agreementsModel.getAgreementNumber() == null
				&& (agreementsModel.getFromDate() == null && agreementsModel.getToDate() == null)
				&& agreementsModel.getStatus() == null && agreementsModel.getTenderNumber() == null
				&& agreementsModel.getTinNumber() == null && agreementsModel.getTradelicenseNumber() == null
				&& agreementsModel.getAllottee() == null && agreementsModel.getAsset() == null
				&& agreementsModel.getTenantId() == null && agreementsModel.getAcknowledgementNumber() == null
				&& agreementsModel.getStateId() == null))
		{
			selectQuery.append(" WHERE");
			boolean isAppendAndClause = false;

			if (agreementsModel.getAgreementId() != null) {
				selectQuery.append(" AGREEMENT.ID IN (" + getIdQuery(agreementsModel.getAgreementId()));
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			}
		}

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

