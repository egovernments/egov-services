package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.FuelType;
import org.egov.swm.domain.model.InsuranceDetails;
import org.egov.swm.domain.model.ManufacturingDetails;
import org.egov.swm.domain.model.PurchaseInfo;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleType;
import org.egov.swm.domain.model.Vendor;
import org.egov.swm.web.contract.Employee;

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

    private Long operatorsReq = null;

    private String model = null;

    private String driver = null;

    private String vendor = null;

    private Long purchaseDate = null;

    private Double price = null;

    private String sourceOfPurchase = null;

    private String remarks = null;

    private String insuranceNumber = null;

    private Long insuranceValidityDate = null;

    private Long kilometers = null;

    private Long endOfWarranty = null;

    private Boolean isVehicleUnderWarranty;

    private Boolean isUlbOwned;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public Vehicle toDomain() {

        final Vehicle vehicle = new Vehicle();
        vehicle.setTenantId(tenantId);
        vehicle.setVehicleType(VehicleType.builder().code(vehicleType).build());
        vehicle.setFuelType(FuelType.builder().code(fuelType).build());
        vehicle.setRegNumber(regNumber);
        vehicle.setManufacturingDetails(new ManufacturingDetails());
        vehicle.getManufacturingDetails().setEngineSrNumber(engineSrNumber);
        vehicle.getManufacturingDetails().setChassisSrNumber(chassisSrNumber);
        vehicle.getManufacturingDetails().setVehicleCapacity(vehicleCapacity);
        vehicle.setOperatorsReq(operatorsReq);
        vehicle.getManufacturingDetails().setModel(model);
        vehicle.setDriver(Employee.builder().code(driver).build());
        vehicle.setVendor(Vendor.builder().vendorNo(vendor).build());
        vehicle.setPurchaseInfo(new PurchaseInfo());
        vehicle.getPurchaseInfo().setPurchaseDate(purchaseDate);
        vehicle.getPurchaseInfo().setPrice(price);
        vehicle.getPurchaseInfo().setSourceOfPurchase(sourceOfPurchase);
        vehicle.setRemarks(remarks);
        vehicle.setInsuranceDetails(new InsuranceDetails());
        vehicle.getInsuranceDetails().setInsuranceNumber(insuranceNumber);
        vehicle.getInsuranceDetails().setInsuranceValidityDate(insuranceValidityDate);
        vehicle.setKilometers(kilometers);
        vehicle.setEndOfWarranty(endOfWarranty);
        vehicle.setIsVehicleUnderWarranty(isVehicleUnderWarranty);
        vehicle.setIsUlbOwned(isUlbOwned);
        vehicle.setAuditDetails(new AuditDetails());
        vehicle.getAuditDetails().setCreatedBy(createdBy);
        vehicle.getAuditDetails().setCreatedTime(createdTime);
        vehicle.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        vehicle.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return vehicle;

    }

}
