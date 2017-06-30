package org.egov.commons.repository.rowmapper;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.egov.commons.model.BusinessCategory;
import org.egov.commons.model.BusinessDetails;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BusinessDetailsRowMapper implements RowMapper<BusinessDetails> {
	final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public BusinessDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

		BusinessCategory businessCategory = new BusinessCategory();
		businessCategory.setId(rs.getLong("id"));

		BusinessDetails businessDetails = new BusinessDetails();
		businessDetails.setId(rs.getLong("id"));
		businessDetails.setBusinessType(rs.getString("businessType"));
		businessDetails.setBusinessUrl(rs.getString("businessUrl"));
		businessDetails.setCode(rs.getString("code"));
		businessDetails.setName(rs.getString("name"));
		businessDetails.setDepartment(rs.getString("department"));
		businessDetails.setFund(rs.getString("fund"));
		businessDetails.setFunction(rs.getString("function"));
		businessDetails.setFunctionary(rs.getString("functionary"));
		businessDetails.setFundSource(rs.getString("fundsource"));
		businessDetails.setIsEnabled((Boolean) rs.getObject("isEnabled"));
		businessDetails.setIsVoucherApproved((Boolean) rs.getObject("isVoucherApproved"));
		businessDetails.setOrdernumber((Integer) rs.getObject("ordernumber"));
		businessDetails.setTenantId(rs.getString("tenantId"));
		businessDetails.setVoucherCreation((Boolean) rs.getObject("voucherCreation"));
		try {
			Date date = isEmpty(rs.getDate("voucherCutOffDate")) ? null
					: sdf.parse(sdf.format(rs.getDate("voucherCutOffDate")));
			businessDetails.setVoucherCutoffDate(date);
			date = isEmpty(rs.getDate("createdDate")) ? null : sdf.parse(sdf.format(rs.getDate("createdDate")));
			businessDetails.setCreatedDate(date);
			date = isEmpty(rs.getDate("lastModifiedDate")) ? null
					: sdf.parse(sdf.format(rs.getDate("lastModifiedDate")));
			businessDetails.setLastModifiedDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SQLException("Parse exception while parsing date");
		}

		businessDetails.setCreatedBy(rs.getLong("createdBy"));
		businessDetails.setLastModifiedBy(rs.getLong("lastModifiedBy"));

		businessDetails.setBusinessCategory(businessCategory);
		return businessDetails;
	}
}
