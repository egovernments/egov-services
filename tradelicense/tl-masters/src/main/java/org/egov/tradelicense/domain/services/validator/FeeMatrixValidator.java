/**
 * 
 */
package org.egov.tradelicense.domain.services.validator;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.FeeMatrix;
import org.egov.tl.commons.web.contract.FeeMatrixDetail;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.requests.FeeMatrixRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.domain.model.FinancialYearContract;
import org.egov.tradelicense.domain.model.FinancialYearContractResponse;
import org.egov.tradelicense.persistence.repository.builder.FeeMatrixQueryBuilder;
import org.egov.tradelicense.persistence.repository.builder.UtilityBuilder;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author phani
 *
 */
@Component
@Slf4j
public class FeeMatrixValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	public void validateFeeMatrixRequest(FeeMatrixRequest feeMatrixRequest, Boolean isNew) {

		RequestInfo requestInfo = feeMatrixRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);

		String tenantId;

		for (FeeMatrix feeMatrix : feeMatrixRequest.getFeeMatrices()) {

			tenantId = feeMatrix.getTenantId();
			String applicationType = feeMatrix.getApplicationType().toString();
			Long categoryId = feeMatrix.getCategoryId();
			Long subCategoryId = feeMatrix.getSubCategoryId();
			String financialYear = feeMatrix.getFinancialYear();

			Date[] dates = validateFinancialYear(financialYear, requestInfoWrapper);
			feeMatrix.setEffectiveFrom(dates[0].getTime());
			feeMatrix.setEffectiveTo(dates[1].getTime());

			validateCategory(categoryId, requestInfo);

			validateCategory(subCategoryId, requestInfo);

			Boolean isExists = utilityHelper.checkWhetherDuplicateFeeMatrixRecordExits(tenantId, applicationType,
					categoryId, subCategoryId, financialYear, ConstantUtility.FEE_MATRIX_TABLE_NAME,
					(isNew ? null : feeMatrix.getId()));

			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getFeeMatrixCustomMsg(), requestInfo);
			}

			if (!isNew) {

				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
			}

			feeMatrix.setAuditDetails(auditDetails);

			// validating fee matrix details
			validateFeeMatrixDetailsRange(feeMatrix, requestInfo, true);
		}
	}

	/**
	 * This method will validate the financial year
	 * 
	 * @param financialYear
	 */
	public Date[] validateFinancialYear(String financialYear, RequestInfoWrapper requestInfoWrapper) {

		StringBuffer financialYearURI = new StringBuffer();
		financialYearURI.append(propertiesManager.getFinancialServiceHostName())
				.append(propertiesManager.getFinancialServiceBasePath())
				.append(propertiesManager.getFinancialServiceSearchPath());

		URI uri = UriComponentsBuilder.fromUriString(financialYearURI.toString())
				.queryParam("id", Long.valueOf(financialYear)).build(true).encode().toUri();

		FinancialYearContractResponse financialYearContractResponse = null;

		try {

			financialYearContractResponse = restTemplate.postForObject(uri, requestInfoWrapper,
					FinancialYearContractResponse.class);

		} catch (Exception e) {
			throw new InvalidInputException(e.getMessage(), requestInfoWrapper.getRequestInfo());
		}

		if (financialYearContractResponse != null) {

			List<FinancialYearContract> financialYearContracts = financialYearContractResponse.getFinancialYears();

			if (financialYearContracts == null || financialYearContracts.size() == 0) {

				throw new InvalidInputException(propertiesManager.getInvalidFinancialYearMsg(),
						requestInfoWrapper.getRequestInfo());
			} else {
				Date[] dates = { financialYearContracts.get(0).getStartingDate(),
						financialYearContracts.get(0).getEndingDate() };
				return dates;
			}

		} else {
			throw new InvalidInputException(propertiesManager.getInvalidFinancialYearMsg(),
					requestInfoWrapper.getRequestInfo());
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
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			count = (Integer) namedParameterJdbcTemplate.queryForObject(query, parameters, Integer.class);
		} catch (Exception e) {
			log.error("error while executing the query :" + query + " , error message : " + e.getMessage());
		}

		if (count == 0) {
			throw new InvalidInputException(propertiesManager.getInvalidCategoryIdMsg(), requestInfo);
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

				throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
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

		if (feeMatrixDetails.size() > 1) {
			feeMatrixDetails.sort((r1, r2) -> r1.getUomFrom().compareTo(r2.getUomFrom()));
		}

		Long uomFrom = null;
		Long oldUomTo = null;
		int count = 0;

		for (FeeMatrixDetail feeMatrixDetail : feeMatrixDetails) {

			uomFrom = feeMatrixDetail.getUomFrom();
			feeMatrixDetail.setAuditDetails(feeMatrix.getAuditDetails());
			if (count > 0) {
				if (!uomFrom.equals(oldUomTo)) {
					throw new InvalidRangeException(propertiesManager.getInvalidSequenceRangeMsg(), requestInfo);
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

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String feeMatrixDetailSearchQuery = FeeMatrixQueryBuilder.buildFeeMatrixDetailSearchQuery(feeMatrixId,
				parameters);
		List<FeeMatrixDetail> feeMatrixDetails = getFeeMatrixDetails(feeMatrixDetailSearchQuery.toString(), parameters);

		return feeMatrixDetails;
	}

	/**
	 * This method will execute the given query & will build the FeeMatrixDetail
	 * object
	 * 
	 * @param query
	 * @return {@link FeeMatrixDetail} List of FeeMatrixDetail
	 */
	public List<FeeMatrixDetail> getFeeMatrixDetails(String query, MapSqlParameterSource parameters) {

		List<FeeMatrixDetail> feeMatrixDetails = new ArrayList<>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameters);

		for (Map<String, Object> row : rows) {
			FeeMatrixDetail feeMatrixDetail = new FeeMatrixDetail();
			feeMatrixDetail.setId(getLong(row.get("id")));
			feeMatrixDetail.setFeeMatrixId(getLong(row.get("feeMatrixId")));
			feeMatrixDetail.setTenantId(getString(row.get("tenantId")));
			feeMatrixDetail.setUomFrom(getLong(row.get("uomFrom")));
			feeMatrixDetail.setUomTo(getLong(row.get("uomTo")));
			feeMatrixDetail.setAmount(getDouble(row.get("amount")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			feeMatrixDetail.setAuditDetails(auditDetails);

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
	public List<FeeMatrix> getFeeMatrices(String query, MapSqlParameterSource parameters) {

		List<FeeMatrix> feeMatrixes = new ArrayList<>();
		List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(query, parameters);

		for (Map<String, Object> row : rows) {
			FeeMatrix feeMatrix = new FeeMatrix();
			feeMatrix.setId(getLong(row.get("id")));
			feeMatrix.setTenantId(getString(row.get("tenantid")));
			feeMatrix.setApplicationType(ApplicationTypeEnum.fromValue(getString(row.get("applicationType"))));
			feeMatrix.setBusinessNature(BusinessNatureEnum.fromValue(getString(row.get("businessNature"))));
			feeMatrix.setCategoryId(getLong(row.get("categoryId")));
			feeMatrix.setSubCategoryId(getLong(row.get("subCategoryId")));
			feeMatrix.setEffectiveFrom(((Timestamp) row.get("effectiveFrom")).getTime());
			feeMatrix.setEffectiveTo(((Timestamp) row.get("effectiveTo")).getTime());
			feeMatrix.setFinancialYear(getString(row.get("financialYear")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			feeMatrix.setAuditDetails(auditDetails);

			feeMatrixes.add(feeMatrix);
		}

		return feeMatrixes;
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