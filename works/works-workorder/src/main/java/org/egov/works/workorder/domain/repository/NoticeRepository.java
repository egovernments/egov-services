package org.egov.works.workorder.domain.repository;

import java.util.List;

import org.egov.works.workorder.web.contract.Notice;
import org.egov.works.workorder.web.contract.NoticeSearchContract;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepository {

	@Autowired
	private NoticeJdbcRepository noticeJdbcRepository;

	public List<Notice> search(final NoticeSearchContract noticeSearchContract, final RequestInfo requestInfo) {
		return noticeJdbcRepository.searchNotices(noticeSearchContract, requestInfo);
	}

}
