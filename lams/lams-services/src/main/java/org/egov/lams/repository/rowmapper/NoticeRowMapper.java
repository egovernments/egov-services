package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class NoticeRowMapper implements ResultSetExtractor<List<Notice>> {
	public static final Logger logger = LoggerFactory.getLogger(NoticeRowMapper.class);

	@Override
	public List<Notice> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<Notice> notices = new ArrayList<>();
		while (rs.next()) {
			Notice notice = new Notice();
			try {
				notice.setId((Long) (rs.getObject("id")));
				notice.setNoticeNo(rs.getString("noticeno"));
				notice.setAgreementNumber(rs.getString("agreementno"));
				notice.setAcknowledgementNumber(rs.getString("acknowledgementnumber"));
				notice.setAllotteeName(rs.getString("allotteename"));
				notice.setAllotteeMobileNumber(rs.getString("allotteemobilenumber"));
				notice.setAllotteeAddress(rs.getString("allotteeaddress"));
				notice.setAssetCategory((Long) rs.getObject("assetcategory"));
				notice.setAssetNo((Long) rs.getObject("assetno"));
				notice.setNoticeType(rs.getString("noticetype"));
				notice.setWard((Long)rs.getObject("ward"));
				notice.setTenantId(rs.getString("tenantid"));
				notice.setFileStore(rs.getString("filestore"));

				notices.add(notice);
			} catch (Exception e) {
				logger.info("exception in notice RowMapper : " + e);
				throw new RuntimeException("error while mapping notices from reult set : " + e.getCause());
			}
		}
		return notices;
	}

}
