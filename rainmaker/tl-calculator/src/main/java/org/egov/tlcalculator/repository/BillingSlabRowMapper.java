package org.egov.tlcalculator.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tlcalculator.web.models.AuditDetails;
import org.egov.tlcalculator.web.models.BillingSlab;
import org.egov.tlcalculator.web.models.BillingSlab.LicenseTypeEnum;
import org.egov.tlcalculator.web.models.BillingSlab.TypeEnum;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
public class BillingSlabRowMapper implements ResultSetExtractor<List<BillingSlab>> {

	@Override
	public List<BillingSlab> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, BillingSlab> billingSlabMap = new HashMap<>();
		while (rs.next()) {
			String currentId = rs.getString("id");
			BillingSlab currentBillingSlab = billingSlabMap.get(currentId);
			if (null == currentBillingSlab) {
				AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
						.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
						.lastModifiedTime(rs.getLong("lastmodifiedtime")).build();

				currentBillingSlab = BillingSlab.builder().id(rs.getString("id"))
						.accessoryCategory(rs.getString("accessorycategory"))
						.fromUom(rs.getDouble("fromUom"))
						.licenseType(LicenseTypeEnum.fromValue(rs.getString("licensetype")))
						.rate(rs.getDouble("rate"))
						.structureType(rs.getString("structuretype"))
						.tenantId(rs.getString("tenantid"))
						.uom(rs.getString("uom"))
						.tradeType(rs.getString("tradetype"))
						.toUom(rs.getDouble("toUom"))
						.type(TypeEnum.valueOf(rs.getString("type")))
						.auditDetails(auditDetails).build();

				billingSlabMap.put(currentId, currentBillingSlab);
			}

		}

		return new ArrayList<>(billingSlabMap.values());

	}

}
