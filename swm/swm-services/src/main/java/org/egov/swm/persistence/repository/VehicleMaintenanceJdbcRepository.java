package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleMaintenance;
import org.egov.swm.domain.model.VehicleMaintenanceSearch;
import org.egov.swm.persistence.entity.VehicleMaintenanceEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vehiclemaintenance";

	public Pagination<VehicleMaintenance> search(VehicleMaintenanceSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VehicleMaintenanceSearch.class);
		}

		String orderBy = "order by code";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getRegNumber() != null) {
			addAnd(params);
			params.append("vehicle in (:vehicle)");
			paramValues.put("vehicle", searchRequest.getRegNumber());
		}

		if (searchRequest.getCode() != null) {
			addAnd(params);
			params.append("code in (:code)");
			paramValues.put("code", searchRequest.getCode());
		}

		if (searchRequest.getCodes() != null) {
			addAnd(params);
			params.append("code in(:codes) ");
			paramValues.put("codes", new ArrayList<String>(Arrays.asList(searchRequest.getCodes().split(","))));
		}

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getDowntimeforMaintenance() != null) {
			addAnd(params);
			params.append("downtimeforMaintenance =:downtimeforMaintenance");
			paramValues.put("downtimeforMaintenance", searchRequest.getDowntimeforMaintenance());
		}

		if (searchRequest.getMaintenanceAfter() != null) {
			addAnd(params);
			params.append("maintenanceAfter =:maintenanceAfter");
			paramValues.put("maintenanceAfter", searchRequest.getMaintenanceAfter());
		}

		Pagination<VehicleMaintenance> page = new Pagination<>();
		if (searchRequest.getOffset() != null) {
			page.setOffset(searchRequest.getOffset());
		}
		if (searchRequest.getPageSize() != null) {
			page.setPageSize(searchRequest.getPageSize());
		}

		if (params.length() > 0) {

			searchQuery = searchQuery.replace(":condition", " where " + params.toString());

		} else

			searchQuery = searchQuery.replace(":condition", "");

		searchQuery = searchQuery.replace(":orderby", orderBy);

		page = (Pagination<VehicleMaintenance>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleMaintenanceEntity.class);

		List<VehicleMaintenance> vehicleMaintenanceList = new ArrayList<>();

		List<VehicleMaintenanceEntity> vehicleMaintenanceEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		for (VehicleMaintenanceEntity vehicleMaintenanceEntity : vehicleMaintenanceEntities) {
			vehicleMaintenanceList.add(vehicleMaintenanceEntity.toDomain());
		}

		page.setTotalResults(vehicleMaintenanceList.size());

		page.setPagedData(vehicleMaintenanceList);

		return page;
	}

}