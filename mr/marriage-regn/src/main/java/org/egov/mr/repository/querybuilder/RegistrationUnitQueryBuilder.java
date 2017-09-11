package org.egov.mr.repository.querybuilder;

import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.springframework.stereotype.Component;

@Component
public class RegistrationUnitQueryBuilder {

	public final String BASEQUERY = "SELECT * FROM egmr_registration_unit ";
	StringBuilder selectQuery;

	/**
	 * @Query_for_search
	 * 
	 * @param regnUnitSearchCriteria
	 * @param preparedStatementValues
	 * @return
	 */
	public String getSelectQuery(RegistrationUnitSearchCriteria regnUnitSearchCriteria) {
		selectQuery = new StringBuilder(BASEQUERY);
		addWhereCluase(regnUnitSearchCriteria);
		return selectQuery.toString();
	}

	/**
	 * @Helper_method
	 * @param regnUnitSearchCriteria
	 * @param preparedStatementValues
	 */
	private void addWhereCluase(RegistrationUnitSearchCriteria regnUnitSearchCriteria) {
		if (regnUnitSearchCriteria.getId() == null && regnUnitSearchCriteria.getName() == null
				&& regnUnitSearchCriteria.getLocality() == null && regnUnitSearchCriteria.getZone() == null
				&& regnUnitSearchCriteria.getIsActive() == null && regnUnitSearchCriteria.getTenantId() == null) {
			return;
		}
		selectQuery.append("WHERE ");
		selectQuery.append("tenantId='" + regnUnitSearchCriteria.getTenantId() + "' ");
		if (regnUnitSearchCriteria.getId() != null && regnUnitSearchCriteria.getId() != 0) {
			selectQuery.append("AND id='" + regnUnitSearchCriteria.getId() + "' ");
		}
		if (regnUnitSearchCriteria.getName() != null && regnUnitSearchCriteria.getName() != "") {
			selectQuery.append("AND name='" + regnUnitSearchCriteria.getName() + "' ");
		}
		if (regnUnitSearchCriteria.getLocality() != null && regnUnitSearchCriteria.getLocality() != 0) {
			selectQuery.append("AND locality='" + regnUnitSearchCriteria.getLocality() + "' ");
		}
		if (regnUnitSearchCriteria.getZone() != null && regnUnitSearchCriteria.getZone() != 0) {
			selectQuery.append("AND zone='" + regnUnitSearchCriteria.getZone() + "' ");
		}
		if (regnUnitSearchCriteria.getIsActive() != null) {
			selectQuery.append("AND isActive='" + regnUnitSearchCriteria.getIsActive() + "' ");
		}
		selectQuery.append("ORDER BY createdTime ASC ;");
	}

	/**
	 * @Helper_Methods
	 * @Query_to_generate_Unique_Id
	 * @return
	 */
	public String getIdNextValForRegnUnit() {
		return "SELECT NEXTVAL('seq_registartion_unit') ;";
	}

	/**
	 * @Query_for_create
	 * @return
	 */
	public String getCreateQuery() {
		return "INSERT INTO egmr_registration_unit" + "(id,name,isactive,tenantid,locality,zone,revenueWard,block"
				+ ",street,electionWard,doorNo,pinCode,isMainRegistrationUnit,createdBy,lastModifiedBy,createdTime,lastModifiedTime) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
	}

	/**
	 * @Query_for_update
	 * @return
	 */
	public String getUpdateQuery() {
		return "UPDATE egmr_registration_unit SET name=?,isactive=?,locality=?,zone=?,revenueWard=?,block=?"
				+ ",street=?,electionWard=?,doorNo=?,pinCode=?,isMainRegistrationUnit=?,createdBy=?,lastModifiedBy=?,createdTime=?,lastModifiedTime=?"
				+ "WHERE id=? AND tenantId=? ;";
	}
}