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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceJdbcRepository extends JdbcRepository {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Pagination<VehicleMaintenance> search(VehicleMaintenanceSearch searchRequest) {

		String searchQuery = "select * from egswm_vehiclemaintenance :condition  :orderby ";

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

		if (searchRequest.getVehicleNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vehicleNo in (:vehicleNo)");
			paramValues.put("vehicleNo", searchRequest.getVehicleNo());
		}

		if (searchRequest.getCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code in (:code)");
			paramValues.put("code", searchRequest.getCode());
		}

		if (searchRequest.getCodes() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("code in(:codes) ");
			paramValues.put("codes", new ArrayList<String>(Arrays.asList(searchRequest.getCodes().split(","))));
		}

		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getDowntimeforMaintenance() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("downtimeforMaintenance =:downtimeforMaintenance");
			paramValues.put("downtimeforMaintenance", searchRequest.getDowntimeforMaintenance());
		}

		if (searchRequest.getMaintenanceAfter() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
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