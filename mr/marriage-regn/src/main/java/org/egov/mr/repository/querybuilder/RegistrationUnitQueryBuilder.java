package org.egov.mr.repository.querybuilder;

import java.util.List;

import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class RegistrationUnitQueryBuilder {

	public final String BASEQUERY = "SELECT * FROM egmr_registration_unit ";
	StringBuilder selectQuery;

	/**
	 * @Query for _search
	 * 
	 * @param regnUnitSearchCriteria
	 * @param preparedStatementValues
	 * @return
	 */
	public String getSelectQuery(RegistrationUnitSearchCriteria regnUnitSearchCriteria,
			List<Object> preparedStatementValues) {
		selectQuery = new StringBuilder(BASEQUERY);
		addWhereCluase(regnUnitSearchCriteria, preparedStatementValues);
		return selectQuery.toString();
	}

	/**
	 * @Helper_method
	 * @param regnUnitSearchCriteria
	 * @param preparedStatementValues
	 */
	@SuppressWarnings("unchecked")
	private void addWhereCluase(RegistrationUnitSearchCriteria regnUnitSearchCriteria,
			@SuppressWarnings("rawtypes") List preparedStatementValues) {
		if (regnUnitSearchCriteria.getId() == null && regnUnitSearchCriteria.getName() == null
				&& regnUnitSearchCriteria.getLocality() == null && regnUnitSearchCriteria.getZone() == null
				&& regnUnitSearchCriteria.getIsActive() == null && regnUnitSearchCriteria.getTenantId() == null) {
			return;
		}
		selectQuery.append("WHERE ");
		boolean addappendAndClauseFlag = false;

		if (regnUnitSearchCriteria.getId() != null && regnUnitSearchCriteria.getId() != 0) {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("id=? ");
			preparedStatementValues.add(regnUnitSearchCriteria.getId());
		}
		if (regnUnitSearchCriteria.getName() != null && regnUnitSearchCriteria.getName() != "") {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("name=? ");
			preparedStatementValues.add(regnUnitSearchCriteria.getName());
		}
		if (regnUnitSearchCriteria.getLocality() != null && regnUnitSearchCriteria.getLocality() != 0) {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("locality=? ");
			preparedStatementValues.add(regnUnitSearchCriteria.getLocality());
		}
		if (regnUnitSearchCriteria.getZone() != null && regnUnitSearchCriteria.getZone() != 0) {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("zone=? ");
			preparedStatementValues.add(regnUnitSearchCriteria.getZone());
		}
		if (regnUnitSearchCriteria.getIsActive() != null) {
			addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
			selectQuery.append("isActive=? ");
			preparedStatementValues.add(regnUnitSearchCriteria.getIsActive());
		}
		addappendAndClauseFlag = addAndClauseIfRequired(addappendAndClauseFlag, selectQuery);
		selectQuery.append("tenantId=? ");
		preparedStatementValues.add(regnUnitSearchCriteria.getTenantId());
		selectQuery.append("ORDER BY createdTime ASC ;");
		// Should be Added OR Not ORDER BY id ASC
	}

	/**
	 * @Helper_method
	 * @param appendAndClauseFlag
	 * @param queryString
	 * @return
	 */
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append("AND ");
		}
		return true;
	}

	/**
	 * @Query to generate Unique Id
	 * @return
	 */
	public String getIdNextValForRegnUnit() {
		return "SELECT NEXTVAL('seq_registartion_unit') ;";
	}

	/**
	 * @Query for _create
	 * @return
	 */
	public String getCreateQuery() {
		return "INSERT INTO egmr_registration_unit" + "(id,name,isactive,tenantid,locality,zone,revenueWard,block"
				+ ",street,electionWard,doorNo,pinCode,isMainRegistrationUnit,createdBy,lastModifiedBy,createdTime,lastModifiedTime) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
	}

	/**
	 * @Query for _update
	 * @return
	 */
	public String getUpdateQuery() {
		return "UPDATE egmr_registration_unit SET name=?,isactive=?,locality=?,zone=?,revenueWard=?,block=?"
				+ ",street=?,electionWard=?,doorNo=?,pinCode=?,isMainRegistrationUnit=?,createdBy=?,lastModifiedBy=?,createdTime=?,lastModifiedTime=?"
				+ "WHERE id=? AND tenantId=? ;";
	}
}