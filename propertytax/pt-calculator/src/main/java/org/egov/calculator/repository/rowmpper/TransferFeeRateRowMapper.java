package org.egov.calculator.repository.rowmpper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.enums.TransferFeeRatesEnum;
import org.egov.models.AuditDetails;
import org.egov.models.TransferFeeRate;
import org.springframework.stereotype.Repository;

@Repository
public class TransferFeeRateRowMapper {
	
	@SuppressWarnings("rawtypes")
	public List<TransferFeeRate> getMappedTransferFeeRates(List<Map<String, Object>> rows) {
		List<TransferFeeRate> transferFeeRates = new ArrayList<TransferFeeRate>();
		for (Map row : rows) {
			TransferFeeRate transferFeeRate = new TransferFeeRate();
			transferFeeRate.setId(getLong(row.get("id")));
			transferFeeRate.setTenantId(getString(row.get("tenantid")));
			transferFeeRate.setFeeFactor(TransferFeeRatesEnum.fromValue(getString(row.get("feefactor").toString())));
			transferFeeRate.setFromDate(getString(row.get("fromdate")));
			transferFeeRate.setToDate(getString(row.get("todate")));
			transferFeeRate.setFromValue(getDouble(row.get("fromvalue")));
			transferFeeRate.setToValue(getDouble(row.get("tovalue")));
			transferFeeRate.setFeePercentage(getDouble(row.get("tovalue")));
			transferFeeRate.setFlatValue(getDouble(row.get("flatvalue")));			
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong((row.get("createdtime"))));
			auditDetails.setLastModifiedTime(getLong((row.get("lastmodifiedtime"))));
			transferFeeRate.setAuditDetails(auditDetails);
			transferFeeRates.add(transferFeeRate);
		}
		return transferFeeRates;
	}
	
	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? null : object.toString();
	}

	/**
	 * This method will cast the given object to double
	 * 
	 * @param object
	 *            that need to be cast to Double
	 * @return {@link Double}
	 */
	private Double getDouble(Object object) {
		return object == null ? 0.0 : Double.parseDouble(object.toString());
	}

	/**
	 * This method will cast the given object to Long
	 * 
	 * @param object
	 *            that need to be cast to Long
	 * @return {@link Long}
	 */
	private Long getLong(Object object) {
		return object == null ? 0 : Long.parseLong(object.toString());
	}
}
