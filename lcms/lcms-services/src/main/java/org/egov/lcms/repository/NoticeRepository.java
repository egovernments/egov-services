package org.egov.lcms.repository;

import java.util.ArrayList;
import java.util.List;
import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.models.Notice;
import org.egov.lcms.models.NoticeSearchCriteria;
import org.egov.lcms.repository.builder.NoticeBuilder;
import org.egov.lcms.repository.rowmapper.NoticeRowMapper;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class NoticeRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	NoticeRowMapper noticeRowMapper;

	public List<Notice> search(NoticeSearchCriteria noticeSearchCriteria) {

		if (noticeSearchCriteria.getPageNumber() == null || noticeSearchCriteria.getPageNumber() == 0)
			noticeSearchCriteria.setPageNumber(Integer.valueOf(propertiesManager.getDefaultPageNumber().trim()));

		if (noticeSearchCriteria.getPageSize() == null)
			noticeSearchCriteria.setPageSize(Integer.valueOf(propertiesManager.getDefaultPageSize().trim()));

		final List<Object> preparedStatementValues = new ArrayList<Object>();
		final String selectQuery = NoticeBuilder.getSearchQuery(noticeSearchCriteria, preparedStatementValues);

		List<Notice> notices = new ArrayList<Notice>();
		try {
			notices = jdbcTemplate.query(selectQuery, preparedStatementValues.toArray(), noticeRowMapper);
		} catch (final Exception exception) {
			log.info("the exception in notice search :" + exception);
			throw new CustomException(propertiesManager.getNoticeErrorCode(), propertiesManager.getNoticeErrorMsg());
		}

		return notices;
	}
}
