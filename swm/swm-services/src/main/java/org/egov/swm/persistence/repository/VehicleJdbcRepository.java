package org.egov.swm.persistence.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.swm.domain.model.Pagination;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleSearch;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.domain.model.VendorSearch;
import org.egov.swm.domain.service.FuelTypeService;
import org.egov.swm.domain.service.VehicleTypeService;
import org.egov.swm.persistence.entity.VehicleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

@Service
public class VehicleJdbcRepository extends JdbcRepository {

	public static final String TABLE_NAME = "egswm_vehicle";

	@Autowired
	public VendorJdbcRepository vendorJdbcRepository;

	@Autowired
	private VehicleTypeService vehicleTypeService;

	@Autowired
	private FuelTypeService fuelTypeService;

	public Boolean uniqueCheck(String tenantId, String fieldName, String fieldValue, String uniqueFieldName,
			String uniqueFieldValue) {

		return uniqueCheck(TABLE_NAME, tenantId, fieldName, fieldValue, uniqueFieldName, uniqueFieldValue);
	}

	public Pagination<Vehicle> search(VehicleSearch searchRequest) {

		String searchQuery = "select * from " + TABLE_NAME + " :condition  :orderby ";

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

		if (searchRequest.getTenantId() != null) {
			addAnd(params);
			params.append("tenantId =:tenantId");
			paramValues.put("tenantId", searchRequest.getTenantId());
		}
		if (searchRequest.getRegNumber() != null) {
			addAnd(params);
			params.append("regNumber =:regNumber");
			paramValues.put("regNumber", searchRequest.getRegNumber());
		}
		if (searchRequest.getManufacturingDetails() != null) {

			if (searchRequest.getManufacturingDetails().getChassisSrNumber() != null) {
				addAnd(params);
				params.append("chassisSrNumber =:chassisSrNumber");
				paramValues.put("chassisSrNumber", searchRequest.getManufacturingDetails().getChassisSrNumber());
			}

			if (searchRequest.getManufacturingDetails().getEngineSrNumber() != null) {
				addAnd(params);
				params.append("engineSrNumber =:engineSrNumber");
				paramValues.put("engineSrNumber", searchRequest.getManufacturingDetails().getEngineSrNumber());
			}

			if (searchRequest.getInsuranceDetails().getInsuranceNumber() != null) {
				addAnd(params);
				params.append("insuranceNumber =:insuranceNumber");
				paramValues.put("insuranceNumber", searchRequest.getInsuranceDetails().getInsuranceNumber());
			}

			if (searchRequest.getManufacturingDetails().getModel() != null) {
				addAnd(params);
				params.append("model =:model");
				paramValues.put("model", searchRequest.getManufacturingDetails().getModel());
			}

		}

		if (searchRequest.getInsuranceDetails() != null) {
			if (searchRequest.getInsuranceDetails().getInsuranceValidityDate() != null) {
				addAnd(params);
				params.append("insuranceValidityDate =:insuranceValidityDate");
				paramValues.put("insuranceValidityDate",
						searchRequest.getInsuranceDetails().getInsuranceValidityDate());
			}
		}

		if (searchRequest.getPurchaseInfo() != null) {

			if (searchRequest.getPurchaseInfo().getPurchaseDate() != null) {
				addAnd(params);
				params.append("purchaseDate =:purchaseDate");
				paramValues.put("purchaseDate", searchRequest.getPurchaseInfo().getPurchaseDate());
			}

		}

		if (searchRequest.getVehicleTypeCode() != null) {
			addAnd(params);
			params.append("vehicleType =:vehicleType");
			paramValues.put("vehicleType", searchRequest.getVehicleTypeCode());
		}

		if (searchRequest.getFuelTypeCode() != null) {
			addAnd(params);
			params.append("fuelType =:fuelType");
			paramValues.put("fuelType", searchRequest.getFuelTypeCode());
		}

		if (searchRequest.getPurchaseYear() != null) {
			addAnd(params);
			params.append("yearOfPurchase =:yearOfPurchase");
			paramValues.put("yearOfPurchase", searchRequest.getPurchaseYear());
		}

		if (searchRequest.getVendorName() != null) {
			addAnd(params);
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

		page = (Pagination

		<Vehicle>) getPagination(searchQuery, page, paramValues);
		searchQuery = searchQuery + " :pagination";

		searchQuery = searchQuery.replace(":pagination",
				"limit " + page.getPageSize() + " offset " + page.getOffset() * page.getPageSize());

		BeanPropertyRowMapper row = new BeanPropertyRowMapper(VehicleEntity.class);

		List<Vehicle> vehicleList = new ArrayList<>();

		List<VehicleEntity> vehicleEntities = namedParameterJdbcTemplate.query(searchQuery.toString(), paramValues,
				row);
		Vehicle v;
		VendorSearch vendorSearch;
		Pagination<Vendor> vendors;
		for (VehicleEntity vehicleEntity : vehicleEntities) {

			v = vehicleEntity.toDomain();

			if (v.getVehicleType() != null && v.getVehicleType().getCode() != null) {
				v.setVehicleType(vehicleTypeService.getVehicleType(v.getTenantId(), v.getVehicleType().getCode(),
						new RequestInfo()));
			}

			if (v.getFuelType() != null && v.getFuelType().getCode() != null) {
				v.setFuelType(
						fuelTypeService.getFuelType(v.getTenantId(), v.getFuelType().getCode(), new RequestInfo()));
			}

			if (v.getVendor() != null && v.getVendor().getVendorNo() != null
					&& !v.getVendor().getVendorNo().isEmpty()) {
				vendorSearch = new VendorSearch();
				vendorSearch.setTenantId(v.getTenantId());
				vendorSearch.setVendorNo(v.getVendor().getVendorNo());

				vendors = vendorJdbcRepository.search(vendorSearch);
				if (vendors != null && vendors.getPagedData() != null && !vendors.getPagedData().isEmpty()) {
					v.setVendor(vendors.getPagedData().get(0));
				}
			}

			vehicleList.add(v);
		}

		page.setTotalResults(vehicleList.size());

		page.setPagedData(vehicleList);

		return page;
	}

}