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
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleMaintenanceJdbcRepository {

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

		if (searchRequest.getMaintenanceAfterDays() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("maintenanceAfterDays =:maintenanceAfterDays");
			paramValues.put("maintenanceAfterDays", searchRequest.getMaintenanceAfterDays());
		}

		if (searchRequest.getMaintenanceAfterKm() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("maintenanceAfterKm =:maintenanceAfterKm");
			paramValues.put("maintenanceAfterKm", searchRequest.getMaintenanceAfterKm());
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

	public Pagination<?> getPagination(String searchQuery, Pagination<?> page, Map<String, Object> paramValues) {
		String countQuery = "select count(*) from (" + searchQuery + ") as x";
		Long count = namedParameterJdbcTemplate.queryForObject(countQuery.toString(), paramValues, Long.class);
		Integer totalpages = (int) Math.ceil((double) count / page.getPageSize());
		page.setTotalPages(totalpages);
		page.setCurrentPage(page.getOffset());
		return page;
	}

	public void validateSortByOrder(final String sortBy) {
		List<String> sortByList = new ArrayList<String>();
		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}
		for (String s : sortByList) {
			if (s.contains(" ")
					&& (!s.toLowerCase().trim().endsWith("asc") && !s.toLowerCase().trim().endsWith("desc"))) {

				throw new CustomException(s.split(" ")[0],
						"Please send the proper sortBy order for the field " + s.split(" ")[0]);
			}
		}

	}

	public void validateEntityFieldName(String sortBy, final Class<?> object) {
		List<String> sortByList = new ArrayList<String>();
		if (sortBy.contains(",")) {
			sortByList = Arrays.asList(sortBy.split(","));
		} else {
			sortByList = Arrays.asList(sortBy);
		}
		Boolean isFieldExist = Boolean.FALSE;
		for (String s : sortByList) {
			for (int i = 0; i < object.getDeclaredFields().length; i++) {
				if (object.getDeclaredFields()[i].getName().equals(s.contains(" ") ? s.split(" ")[0] : s)) {
					isFieldExist = Boolean.TRUE;
					break;
				} else {
					isFieldExist = Boolean.FALSE;
				}
			}
			if (!isFieldExist) {
				throw new CustomException(s.contains(" ") ? s.split(" ")[0] : s, "Please send the proper Field Names ");

			}
		}

	}

}