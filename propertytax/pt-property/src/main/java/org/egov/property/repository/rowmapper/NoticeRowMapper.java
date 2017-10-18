package org.egov.property.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import org.egov.enums.NoticeType;
import org.egov.models.AuditDetails;
import org.egov.models.Notice;
import org.egov.property.utility.TimeStampUtil;
import org.springframework.jdbc.core.RowMapper;

public class NoticeRowMapper implements RowMapper<Notice> {
	@Override
	public Notice mapRow(ResultSet resultSet, int rowNumber) throws SQLException {

		AuditDetails auditDetails = AuditDetails.builder().createdTime(resultSet.getLong("createdtime"))
				.createdBy(resultSet.getString("createdby")).lastModifiedBy(resultSet.getString("lastmodifiedby"))
				.lastModifiedTime(resultSet.getLong("lastmodifiedtime")).build();

		try {
			return Notice.builder().fileStoreId(resultSet.getString("fileStoreId"))
					.applicationNo(resultSet.getString("applicationnumber"))
					.noticeDate(TimeStampUtil.getDateFormat(resultSet.getString("noticedate")))
					.noticeNumber(resultSet.getString("noticenumber"))
					.noticeType(NoticeType.valueOf(resultSet.getString("noticetype")))
					.tenantId(resultSet.getString("tenantid")).upicNumber(resultSet.getString("upicnumber"))
					.auditDetails(auditDetails).build();
		} catch (ParseException e) {

		}
		return Notice.builder().build();
	}
}
