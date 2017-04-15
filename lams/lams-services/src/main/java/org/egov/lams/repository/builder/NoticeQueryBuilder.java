package org.egov.lams.repository.builder;

import org.springframework.stereotype.Component;

@Component
public class NoticeQueryBuilder {
	
	public final String INSERT_NOTICE_QUERY = "INSERT INTO eglams_notice"
			+ " (id, noticeno, noticedate, agreementno, assetcategory, acknowledgementnumber, assetno, allotteename,"
			+ " allotteeaddress, allotteemobilenumber, agreementperiod, commencementdate, templateversion, expirydate, rent,"
			+ " securitydeposit, commissionername, zone, ward, street, electionward, locality, block, createdby,"
			+ " createddate, lastmodifiedby ,lastmodifieddate, tenantId)" + " VALUES (nextval('seq_eglams_notice'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public final String SEQ_NOTICE_NO = "SELECT nextval('seq_eglams_noticeno')";
	
	public final String SEQ_NOTICE_ID = "SELECT nextval('seq_eglams_notice')";
			
}
