package org.egov.tradelicense.persistence.repository.helper;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.enums.BusinessNatureEnum;
import org.egov.models.AuditDetails;
import org.egov.models.FeeMatrix;
import org.egov.models.FeeMatrixDetail;
import org.egov.models.RequestInfo;
import org.egov.models.RequestInfoWrapper;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.domain.model.FinancialYearContract;
import org.egov.tradelicense.domain.model.FinancialYearContractResponse;
import org.egov.tradelicense.persistence.repository.builder.FeeMatrixQueryBuilder;
import org.egov.tradelicense.persistence.repository.builder.UtilityBuilder;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FeeMatrixHelper {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	/**
	 * This method will validate the financial year
	 * 
	 * @param financialYear
	 */
	public void validateFinancialYear(String financialYear, RequestInfoWrapper requestInfoWrapper) {

		StringBuffer financialYearURI = new StringBuffer();
		financialYearURI.append(propertiesManager.getFinancialServiceHostName())
				.append(propertiesManager.getFinancialServiceBasePath())
				.append(propertiesManager.getFinancialServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(financialYearURI.toString()).queryParam("id", financialYear)
				.build(true).encode().toUri();
		try {
			FinancialYearContractResponse financialYearContractResponse = restTemplate.postForObject(uri,
					requestInfoWrapper, FinancialYearContractResponse.class);
			
			if (financialYearContractResponse != null) {
				
				List<FinancialYearContract> FinancialYearContracts = financialYearContractResponse.getFinancialYears();
				
				if (FinancialYearContracts == null || FinancialYearContracts.size() == 0) {
					
					throw new InvalidInputException(requestInfoWrapper.getRequestInfo());
				}
				
			} else {
				throw new InvalidInputException(requestInfoWrapper.getRequestInfo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will validate category and sub category
	 * 
	 * @param categoryId
	 * @param requestInfo
	 */
	public void validateCategory(Long categoryId, RequestInfo requestInfo) {

		String tableName = ConstantUtility.CATEGORY_TABLE_NAME;
		String query = UtilityBuilder.getCategoryIdValidationQuery(categoryId, tableName);
		int count = 0;

		try {
			count = (Integer) jdbcTemplate.queryForObject(query, Integer.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		if (count == 0) {
			throw new InvalidInputException(requestInfo);
		}
	}

	/**
	 * This method will validate sequence ranges of a fee matrix details
	 * 
	 * @param FeeMatrix
	 * @param RequestInfo
	 * @param validateNew
	 */
	public void validateFeeMatrixDetailsRange(FeeMatrix feeMatrix, RequestInfo requestInfo, boolean validateNew) {

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();

		if (validateNew) {

			feeMatrixDetails = feeMatrix.getFeeMatrixDetails();
		} else {
			Long feeMatrixId = feeMatrix.getId();
			try {

				feeMatrixDetails = getFeeMatrixDetailsByFeeMatrixId(feeMatrixId);
			} catch (Exception e) {

				throw new InvalidInputException(requestInfo);
			}

			for (FeeMatrixDetail feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {

				for (int i = 0; i < feeMatrixDetails.size(); i++) {
					Long id = feeMatrixDetails.get(i).getId();
					if (feeMatrixDetail.getId() != null && id == feeMatrixDetail.getId()) {
						feeMatrixDetails.set(i, feeMatrixDetail);
					}
				}
			}
		}

		feeMatrixDetails.sort((r1, r2) -> r1.getUomFrom().compareTo(r2.getUomFrom()));
		Long UomFrom = null;
		Long oldUomTo = null;
		int count = 0;

		for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails) {

			UomFrom = feeMatrixDetail.getUomFrom();
			if (count > 0) {
				if (!UomFrom.equals(oldUomTo)) {
					throw new InvalidRangeException(requestInfo);
				}
			}
			oldUomTo = feeMatrixDetail.getUomTo();
			count++;
		}
	}

	/**
	 * Description : this method for search FeeMatrixDetail of a feeMatrix
	 * 
	 * @param feeMatrixId
	 * @return List<FeeMatrixDetail>
	 */
	public List<FeeMatrixDetail> getFeeMatrixDetailsByFeeMatrixId(Long feeMatrixId) {

		List<Object> preparedStatementValues = new ArrayList<>();
		String feeMatrixDetailSearchQuery = FeeMatrixQueryBuilder.buildFeeMatrixDetailSearchQuery(feeMatrixId,
				preparedStatementValues);
		List<FeeMatrixDetail> feeMatrixDetails = getFeeMatrixDetails(feeMatrixDetailSearchQuery.toString(),
				preparedStatementValues);

		return feeMatrixDetails;
	}

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