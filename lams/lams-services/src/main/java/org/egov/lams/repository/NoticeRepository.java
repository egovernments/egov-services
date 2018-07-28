package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.lams.model.DefaultersInfo;
import org.egov.lams.model.DueNotice;
import org.egov.lams.model.DueNoticeCriteria;
import org.egov.lams.model.DueSearchCriteria;
import org.egov.lams.model.Notice;
import org.egov.lams.model.NoticeCriteria;
import org.egov.lams.repository.builder.NoticeQueryBuilder;
import org.egov.lams.repository.rowmapper.DueDetailsRowMapper;
import org.egov.lams.repository.rowmapper.DueNoticeRowMapper;
import org.egov.lams.repository.rowmapper.NoticeRowMapper;
import org.egov.lams.web.contract.DueNoticeRequest;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(NoticeRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	public Notice createNotice(NoticeRequest noticeRequest) {

		RequestInfo requestInfo = noticeRequest.getRequestInfo();
		Notice notice = noticeRequest.getNotice();

		Object[] obj = new Object[] { notice.getNoticeNo(), notice.getNoticeDate(), notice.getAgreementNumber(),
				notice.getAssetCategory(), notice.getAcknowledgementNumber(), notice.getAssetNo(),
				notice.getAllotteeName(), notice.getAllotteeAddress(), notice.getAllotteeMobileNumber(),
				notice.getAgreementPeriod(), notice.getCommencementDate(), notice.getTemplateVersion(),
				notice.getExpiryDate(), notice.getRent(), notice.getSecurityDeposit(), notice.getCommissionerName(),
				notice.getZone(), notice.getWard(), notice.getStreet(), notice.getElectionward(), notice.getLocality(),
				notice.getBlock(), requestInfo.getRequesterId(), new Date(), requestInfo.getRequesterId(), new Date(),
				notice.getTenantId(), notice.getFileStore(), notice.getNoticeType() };

		try {
			jdbcTemplate.update(NoticeQueryBuilder.INSERT_NOTICE_QUERY, obj);
		} catch (Exception ex) {
			LOGGER.info("the exception from notice repo query :: " + ex);
			throw new RuntimeException(ex.getMessage());
		}

		return notice;
	}

	public DueNotice createDueNotice(DueNoticeRequest noticeRequest) {
		DueNotice notice = noticeRequest.getDueNotice();
		Map<String, Object> processMap = getDueNoticeParams(notice);

		try {
			namedParameterJdbcTemplate.update(NoticeQueryBuilder.INSERT_DUE_NOTICE_QUERY, processMap);
		} catch (Exception ex) {
			LOGGER.info("exception in saving due notice :: " + ex);
			throw new RuntimeException(ex.getMessage());
		}

		return notice;
	}
	
	public List<DueNotice> getAllDueNoticesByAgreement(DueNoticeCriteria noticeCriteria){
		List<DueNotice> dueNotices = null;
		Map<String,Object> params = new HashMap<>();
		String queryString = NoticeQueryBuilder.getDueNoticesQuery(noticeCriteria, params);
		try {
			dueNotices = namedParameterJdbcTemplate.query(queryString, params, new DueNoticeRowMapper());
		} catch (DataAccessException e) {
			LOGGER.info(" exception while fetching all the due notices:: " + e);
			throw new RuntimeException(e.getMessage());
		}
		return dueNotices;
	}
	public Set<DefaultersInfo> generateDueNoticeDate(DueSearchCriteria dueCriteria) {
		Set<DefaultersInfo> defaulters = new LinkedHashSet<>();
		Map<String, Object> params = new HashMap<>();
		String queryStr = NoticeQueryBuilder.getRentDueSearchQuery(dueCriteria, params);
		try {
			defaulters = namedParameterJdbcTemplate.query(queryStr, params, new DueDetailsRowMapper());
		} catch (Exception ex) {
			LOGGER.info("exception while fetching defaulters :: " + ex);
			throw new RuntimeException(ex.getMessage());
		}

		return defaulters;
	}

	public List<Notice> getNotices(NoticeCriteria noticeCriteria) {

		List<Notice> notices = null;
		Map<String, Object> params = new HashMap<>();
		String queryString = NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params);
		try {
			notices = namedParameterJdbcTemplate.query(queryString, params, new NoticeRowMapper());
		} catch (DataAccessException e) {
			LOGGER.info("the exception from notice repo query :: " + e);
			throw new RuntimeException(e.getMessage());
		}
		return notices;
	}
    
	public List<DefaultersInfo> getDefaulterDetails(DueNotice noticeData) {

		Map<String, Object> params = new HashMap<>();
		Set<DefaultersInfo> defaultersData = null;
		List<DefaultersInfo> defaulterDetails = new ArrayList<>();
		StringBuilder defaultersQueryString = new StringBuilder(NoticeQueryBuilder.RENT_DUE_QUERY);
		if (noticeData.getAgreementNumber() != null) {
			defaultersQueryString.append(" and df.agreementnumber=:agreementNumber");
			params.put("agreementNumber", noticeData.getAgreementNumber());
		}
		if (noticeData.getTenantId() != null) {
			defaultersQueryString.append(" and df.tenantid=:tenantId");
			params.put("tenantId", noticeData.getTenantId());
		}
		try {
			defaultersData = namedParameterJdbcTemplate.query(defaultersQueryString.toString(), params,
					new DueDetailsRowMapper());
		} catch (DataAccessException e) {
			LOGGER.info("the exception fetching data to save duenotice :: " + e);
			throw new RuntimeException(e.getMessage());
		}
		defaulterDetails.addAll(defaultersData);

		return defaulterDetails;

	}
	 private Map<String, Object> getDueNoticeParams(DueNotice notice) {
	        Map<String, Object> inputParameters = new HashMap<>();
     inputParameters.put("noticeNo", notice.getNoticeNo());
     inputParameters.put("noticeDate",new Date());
     inputParameters.put("agreementNumber",notice.getAgreementNumber());
     inputParameters.put("assetCategory",notice.getAssetCategory());
     inputParameters.put("categoryName",notice.getCategoryName());
     inputParameters.put("allotteeName",notice.getAllotteeName());
     inputParameters.put("mobileNumber",notice.getAllotteeMobileNumber());
     inputParameters.put("commencementDate",notice.getCommencementDate());
     inputParameters.put("expiryDate",notice.getExpiryDate());
     inputParameters.put("dueFromDate",new Date());
     inputParameters.put("dueToDate", new Date());
     inputParameters.put("action",notice.getAction());
     inputParameters.put("status",notice.getStatus());
     inputParameters.put("createdDate",new Date());
     inputParameters.put("createdBy",1);
     inputParameters.put("lastmodifiedby",1);
     inputParameters.put("lastmodifiedDate",new Date());
     inputParameters.put("noticeType","DUE");
     inputParameters.put("filestore",notice.getFileStore());
     inputParameters.put("tenantId",notice.getTenantId());
     
         
	        
       return inputParameters;
      }
}