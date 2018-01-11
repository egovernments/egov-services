package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleFuellingDetails;

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
public class VehicleFuellingDetailsEntity {

    private String id = null;

    private String tenantId = null;

    private String transactionNo = null;

    private Long transactionDate = null;

    private String vehicleType = null;

    private String vehicle = null;

    private Double vehicleReadingDuringFuelling = null;

    private String refuellingStation = null;

    private Double fuelFilled = null;

    private Double totalCostIncurred = null;

    private String receiptNo = null;

    private Long receiptDate = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public VehicleFuellingDetails toDomain() {

        final VehicleFuellingDetails vehicleFuellingDetails = new VehicleFuellingDetails();
        vehicleFuellingDetails.setTenantId(tenantId);
        vehicleFuellingDetails.setTransactionNo(transactionNo);
        vehicleFuellingDetails.setTransactionDate(transactionDate);
        vehicleFuellingDetails.setVehicle(Vehicle.builder().regNumber(vehicle).build());
        vehicleFuellingDetails.setVehicleReadingDuringFuelling(vehicleReadingDuringFuelling);
        vehicleFuellingDetails.setRefuellingStation(RefillingPumpStation.builder().code(refuellingStation).build());
        vehicleFuellingDetails.setFuelFilled(fuelFilled);
        vehicleFuellingDetails.setTotalCostIncurred(totalCostIncurred);
        vehicleFuellingDetails.setReceiptNo(receiptNo);
        vehicleFuellingDetails.setReceiptDate(receiptDate);
        vehicleFuellingDetails.setAuditDetails(new AuditDetails());
        vehicleFuellingDetails.getAuditDetails().setCreatedBy(createdBy);
        vehicleFuellingDetails.getAuditDetails().setCreatedTime(createdTime);
        vehicleFuellingDetails.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        vehicleFuellingDetails.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        return vehicleFuellingDetails;

    }

}
