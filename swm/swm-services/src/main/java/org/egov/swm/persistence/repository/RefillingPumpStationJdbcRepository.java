package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Boundary;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.service.FuelTypeService;
import org.egov.swm.domain.service.OilCompanyService;
import org.egov.swm.persistence.entity.RefillingPumpStationEntity;
import org.egov.swm.web.repository.BoundaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RefillingPumpStationJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_refillingpumpstation";

	@Autowired
	private OilCompanyService oilCompanyService;

	@Autowired
	private FuelTypeService fuelTypeService;

	@Autowired
	private BoundaryRepository boundaryRepository;

	public Boolean checkForUniqueRecords(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {
		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<RefillingPumpStation> search(RefillingPumpStationSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

		Map<String, Object> paramValues = new HashMap<>();
		StringBuffer params = new StringBuffer();

		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			validateSortByOrder(searchRequest.getSortBy());
			validateEntityFieldName(searchRequest.getSortBy(), RefillingPumpStationSearch.class);
		}

		String orderBy = "order by name";
		if (searchRequest.getSortBy() != null && !searchRequest.getSortBy().isEmpty()) {
			orderBy = "order by " + searchRequest.getSortBy();
		}

		if (searchRequest.getCodes() != null) {
			addAnd(params);
			params.append("code in (:codes)");
			paramValues.put("codes", new ArrayList<String>(Arrays.asList(searchRequest.getCodes().split(","))));
		}

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}

		if (searchRequest.getCode() != null) {
			addAnd(params);
			params.append("code =:code");
			paramValues.put("code", searchRequest.getCode());
		}

		if (searchRequest.getName() != null) {
			addAnd(params);
			params.append("name =:name");
			paramValues.put("name", searchRequest.getName());
		}

		if (searchRequest.getQuantity() != null) {
			addAnd(params);
			params.append("quantity =:quantity");
			paramValues.put("quantity", searchRequest.getQuantity());
		}

		if (searchRequest.getTypeOfFuelCode() != null) {
			addAnd(params);
			params.append("typeoffuel =:typeoffuel");
			paramValues.put("typeoffuel", searchRequest.getTypeOfFuelCode());
		}

		if (searchRequest.getLocationCode() != null) {
			addAnd(params);
			params.append("location =:location");
			paramValues.put("location", searchRequest.getLocationCode());
		}

		if (searchRequest.getTypeOfPumpCode() != null) {
			addAnd(params);
			params.append("typeofpump =:typeofpump");
			paramValues.put("typeofpump", searchRequest.getTypeOfPumpCode());
		}

		Pagination<RefillingPumpStation> page = new Pagination<>();
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

		page = (Pagination<RefillingPumpStation>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(RefillingPumpStationEntity.class);

		List<RefillingPumpStation> refillingPumpStationList = new ArrayList<>();

		List<RefillingPumpStationEntity> refillingPumpStationEntities = namedParameterJdbcTemplate
				.query(searchQuery.toString(), paramValues, row);

		RefillingPumpStation refillingPumpStation;

		for (RefillingPumpStationEntity refillingPumpStationEntity : refillingPumpStationEntities) {

			refillingPumpStation = refillingPumpStationEntity.toDomain();

			if (refillingPumpStation.getTypeOfFuel() != null) {

				refillingPumpStation.setTypeOfFuel(fuelTypeService.getFuelType(refillingPumpStation.getTenantId(),
						refillingPumpStation.getTypeOfFuel().getCode(), new RequestInfo()));

			}

			if (refillingPumpStation.getTypeOfPump() != null) {

				refillingPumpStation.setTypeOfPump(oilCompanyService.getOilCompany(refillingPumpStation.getTenantId(),
						refillingPumpStation.getTypeOfPump().getCode(), new RequestInfo()));
			}

			if (refillingPumpStation.getLocation() != null && refillingPumpStation.getLocation().getCode() != null) {

				Boundary boundary = boundaryRepository.fetchBoundaryByCode(refillingPumpStation.getLocation().getCode(),
						refillingPumpStation.getTenantId());

				if (boundary != null)
					refillingPumpStation.setLocation(boundary);
			}

			refillingPumpStationList.add(refillingPumpStation);
		}

		page.setTotalResults(refillingPumpStationList.size());

		page.setPagedData(refillingPumpStationList);

		return page;
	}
}
