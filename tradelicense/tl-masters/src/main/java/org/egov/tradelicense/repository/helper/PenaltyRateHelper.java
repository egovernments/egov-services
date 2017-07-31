package org.egov.tradelicense.repository.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.egov.enums.ApplicationTypeEnum;
import org.egov.models.AuditDetails;
import org.egov.models.PenaltyRate;
import org.egov.models.PenaltyRateRequest;
import org.egov.models.RequestInfo;
import org.egov.tradelicense.exception.InvalidInputException;
import org.egov.tradelicense.exception.InvalidRangeException;
import org.egov.tradelicense.repository.PenaltyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PenaltyRateHelper {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	PenaltyRateRepository penaltyRateRepository;

	public void validatePenaltyRange(String tenantId, PenaltyRateRequest penaltyRateRequest, boolean validateNew) {

		RequestInfo requestInfo = penaltyRateRequest.getRequestInfo();
		// Assuming ApplicationType in all PenalityRate objects will be same , considering the first one.
		String applicationType = penaltyRateRequest.getPenaltyRates().get(0).getApplicationType().toString();
		List<PenaltyRate> penaltyRates = new ArrayList<PenaltyRate>();
		if( tenantId == null ){
			tenantId = penaltyRateRequest.getPenaltyRates().get(0).getTenantId();
		}
		try {
			penaltyRates = penaltyRateRepository.searchPenaltyRate(tenantId, null, applicationType, null, null);
		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}
		
		if( validateNew ){
			for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
				penaltyRates.add(penaltyRate);
			}
		}else{
			for (PenaltyRate penaltyRate : penaltyRateRequest.getPenaltyRates()) {
				for (int i = 0; i < penaltyRates.size(); i++) {
					Long id = penaltyRates.get(i).getId();
					if (penaltyRate.getId() != null && id == penaltyRate.getId()) {
						penaltyRates.set(i, penaltyRate);
					}
				}
			}
		}
		
		
		penaltyRates.sort((s1, s2) -> s1.getFromRange().compareTo(s2.getFromRange()));
		String oldApplicationType = null;
		Long oldToRange = null;
		Long fromRange = null;
		int count = 0;
		for (PenaltyRate penaltyRate : penaltyRates) {
			if (count > 0) {
				applicationType = penaltyRate.getApplicationType().toString();
				fromRange = penaltyRate.getFromRange();
				if (applicationType.equalsIgnoreCase(oldApplicationType)) {
					if (!fromRange.equals(oldToRange)) {
						throw new InvalidRangeException(requestInfo);
					}
				} else {
					throw new InvalidInputException(requestInfo);
				}
			}
			oldApplicationType = penaltyRate.getApplicationType().toString();
			oldToRange = penaltyRate.getToRange();
			count++;
		}
	}

	/**
	 * This method will execute the given query & will build the PenaltyRate
	 * object
	 * 
	 * @param query
	 * @return {@link PenaltyRate} List of Category
	 */
	public List<PenaltyRate> getPenaltyRates(String query, List<Object> preparedStatementValues) {

		List<PenaltyRate> penaltyRates = new ArrayList<>();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, preparedStatementValues.toArray());

		for (Map<String, Object> row : rows) {
			PenaltyRate penaltyRate = new PenaltyRate();
			penaltyRate.setId(getLong(row.get("id")));
			penaltyRate.setTenantId(getString(row.get("tenantid")));
			penaltyRate.setApplicationType(ApplicationTypeEnum.fromValue(getString(row.get("applicationTypeId"))));
			penaltyRate.setFromRange(getLong(row.get("fromRange")));
			penaltyRate.setToRange(getLong(row.get("toRange")));
			penaltyRate.setRate(getDouble(row.get("rate")));
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedBy(getString(row.get("createdby")));
			auditDetails.setLastModifiedBy(getString(row.get("lastmodifiedby")));
			auditDetails.setCreatedTime(getLong(row.get("createdtime")));
			auditDetails.setLastModifiedTime(getLong(row.get("lastmodifiedtime")));
			penaltyRate.setAuditDetails(auditDetails);

			penaltyRates.add(penaltyRate);

		}

		return penaltyRates;
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