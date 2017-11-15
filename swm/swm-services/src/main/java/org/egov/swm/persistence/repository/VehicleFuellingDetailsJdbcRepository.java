package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.RefillingPumpStationSearch;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleFuellingDetailsSearch;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.repository.RefillingPumpStationRepository;
import org.egov.swm.domain.repository.VehicleRepository;
import org.egov.swm.domain.service.FuelTypeService;
import org.egov.swm.persistence.entity.VehicleFuellingDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleFuellingDetailsJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vehiclefuellingdetails";

	@Autowired
	private FuelTypeService fuelTypeService;

	@Autowired
	private RefillingPumpStationRepository refillingPumpStationRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<VehicleFuellingDetails> search(VehicleFuellingDetailsSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

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

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}
		if (searchRequest.getTransactionNo() != null) {
			addAnd(params);
			params.append("transactionNo =:transactionNo");
			paramValues.put("transactionNo", searchRequest.getTransactionNo());
		}
		if (searchRequest.getTransactionDate() != null) {
			addAnd(params);
			params.append("transactionDate =:transactionDate");
			paramValues.put("transactionDate", searchRequest.getTransactionDate());
		}
		if (searchRequest.getFuelTypeCode() != null) {
			addAnd(params);
			params.append("typeOfFuel =:typeOfFuel");
			paramValues.put("typeOfFuel", searchRequest.getFuelTypeCode());
		}
		if (searchRequest.getRegNumber() != null) {
			addAnd(params);
			params.append("vehicle =:vehicle");
			paramValues.put("vehicle", searchRequest.getRegNumber());
		}
		if (searchRequest.getVehicleReadingDuringFuelling() != null) {
			addAnd(params);
			params.append("vehicleReadingDuringFuelling =:vehicleReadingDuringFuelling");
			paramValues.put("vehicleReadingDuringFuelling", searchRequest.getVehicleReadingDuringFuelling());
		}

		if (searchRequest.getRefuellingStationName() != null) {
			addAnd(params);
			params.append("refuellingStation =:refuellingStation");
			paramValues.put("refuellingStation", searchRequest.getRefuellingStationName());
		}

		if (searchRequest.getFuelFilled() != null) {
			addAnd(params);
			params.append("fuelFilled =:fuelFilled");
			paramValues.put("fuelFilled", searchRequest.getFuelFilled());
		}

		if (searchRequest.getTotalCostIncurred() != null) {
			addAnd(params);
			params.append("totalCostIncurred =:totalCostIncurred");
			paramValues.put("totalCostIncurred", searchRequest.getTotalCostIncurred());
		}

		if (searchRequest.getReceiptNo() != null) {
			addAnd(params);
			params.append("receiptNo =:receiptNo");
			paramValues.put("receiptNo", searchRequest.getReceiptNo());
		}

		if (searchRequest.getReceiptDate() != null) {

			addAnd(params);
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

		VehicleFuellingDetails vfd;
		Pagination<RefillingPumpStation> refillingPumpStationList;
		VehicleSearch vehicleSearch;
		Pagination<Vehicle> vehicleList;
		RefillingPumpStationSearch refillingPumpStationSearch;

		for (VehicleFuellingDetailsEntity vehicleFuellingDetailsEntity : vehicleFuellingDetailsEntities) {
			vfd = vehicleFuellingDetailsEntity.toDomain();

			if (vfd.getTypeOfFuel() != null && vfd.getTypeOfFuel().getCode() != null
					&& !vfd.getTypeOfFuel().getCode().isEmpty()) {

				vfd.setTypeOfFuel(fuelTypeService.getFuelType(vfd.getTenantId(), vfd.getTypeOfFuel().getCode(),
						new RequestInfo()));

			}

			if (vfd.getVehicle() != null && vfd.getVehicle().getRegNumber() != null
					&& !vfd.getVehicle().getRegNumber().isEmpty()) {

				vehicleSearch = new VehicleSearch();
				vehicleSearch.setTenantId(vfd.getTenantId());
				vehicleSearch.setRegNumber(vfd.getVehicle().getRegNumber());
				vehicleList = vehicleRepository.search(vehicleSearch);

				if (vehicleList != null && vehicleList.getPagedData() != null && !vehicleList.getPagedData().isEmpty())
					vfd.setVehicle(vehicleList.getPagedData().get(0));

			}

			if (vfd.getRefuellingStation() != null && vfd.getRefuellingStation().getCode() != null
					&& vfd.getRefuellingStation().getCode().isEmpty()) {
				refillingPumpStationSearch = new RefillingPumpStationSearch();
				refillingPumpStationSearch.setTenantId(vfd.getTenantId());
				refillingPumpStationSearch.setCode(vfd.getRefuellingStation().getCode());

				refillingPumpStationList = refillingPumpStationRepository.search(refillingPumpStationSearch);

				if (refillingPumpStationList != null && refillingPumpStationList.getPagedData() != null
						&& !refillingPumpStationList.getPagedData().isEmpty())
					vfd.setRefuellingStation(refillingPumpStationList.getPagedData().get(0));
			}

			vehicleFuellingDetailsList.add(vfd);
		}

		page.setTotalResults(vehicleFuellingDetailsList.size());

		page.setPagedData(vehicleFuellingDetailsList);

		return page;
	}

}