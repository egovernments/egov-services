package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Documents;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleType;
import org.egov.swm.domain.model.Vendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleEntity {

	private String id = null;

	private String tenantId = null;

	private String vehicleType = null;

	private String fuelType = null;

	private String regNumber = null;

	private String engineSrNumber = null;

	private String chassisSrNumber = null;

	private Double vehicleCapacity = null;

	private Long numberOfPersonsReq = null;

	private String model = null;

	private Boolean ulbOwnedVehicle = null;

	private String vendor = null;

	private String vehicleDriverName = null;

	private Long purchaseDate = null;

	private String yearOfPurchase = null;

	private Double price = null;

	private String sourceOfPurchase = null;

	private String remarks = null;

	private String insuranceNumber = null;

	private Long insuranceValidityDate = null;

	private String insuranceDocuments = null;

	private Boolean isUnderWarranty = null;

	private Long kilometers = null;

	private Long endOfWarranty = null;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;

	public Vehicle toDomain() {

		Vehicle vehicle = new Vehicle();
		vehicle.setId(id);
		vehicle.setTenantId(tenantId);
		vehicle.setVehicleType(VehicleType.builder().code(vehicleType).build());
		vehicle.setFuelType(FuelType.builder().code(fuelType).build());
		vehicle.setRegNumber(regNumber);
		vehicle.setEngineSrNumber(engineSrNumber);
		vehicle.setChassisSrNumber(chassisSrNumber);
		vehicle.setVehicleCapacity(vehicleCapacity);
		vehicle.setNumberOfPersonsReq(numberOfPersonsReq);
		vehicle.setModel(model);
		vehicle.setUlbOwnedVehicle(ulbOwnedVehicle);
		vehicle.setVendor(Vendor.builder().name(vendor).build());
		vehicle.setVehicleDriverName(vehicleDriverName);
		vehicle.setPurchaseDate(purchaseDate);
		vehicle.setYearOfPurchase(yearOfPurchase);
		vehicle.setPrice(price);
		vehicle.setSourceOfPurchase(sourceOfPurchase);
		vehicle.setRemarks(remarks);
		vehicle.setInsuranceNumber(insuranceNumber);
		vehicle.setInsuranceValidityDate(insuranceValidityDate);
		vehicle.setInsuranceDocuments(Documents.builder().id(insuranceDocuments).build());
		vehicle.setIsUnderWarranty(isUnderWarranty);
		vehicle.setKilometers(kilometers);
		vehicle.setEndOfWarranty(endOfWarranty);
		vehicle.setAuditDetails(new AuditDetails());
		vehicle.getAuditDetails().setCreatedBy(createdBy);
		vehicle.getAuditDetails().setCreatedTime(createdTime);
		vehicle.getAuditDetails().setLastModifiedBy(lastModifiedBy);
		vehicle.getAuditDetails().setLastModifiedTime(lastModifiedTime);

		return vehicle;

	}

}
