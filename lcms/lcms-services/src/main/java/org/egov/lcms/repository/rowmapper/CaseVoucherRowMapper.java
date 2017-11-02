package org.egov.lcms.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.egov.lcms.models.CaseVoucher;
import org.egov.lcms.models.VocherType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class CaseVoucherRowMapper implements RowMapper<CaseVoucher> {

	@Override
	public CaseVoucher mapRow(ResultSet rs, int rowNum) throws SQLException {

		CaseVoucher caseVoucher = new CaseVoucher();
		caseVoucher.setCode(rs.getString("code"));
		if (rs.getString("vouchertype") != null) {
			caseVoucher.setVoucherType(VocherType.valueOf(rs.getString("vouchertype")));
		}
		caseVoucher.setVoucherDate(getLong(rs.getLong("voucherdate")));
		caseVoucher.setDetails(getString(rs.getString("details")));
		caseVoucher.setVerificationRemarks(getString(rs.getString("verificationremarks")));
		caseVoucher.setOfficerSignature(getString(rs.getString("officersignature")));
		caseVoucher.setTenantId(getString(rs.getString("tenantid")));

		return caseVoucher;
	}

	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	private Long getLong(Object object) {
		return object == null ? null : Long.parseLong(object.toString());
	}
}
