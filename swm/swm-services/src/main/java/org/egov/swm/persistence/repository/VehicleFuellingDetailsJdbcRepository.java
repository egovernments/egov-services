package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.persistence.entity.VehicleFuellingDetailsEntity;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleFuellingDetailsJdbcRepository {

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Pagination<VehicleFuellingDetails> search(VehicleFuellingDetailsSearch searchRequest) {

		String searchQuery = "select * from egswm_vehiclefuellingdetails :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VehicleFuellingDetailsSearch.class);
		}

		String orderBy = "order by transactionNo";
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
		if (searchRequest.getTransactionNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("transactionNo =:transactionNo");
			paramValues.put("transactionNo", searchRequest.getTransactionNo());
		}
		if (searchRequest.getTransactionDate() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("transactionDate =:transactionDate");
			paramValues.put("transactionDate", searchRequest.getTransactionDate());
		}
		if (searchRequest.getVehicleTypeCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vehicleType =:vehicleType");
			paramValues.put("vehicleType", searchRequest.getVehicleTypeCode());
		}
		if (searchRequest.getFuelTypeCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("typeOfFuel =:typeOfFuel");
			paramValues.put("typeOfFuel", searchRequest.getFuelTypeCode());
		}
		if (searchRequest.getRegNumber() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vehicleRegNo =:vehicleRegNo");
			paramValues.put("vehicleRegNo", searchRequest.getRegNumber());
		}
		if (searchRequest.getVehicleReadingDuringFuelling() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vehicleReadingDuringFuelling =:vehicleReadingDuringFuelling");
			paramValues.put("vehicleReadingDuringFuelling", searchRequest.getVehicleReadingDuringFuelling());
		}

		if (searchRequest.getRefuellingStationName() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("refuellingStation =:refuellingStation");
			paramValues.put("refuellingStation", searchRequest.getRefuellingStationName());
		}

		if (searchRequest.getFuelFilled() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("fuelFilled =:fuelFilled");
			paramValues.put("fuelFilled", searchRequest.getFuelFilled());
		}

		if (searchRequest.getTotalCostIncurred() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("totalCostIncurred =:totalCostIncurred");
			paramValues.put("totalCostIncurred", searchRequest.getTotalCostIncurred());
		}

		if (searchRequest.getReceiptNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("receiptNo =:receiptNo");
			paramValues.put("receiptNo", searchRequest.getReceiptNo());
		}

		if (searchRequest.getReceiptDate() != null) {

			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("receiptDate =:receiptDate");
			paramValues.put("receiptDate", searchRequest.getReceiptDate());
		}

		Pagination<VehicleFuellingDetails> page = new Pagination<>();
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

		page = (Pagination<VehicleFuellingDetails>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleFuellingDetailsEntity.class);

		List<VehicleFuellingDetails> vehicleFuellingDetailsList = new ArrayList<>();

		List<VehicleFuellingDetailsEntity> vehicleFuellingDetailsEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		for (VehicleFuellingDetailsEntity vehicleFuellingDetailsEntity : vehicleFuellingDetailsEntities) {

			vehicleFuellingDetailsList.add(vehicleFuellingDetailsEntity.toDomain());
		}

		page.setTotalResults(vehicleFuellingDetailsList.size());

		page.setPagedData(vehicleFuellingDetailsList);

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