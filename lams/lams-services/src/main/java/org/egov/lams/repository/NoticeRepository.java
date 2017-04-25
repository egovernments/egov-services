package org.egov.lams.repository;

import java.util.Date;

import org.egov.lams.model.Notice;
import org.egov.lams.repository.builder.NoticeQueryBuilder;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepository {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Notice createNotice(NoticeRequest noticeRequest) {
		
		RequestInfo requestInfo = noticeRequest.getRequestInfo();
		Notice notice= noticeRequest.getNotice();
		notice.setNoticeNo(getNoticeNo());
		
		Object[] obj = new Object[] { notice.getNoticeNo(), notice.getNoticeDate(),notice.getAgreementNumber(),
				notice.getAssetCategory(), notice.getAcknowledgementNumber(),notice.getAssetNo(), notice.getAllotteeName(), 
				notice.getAllotteeAddress(),notice.getAllotteeMobileNumber(),notice.getAgreementPeriod(),notice.getCommencementDate(),
				notice.getTemplateVersion(),notice.getExpiryDate(),notice.getRent(),notice.getSecurityDeposit(),notice.getCommissionerName(),
				notice.getZone(),notice.getWard(),notice.getStreet(),notice.getElectionward(),notice.getLocality(),notice.getBlock(),
				requestInfo.getRequesterId(), new Date(), requestInfo.getRequesterId(), new Date(),
				notice.getTenantId()};
		
		try{
			jdbcTemplate.update(NoticeQueryBuilder.INSERT_NOTICE_QUERY, obj);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return notice;
	}
	
	//public List<Notice> get
	
	private String getNoticeNo() {
		
		Integer result = jdbcTemplate.queryForObject(NoticeQueryBuilder.SEQ_NOTICE_NO,Integer.class);
		StringBuilder noticeNo=null;
		
		try{
			noticeNo = new StringBuilder(String.format("%09d", result));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return noticeNo.toString();
	}	

}
