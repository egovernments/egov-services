package org.egov.lams.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.egov.lams.model.SubSeqRenewal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class SubSeqRenewalRowMapper implements ResultSetExtractor<List<SubSeqRenewal>> {
	public static final Logger logger = LoggerFactory.getLogger(SubSeqRenewalRowMapper.class);

	@Override
	public List<SubSeqRenewal> extractData(ResultSet rs) throws SQLException, DataAccessException {

		List<SubSeqRenewal> subSeqRenewalList = new ArrayList<>();
		while (rs.next()) {
			SubSeqRenewal subSeqRenewal = new SubSeqRenewal();
			try {
				subSeqRenewal.setAgreementid((Long) rs.getObject("agreementid"));
				subSeqRenewal.setHistoryFromDate(rs.getTimestamp("fromdate"));
				subSeqRenewal.setHistoryToDate(rs.getTimestamp("todate"));
				subSeqRenewal.setYears(rs.getDouble("years"));
				subSeqRenewal.setHistoryRent(rs.getDouble("rent"));
				subSeqRenewal.setResolutionNumber(rs.getString("resolutionno"));
				subSeqRenewal.setResolutionDate(rs.getTimestamp("resolutiondate"));
                subSeqRenewal.setTenantId(rs.getString("tenantid"));
				subSeqRenewalList.add(subSeqRenewal);
			} catch (Exception e) {
				logger.info("exception in subSeqRenewal RowMapper : " + e);
				throw new RuntimeException("error while mapping subSeqRenew from reult set : " + e.getCause());
			}
		}
		return subSeqRenewalList;
	}

}
