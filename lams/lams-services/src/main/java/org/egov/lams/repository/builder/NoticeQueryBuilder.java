package org.egov.lams.repository.builder;

import java.util.List;
import java.util.Set;
import org.egov.lams.model.NoticeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NoticeQueryBuilder {
	
	public static final Logger logger = LoggerFactory.getLogger(NoticeQueryBuilder.class);
	
	public static final  String INSERT_NOTICE_QUERY = "INSERT INTO eglams_notice"
			+ " (id, noticeno, noticedate, agreementno, assetcategory, acknowledgementnumber, assetno, allotteename,"
			+ " allotteeaddress, allotteemobilenumber, agreementperiod, commencementdate, templateversion, expirydate, rent,"
			+ " securitydeposit, commissionername, zone, ward, street, electionward, locality, block, createdby,"
			+ " createddate, lastmodifiedby ,lastmodifieddate, tenantId, rentInWord, filestore)" + " VALUES (nextval('seq_eglams_notice'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public final static String SEQ_NOTICE_NO = "SELECT nextval('seq_eglams_noticeno')";

	public final static String SEQ_NOTICE_ID = "SELECT nextval('seq_eglams_notice')";

	public final static String RENTINCREMENTTYPEQUERY = "SELECT * FROM eglams_rentincrementtype rent WHERE rent.id=?";

	@SuppressWarnings("unchecked")
	public static String getNoticeQuery(NoticeCriteria noticeCriteria,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {

		StringBuilder selectQuery = new StringBuilder("SELECT * FROM eglams_notice notice");

		if (!(noticeCriteria.getId() == null && noticeCriteria.getAgreementNumber() == null
				&& (noticeCriteria.getAckNumber() == null && noticeCriteria.getAssetCategory() == null)
				&& noticeCriteria.getNoticeNo() == null &&  noticeCriteria.getTenantId() == null))
		{
			selectQuery.append(" WHERE");
			boolean isAppendAndClause = false;

			if (noticeCriteria.getId() != null) {
				selectQuery.append(" notice.id IN (" + getIdQuery(noticeCriteria.getId()));
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			}
			
			if (noticeCriteria.getAgreementNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" notice.AgreementNumber=?");
				preparedStatementValues.add(noticeCriteria.getAgreementNumber());
			}
			if (noticeCriteria.getAckNumber() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" notice.AckNumber=?");
				preparedStatementValues.add(noticeCriteria.getAckNumber());
			}
			if (noticeCriteria.getAssetCategory() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" notice.AssetCategory=?");
				preparedStatementValues.add(noticeCriteria.getAssetCategory());
			}
			if (noticeCriteria.getNoticeNo() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" notice.NoticeNo=?");
				preparedStatementValues.add(noticeCriteria.getNoticeNo());
			}
			
			if (noticeCriteria.getTenantId() != null) {
				isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
				selectQuery.append(" notice.TenantId=?");
				preparedStatementValues.add(noticeCriteria.getTenantId());
			}
		}

		selectQuery.append(" ORDER BY notice.ID");
		selectQuery.append(" LIMIT ?");
		if (noticeCriteria.getSize() != null)
			preparedStatementValues.add(noticeCriteria.getSize());
		else
			preparedStatementValues.add(20);

		selectQuery.append(" OFFSET ?");

		if (noticeCriteria.getOffset() != null)
			preparedStatementValues.add(noticeCriteria.getOffset());
		else
			preparedStatementValues.add(0);
		logger.info("the select query in notice querybuilder ::"+selectQuery.toString());
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
}

