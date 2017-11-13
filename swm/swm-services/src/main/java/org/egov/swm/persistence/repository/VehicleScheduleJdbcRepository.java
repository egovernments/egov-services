package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.VehicleSchedule;
import org.egov.swm.domain.model.VehicleScheduleSearch;
import org.egov.swm.persistence.entity.VehicleScheduleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VehicleScheduleJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vehicleschedule";

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public SupplierJdbcRepository contractorJdbcRepository;

	@Autowired
	public ServicedLocationsJdbcRepository servicedLocationsJdbcRepository;

	@Autowired
	public ServicesOfferedJdbcRepository servicesOfferedJdbcRepository;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<VehicleSchedule> search(VehicleScheduleSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), VehicleScheduleSearch.class);
		}

		String orderBy = "order by transactionNo";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getTransactionNo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("transactionNo in (:transactionNo)");
			paramValues.put("transactionNo", searchRequest.getTransactionNo());
		}

		if (searchRequest.getTransactionNos() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("transactionNo in (:transactionNos)");
			paramValues.put("transactionNos",
					new ArrayList<String>(Arrays.asList(searchRequest.getTransactionNos().split(","))));
		}
		if (searchRequest.getTenantId() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getRouteCode() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("route =:route");
			paramValues.put("route", searchRequest.getRouteCode());
		}

		if (searchRequest.getRegNumber() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("vehicle =:vehicle");
			paramValues.put("vehicle", searchRequest.getRegNumber());
		}

		if (searchRequest.getScheduledFrom() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("scheduledFrom =:scheduledFrom");
			paramValues.put("scheduledFrom", searchRequest.getScheduledFrom());
		}

		if (searchRequest.getScheduledTo() != null) {
			if (params.length() > 0) {
				params.append(" and ");
			}
			params.append("scheduledTo =:scheduledTo");
			paramValues.put("scheduledTo", searchRequest.getScheduledTo());
		}

		Pagination<VehicleSchedule> page = new Pagination<>();
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

		page = (Pagination<VehicleSchedule>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleScheduleEntity.class);

		List<VehicleSchedule> vehicleScheduleList = new ArrayList<>();

		List<VehicleScheduleEntity> vehicleScheduleEntities = namedParameterJdbcTemplate.query(searchQuery.toString(),
				paramValues, row);
		for (VehicleScheduleEntity vehicleScheduleEntity : vehicleScheduleEntities) {

			vehicleScheduleList.add(vehicleScheduleEntity.toDomain());

		}

		page.setTotalResults(vehicleScheduleList.size());

		page.setPagedData(vehicleScheduleList);

		return page;
	}

}