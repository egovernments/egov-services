package org.egov.mr.repository.querybuilder;

import java.util.List;

import org.egov.mr.web.contract.RegistrationUnitSearchCriteria;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class RegistrationUnitQueryBuilder {

	public final String BASEQUERY = "SELECT * FROM egmr_registration_unit ";
	StringBuilder selectQuery;

	// Query for _search
	public String getSelectQuery(RegistrationUnitSearchCriteria regnUnitSearchCriteria,
			List<Object> preparedStatementValues) {
		selectQuery = new StringBuilder(BASEQUERY);
		addWhereCluase(regnUnitSearchCriteria, preparedStatementValues);
		return selectQuery.toString();
	}

	// Helper method
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
		selectQuery.append("ORDER BY createdTime ASC ");
		selectQuery.append(";");// Should be Added OR Not ORDER BY id ASC
	}

	// Helper method
	private boolean addAndClauseIfRequired(boolean appendAndClauseFlag, StringBuilder queryString) {
		if (appendAndClauseFlag) {
			queryString.append("AND ");
		}
		return true;
	}

	// Query to generate Unique Id
	public String getIdNextValForRegnUnit() {
		return "SELECT NEXTVAL('seq_registartionunit_id') ;";
	}

	// Query for _create
	public String getCreateQuery() {
		return "INSERT INTO egmr_registration_unit " + "(id,code,name,isactive,tenantid,locality,zone,revenueWard,block"
				+ ",street,electionWard,doorNo,pinCode,createdBy,lastModifiedBy,createdTime,lastModifiedTime) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ;";
	}

	// Query for _update
	public String getUpdateQuery() {
		return "UPDATE egmr_registration_unit SET code=?, name=?,isactive=?,locality=?,zone=?,revenueWard=?,block=?"
				+ ",street=?,electionWard=?,doorNo=?,pinCode=?,createdBy=?,lastModifiedBy=?,createdTime=?,lastModifiedTime=?"
				+ "WHERE id=? AND tenantId=? ;";
	}
}