/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.repository.builder;

import java.util.Map;

import org.egov.wcms.web.contract.TreatmentPlantGetRequest;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TreatmentPlantQueryBuilder {

	private static final String BASE_QUERY = "SELECT treatmentplant.id as treatmentplant_id,treatmentplant.code as treatmentplant_code, treatmentplant.name as treatmentplant_name,"
			+ "treatmentplant.planttype as treatmentplant_planttype,treatmentplant.location as treatmentplant_location, "
			+ " treatmentplant.capacity as treatmentplant_capacity ,"
			+ "treatmentplant.storagereservoirid as treatmentplant_storagereservoirId,treatmentplant.description as treatmentplant_description ,"
			+ "treatmentplant.tenantId as treatmentplant_tenantId FROM egwtr_treatment_plant treatmentplant ";

	public String getQuery(final TreatmentPlantGetRequest treatmentPlantGetRequest,
			@SuppressWarnings("rawtypes") final Map<String, Object> preparedStatementValues) {
		final StringBuilder selectQuery = new StringBuilder(BASE_QUERY);
		addWhereClause(selectQuery, preparedStatementValues, treatmentPlantGetRequest);
		addOrderByClause(selectQuery, treatmentPlantGetRequest);
		log.debug("Query : " + selectQuery);
		return selectQuery.toString();
	}

	@SuppressWarnings("unchecked")
	private void addWhereClause(final StringBuilder selectQuery, final Map<String, Object> preparedStatementValues,
			final TreatmentPlantGetRequest treatmentPlantGetRequest) {

		if (treatmentPlantGetRequest.getIds() == null && treatmentPlantGetRequest.getName() == null
				&& treatmentPlantGetRequest.getCode() == null
				&& treatmentPlantGetRequest.getStorageReservoirId() == null
				&& treatmentPlantGetRequest.getPlantType() == null && treatmentPlantGetRequest.getLocation() == null
				&& treatmentPlantGetRequest.getCapacity() == 0 && treatmentPlantGetRequest.getTenantId() == null)
			return;

		selectQuery.append(" WHERE");
		boolean isAppendAndClause = false;

		if (treatmentPlantGetRequest.getTenantId() != null) {
			isAppendAndClause = true;
			selectQuery.append(" treatmentplant.tenantId = :tenantId");
			preparedStatementValues.put("tenantId", treatmentPlantGetRequest.getTenantId());
		}

		if (treatmentPlantGetRequest.getIds() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" treatmentplant.id IN (:ids)");
			preparedStatementValues.put("ids", treatmentPlantGetRequest.getIds());
		}

		if (treatmentPlantGetRequest.getName() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" treatmentplant.name = :name");
			preparedStatementValues.put("name", treatmentPlantGetRequest.getName());
		}
		if (treatmentPlantGetRequest.getStorageReservoirId() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" treatmentplant.storagereservoirid = :storagereservoirid");
			preparedStatementValues.put("storagereservoirid", treatmentPlantGetRequest.getStorageReservoirId());
		}

		if (treatmentPlantGetRequest.getCode() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" treatmentplant.code = :code");
			preparedStatementValues.put("code", treatmentPlantGetRequest.getCode());
		}

		if (treatmentPlantGetRequest.getPlantType() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" treatmentplant.planttype = :planttype");
			preparedStatementValues.put("planttype", treatmentPlantGetRequest.getPlantType());
		}

		if (treatmentPlantGetRequest.getLocation() != null) {
			isAppendAndClause = addAndClauseIfRequired(isAppendAndClause, selectQuery);
			selectQuery.append(" treatmentplant.location = :location");
			preparedStatementValues.put("location", treatmentPlantGetRequest.getLocation());
		}

	}

	private void addOrderByClause(final StringBuilder selectQuery,
			final TreatmentPlantGetRequest treatmentPlantGetRequest) {
		final String sortBy = treatmentPlantGetRequest.getSortBy() == null ? "treatmentplant.id"
				: "treatmentplant." + treatmentPlantGetRequest.getSortBy();
		final String sortOrder = treatmentPlantGetRequest.getSortOrder() == null ? "DESC"
				: treatmentPlantGetRequest.getSortOrder();
		selectQuery.append(" ORDER BY " + sortBy + " " + sortOrder);
	}

	private boolean addAndClauseIfRequired(final boolean appendAndClauseFlag, final StringBuilder queryString) {
		if (appendAndClauseFlag)
			queryString.append(" AND");

		return true;
	}

	public static String insertTreatmentPlantQuery() {
		return "INSERT INTO egwtr_treatment_plant(id,code,name,planttype,location,capacity,storagereservoirid,"
				+ "description,createdby,lastmodifiedby,createddate,lastmodifieddate,tenantid) values "
				+ "(:id,:code,:name,:planttype,:location,:capacity,:storagereservoirid,:description,:createdby,"
				+ ":lastmodifiedby,:createddate,:lastmodifieddate,:tenantid)";
	}

	public static String updateTreatmentPlantQuery() {
		return "UPDATE egwtr_treatment_plant SET name = :name,planttype = :planttype,location = :location "
				+ " , capacity = :capacity,storagereservoirid = :storagereservoirid,description = :description,"
				+ "lastmodifiedby = :lastmodifiedby,lastmodifieddate = :lastmodifieddate where code = :code  and tenantid = :tenantid";
	}

	public static String selectTreatmentPlantByNameByCodeQuery() {
		return " select code FROM egwtr_treatment_plant where name = :name and tenantId = :tenantId";
	}

	public static String selectTreatmentPlantByNameByCodeNotInQuery() {
		return " select code from egwtr_treatment_plant where name = :name and tenantId = :tenantId and code != :code ";
	}

	public static String getStorageReservoirIdQuery() {
		return " select id FROM egwtr_storage_reservoir where name= :name and tenantId = :tenantId ";
	}

	public static String getStorageReservoirName() {
		return "SELECT name FROM egwtr_storage_reservoir WHERE id = :id and tenantId = :tenantId ";
	}
}
