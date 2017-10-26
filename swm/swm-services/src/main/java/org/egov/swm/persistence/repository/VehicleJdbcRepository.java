package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.persistence.entity.VehicleEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleJdbcRepository {

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Pagination<Vehicle> search(VehicleSearch searchRequest) {

		String searchQuery = "select * from egswm_vehicle :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VehicleSearch.class);
		}

		String orderBy = "order by regNumber";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getIds() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id in (:ids)");
			paramValues.put("ids", new ArrayList<String>(Arrays.asList(searchRequest.getIds().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}
		if (searchRequest.getId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("id =:id");
			paramValues.put("id", searchRequest.getId());
		}
		if (searchRequest.getRegNumber() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("regNumber =:regNumber");
			paramValues.put("regNumber", searchRequest.getRegNumber());
		}
		if (searchRequest.getUlbOwnedVehicle() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("ulbOwnedVehicle =:ulbOwnedVehicle");
			paramValues.put("ulbOwnedVehicle", searchRequest.getUlbOwnedVehicle());
		}
		if (searchRequest.getChassisSrNumber() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("chassisSrNumber =:chassisSrNumber");
			paramValues.put("chassisSrNumber", searchRequest.getChassisSrNumber());
		}
		if (searchRequest.getEngineSrNumber() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("engineSrNumber =:engineSrNumber");
			paramValues.put("engineSrNumber", searchRequest.getEngineSrNumber());
		}
		if (searchRequest.getInsuranceNumber() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("insuranceNumber =:insuranceNumber");
			paramValues.put("insuranceNumber", searchRequest.getInsuranceNumber());
		}

		if (searchRequest.getInsuranceValidityDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("insuranceValidityDate =:insuranceValidityDate");
			paramValues.put("insuranceValidityDate", searchRequest.getInsuranceValidityDate());
		}

		if (searchRequest.getModel() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("model =:model");
			paramValues.put("model", searchRequest.getModel());
		}

		if (searchRequest.getPurchaseDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("purchaseDate =:purchaseDate");
			paramValues.put("purchaseDate", searchRequest.getPurchaseDate());
		}

		if (searchRequest.getVehicleTypeName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vehicleType =:vehicleType");
			paramValues.put("vehicleType", searchRequest.getVehicleTypeName());
		}

		if (searchRequest.getPurchaseYear() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("yearOfPurchase =:yearOfPurchase");
			paramValues.put("yearOfPurchase", searchRequest.getPurchaseYear());
		}

		if (searchRequest.getVendorName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vendor =:vendor");
			paramValues.put("vendor", searchRequest.getVendorName());
		}

		Pagination<Vehicle> page = new Pagination<>();
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

		page = (Pagination<Vehicle>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleEntity.class);

		List<Vehicle> vehicleList = new ArrayList<>();

		List<VehicleEntity> vehicleEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);

		for (VehicleEntity vehicleEntity : vehicleEntities) {

			vehicleList.add(vehicleEntity.toDomain());
		}

		page.setTotalResults(vehicleList.size());

		page.setPagedData(vehicleList);

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