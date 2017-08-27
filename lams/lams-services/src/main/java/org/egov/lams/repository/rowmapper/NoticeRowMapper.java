package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lams.model.Notice;
import org.springframework.jdbc.core.RowMapper;

public class NoticeRowMapper implements RowMapper<Notice> {
	
	@Override
	public Notice mapRow(ResultSet rs, int rowNum) throws SQLException {

		Notice notice = new Notice();
		
		notice.setAcknowledgementNumber(rs.getString("AcknowledgementNumber"));
		notice.setAgreementNumber(rs.getString("agreementNumber"));
		notice.setAgreementPeriod(rs.getLong("agreementPeriod"));
		notice.setAllotteeAddress(rs.getString("allotteeAddress"));
		notice.setAllotteeMobileNumber(rs.getString("allotteeMobileNumber"));
		notice.setAllotteeName(rs.getString("allotteeName"));
		notice.setAssetCategory(rs.getLong("assetCategory"));
		notice.setAssetNo(rs.getLong("assetNo"));
		notice.setBlock(rs.getLong("block"));
		notice.setCommencementDate(rs.getDate("commencementDate"));
		notice.setCommissionerName(rs.getString("commissionerName"));
		notice.setDoorNo(rs.getString("doorNo"));
		notice.setElectionward(rs.getLong("electionward"));
		notice.setExpiryDate(rs.getDate("expiryDate"));
		notice.setId(rs.getLong("id"));
		notice.setLocality(rs.getLong("locality"));
		notice.setNoticeDate(rs.getDate("noticeDate"));
		notice.setNoticeNo(rs.getString("noticeNo"));
		notice.setRent(rs.getDouble("rent"));
		notice.setRentInWord(rs.getString("rentInWord"));
		notice.setSecurityDeposit(rs.getDouble("securityDeposit"));
		notice.setSecurityDepositInWord(rs.getString("securityDepositInWord"));
		notice.setStreet(rs.getLong("street"));
		notice.setTemplateVersion(rs.getString("templateVersion"));
		notice.setTenantId(rs.getString("tenantId"));
		notice.setWard(rs.getLong("ward"));
		notice.setZone(rs.getLong("zone"));
		notice.setFileStore(rs.getString("filestore"));
		
		return notice;
	}

}
