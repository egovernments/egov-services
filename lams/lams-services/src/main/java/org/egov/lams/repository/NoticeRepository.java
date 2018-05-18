package org.egov.lams.repository;

import org.egov.lams.model.Notice;
import org.egov.lams.model.NoticeCriteria;
import org.egov.lams.repository.builder.NoticeQueryBuilder;
import org.egov.lams.repository.rowmapper.NoticeRowMapper;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				notice.getTenantId(), notice.getFileStore() ,notice.getNoticeType()};

        try {
            jdbcTemplate.update(NoticeQueryBuilder.INSERT_NOTICE_QUERY, obj);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return notice;
    }

    public List<Notice> getNotices(NoticeCriteria noticeCriteria) {

		List<Notice> notices = null;
		Map params = new HashMap<>();
		String queryString = NoticeQueryBuilder.getNoticeQuery(noticeCriteria, params);
		try {
			notices = namedParameterJdbcTemplate.query(queryString, params, new NoticeRowMapper());
		} catch (DataAccessException e) {
			LOGGER.info("the exception from notice repo query :: " + e);
			throw new RuntimeException(e.getMessage());
		}
		return notices;
	}

}
