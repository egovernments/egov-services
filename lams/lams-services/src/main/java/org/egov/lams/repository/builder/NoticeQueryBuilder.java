package org.egov.lams.repository.builder;

import java.util.Date;
import java.util.Map;

import org.egov.lams.model.DueNoticeCriteria;
import org.egov.lams.model.DueSearchCriteria;
import org.egov.lams.model.NoticeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NoticeQueryBuilder {

    public static final Logger logger = LoggerFactory.getLogger(NoticeQueryBuilder.class);

    public static final String INSERT_NOTICE_QUERY = "INSERT INTO eglams_notice"
            + " (id, noticeno, noticedate, agreementno, assetcategory, acknowledgementnumber, assetno, allotteename,"
            + " allotteeaddress, allotteemobilenumber, agreementperiod, commencementdate, templateversion, expirydate, rent,"
            + " securitydeposit, commissionername, zone, ward, street, electionward, locality, block, createdby,"
			+ " createddate, lastmodifiedby ,lastmodifieddate, tenantId, filestore, noticetype)"
			+ " VALUES (nextval('seq_eglams_notice'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static final String INSERT_DUE_NOTICE_QUERY = "INSERT INTO eglams_duenotice (id,noticeno, noticedate,agreementnumber,assetcode,assetcategory,categoryname,allotteename,mobilenumber,commencementdate,duefromdate,duetodate,expirydate,action,status,createddate,createdby, lastmodifieddate,lastmodifiedby,ward,noticetype,filestore, tenantId)"
			+ " values (nextval('seq_eglams_duenotice'),:noticeNo, :noticeDate,:agreementNumber,:assetCode,:assetCategory,:categoryName,:allotteeName,:mobileNumber,:commencementDate,:dueFromDate,:dueToDate,:expiryDate,:action,:status,:createdDate,:createdBy, :lastmodifiedDate,:lastmodifiedBy ,:ward,:noticeType , :filestore, :tenantId)";
	public final static String SEQ_NOTICE_NO = "SELECT nextval('seq_eglams_noticeno')";

	public final static String SEQ_NOTICE_ID = "SELECT nextval('seq_eglams_notice')";

	public final static String RENTINCREMENTTYPEQUERY = "SELECT * FROM eglams_rentincrementtype rent WHERE rent.id = :rentId";

	public final static String RENT_DUE_QUERY = "select distinct df.agreementnumber,df.id, df.action,df.status,df.paymentcycle,df.rent,df.securitydeposit,df.commencementdate,df.expirydate,df.allotteename,df.allotteemobilenumber,df.assetcode,df.categoryname,df.assetcategory,df.zone,df.ward,df.street,df.electionward,df.locality,df.block, dft.installment,df.lastpaid,(select min(fromdate)  from eglams_defaulters_details where agreementnumber=df.agreementnumber and tenantid=df.tenantid) as fromdate,(select max(todate)  from eglams_defaulters_details where agreementnumber=df.agreementnumber and tenantid=df.tenantid) as todate,(select sum(amount)  from eglams_defaulters_details where agreementnumber=df.agreementnumber and tenantid=df.tenantid) as amount,(select sum(collection) from eglams_defaulters_details where agreementnumber=df.agreementnumber and tenantid=df.tenantid) as collection,"
			+ "(select sum(balance)  from eglams_defaulters_details where agreementnumber=df.agreementnumber and tenantid=df.tenantid) as balance,df.tenantid from eglams_defaulters df inner join eglams_defaulters_details dft on df.agreementnumber=dft.agreementnumber where df.agreementnumber=dft.agreementnumber and df.tenantid=dft.tenantid ";

	@SuppressWarnings("unchecked")
	public static String getNoticeQuery(NoticeCriteria noticeCriteria, @SuppressWarnings("rawtypes") Map params) {

		StringBuilder selectQuery = new StringBuilder("SELECT * FROM eglams_notice notice");

		selectQuery.append(" WHERE notice.id is not null");

		if (noticeCriteria.getId() != null) {
			selectQuery.append("and notice.id IN (:noticeId)");
			params.put("noticeId", noticeCriteria.getId());
		}

		if (noticeCriteria.getAgreementNumber() != null) {
			selectQuery.append(" and notice.agreementno = :agreementNumber");
			params.put("agreementNumber", noticeCriteria.getAgreementNumber());
		}
		if (noticeCriteria.getAcknowledgementNumber() != null) {
			selectQuery.append(" and notice.acknowledgementnumber = :ackNumber");
			params.put("ackNumber", noticeCriteria.getAcknowledgementNumber());
		}
		if (noticeCriteria.getAssetCategory() != null) {
			selectQuery.append(" and notice.assetcategory = :assetCategory");
			params.put("assetCategory", noticeCriteria.getAssetCategory());
		}
		if (noticeCriteria.getNoticeNo() != null) {
			selectQuery.append(" and notice.noticeno = :noticeNo");
			params.put("noticeNo", noticeCriteria.getNoticeNo());
		}

		if (noticeCriteria.getNoticeType() != null) {
			selectQuery.append(" and notice.noticetype = :noticeType");
			params.put("noticeType", noticeCriteria.getNoticeType());
		}

		if (noticeCriteria.getRevenueWard() != null) {
			selectQuery.append(" and notice.ward = :revenueWard");
			params.put("revenueWard", noticeCriteria.getRevenueWard());
		}

		if (noticeCriteria.getAssetNo() != null) {
			selectQuery.append(" and notice.assetno = :assetNo");
			params.put("assetNo", noticeCriteria.getAssetNo());
		}

		if (noticeCriteria.getTenantId() != null) {
			selectQuery.append(" and notice.tenantid = :tenantId");
			params.put("tenantId", noticeCriteria.getTenantId());
		}

		selectQuery.append(" ORDER BY notice.ID");
		selectQuery.append(" LIMIT :pageSize");
		if (noticeCriteria.getSize() != null)
			params.put("pageSize", noticeCriteria.getSize());
		else
			params.put("pageSize", 20);

		selectQuery.append(" OFFSET :pageNumber");

		if (noticeCriteria.getOffset() != null)
			params.put("pageNumber", noticeCriteria.getOffset());
		else
			params.put("pageNumber", 0);

		return selectQuery.toString();
	}
    
	public static String getRentDueSearchQuery(DueSearchCriteria dueCriteria, Map<String, Object> params) {

		StringBuilder selectQuery = new StringBuilder(RENT_DUE_QUERY);
		Date installmentDate = new Date();
		params.put("tenantId", dueCriteria.getTenantId());
		if (dueCriteria.getAgreementNumber() != null) {
			selectQuery.append(" and dft.agreementnumber = :agreementNumber");
			params.put("agreementNumber", dueCriteria.getAgreementNumber());
		}

		if (dueCriteria.getAssetCategory() != null) {
			selectQuery.append(" and df.assetcategory = :assetCategory");
			params.put("assetCategory", dueCriteria.getAssetCategory());
		}
		if (dueCriteria.getAssetCode() != null) {
			selectQuery.append(" and df.assetcode = :assetCode");
			params.put("assetCode", dueCriteria.getAssetCode());
		}
		if (dueCriteria.getRevenueWard() != null) {
			selectQuery.append(" and df.ward = :revenueWard");
			params.put("revenueWard", dueCriteria.getRevenueWard());
		}
		if (dueCriteria.getRevenueBlock() != null) {
			selectQuery.append(" and df.block = :revenueBlock");
			params.put("revenueBlock", dueCriteria.getRevenueBlock());
		}
		if (dueCriteria.getZone() != null) {
			selectQuery.append(" and df.zone = :zone");
			params.put("zone", dueCriteria.getZone());
		}

		if (dueCriteria.getLocality() != null) {
			selectQuery.append(" and df.locality = :locality");
			params.put("locality", dueCriteria.getLocality());
		}

		selectQuery.append(" and dft.balance >0");
		selectQuery.append(" and dft.fromdate <= :installmentDate");
		selectQuery.append(" and df.tenantid = :tenantId");
		params.put("installmentDate", installmentDate);
		selectQuery.append(" order by balance desc");
		selectQuery.append(" LIMIT 500");
	
		return selectQuery.toString();
	}

	public static String getDueNoticesQuery(DueNoticeCriteria noticeCriteria, Map<String, Object> params) {
		StringBuilder selectQuery = new StringBuilder("SELECT * FROM eglams_duenotice ");

		selectQuery.append(" WHERE id is not null and noticetype='DUE' ");

		if (noticeCriteria.getId() != null) {
			selectQuery.append(" and id IN (:noticeId)");
			params.put("noticeId", noticeCriteria.getId());
		}

		if (noticeCriteria.getAgreementNumber() != null) {
			selectQuery.append(" and agreementnumber = :agreementNumber");
			params.put("agreementNumber", noticeCriteria.getAgreementNumber());
		}

		if (noticeCriteria.getAssetCategory() != null) {
			selectQuery.append(" and assetcategory = :assetCategory");
			params.put("assetCategory", noticeCriteria.getAssetCategory());
		}
		if (noticeCriteria.getNoticeNo() != null) {
			selectQuery.append(" and noticeno = :noticeNo");
			params.put("noticeNo", noticeCriteria.getNoticeNo());
		}

		if (noticeCriteria.getTenantId() != null) {
			selectQuery.append(" and tenantid = :tenantId");
			params.put("tenantId", noticeCriteria.getTenantId());
		}
		selectQuery.append(" and duefromdate <= :dueDate");
		params.put("dueDate", new Date());
		selectQuery.append(" ORDER BY ID desc");
		selectQuery.append(" LIMIT :pageSize");
		if (noticeCriteria.getSize() != null)
			params.put("pageSize", noticeCriteria.getSize());
		else
			params.put("pageSize", 20);

		selectQuery.append(" OFFSET :pageNumber");

		if (noticeCriteria.getOffset() != null)
			params.put("pageNumber", noticeCriteria.getOffset());
		else
			params.put("pageNumber", 0);
		return selectQuery.toString();
	}
}

