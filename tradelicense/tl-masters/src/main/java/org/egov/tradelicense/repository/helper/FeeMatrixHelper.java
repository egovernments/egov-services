package org.egov.tradelicense.repository.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.enums.BusinessNatureEnum;
import org.egov.models.AuditDetails;
import org.egov.models.FeeMatrix;
import org.egov.models.FeeMatrixDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeeMatrixHelper {

	@Autowired
	JdbcTemplate jdbcTemplate;

	/**
	 * This method will execute the given query & will build the FeeMatrixDetail
	 * object
	 * 
	 * @param query
	 * @return {@link FeeMatrixDetail} List of FeeMatrixDetail
	 */
	public List<FeeMatrixDetail> getFeeMatrixDetails(String query, List<Object> preparedStatementValues) {

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map<String, Object> row : rows) {
			FeeMatrixDetail feeMatrixDetail = new FeeMatrixDetail();
			feeMatrixDetail.setId(getLong(row.get("id")));
			feeMatrixDetail.setFeeMatrixId(getLong(row.get("feeMatrixId")));
			feeMatrixDetail.setUomFrom(getLong(row.get("uomFrom")));
			feeMatrixDetail.setUomTo(getLong(row.get("uomTo")));
			feeMatrixDetail.setAmount(getDouble(row.get("amount")));

			feeMatrixDetails.add(feeMatrixDetail);
		}

		return feeMatrixDetails;
	}

	/**
	 * This method will execute the given query & will build the FeeMatrix
	 * object
	 * 
	 * @param query
	 * @return {@link FeeMatrix} List of FeeMatrix
	 */
	public List<FeeMatrix> getFeeMatrices(String query, List<Object> preparedStatementValues) {

		List<FeeMatrix> feeMatrices = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map<String, Object> row : rows) {
			FeeMatrix feeMatrix = new FeeMatrix();
			feeMatrix.setId(getLong(row.get("id")));
			feeMatrix.setTenantId(getString(row.get("tenantid")));
			feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue(getString(row.get("applicationType"))));
			feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue(getString(row.get("businessNature"))));
			feeMatrix.setCategoryId(getLong(row.get("categoryId")));
			feeMatrix.setSubCategoryId(getLong(row.get("subCategoryId")));
			feeMatrix.setEffectiveFrom(getString(row.get("effectiveFrom")));
			feeMatrix.setEffectiveTo(getString(row.get("effectiveTo")));
			feeMatrix.setFinancialYear(getString(row.get("financialYear")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			feeMatrix.setAuditDetails(auditDetails);

			feeMatrices.add(feeMatrix);
		}

		return feeMatrices;
	}

	/**
	 * This method will cast the given object to String
	 * 
	 * @param object
	 *            that need to be cast to string
	 * @return {@link String}
	 */
	private String getString(Object object) {
		return object == null ? "" : object.toString();
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