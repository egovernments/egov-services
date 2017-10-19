package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.RefillingPumpStation;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleFuellingDetails;
import org.egov.swm.domain.model.VehicleType;

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

	private String transactionId = null;

	private Long transactionDate = null;

	private String vehicleType = null;

	private String vehicleRegNo = null;

	private Long vehicleReadingDuringFuelling = null;

	private String refuellingStation = null;

	private String fuelFilled = null;

	private String typeOfFuel = null;

	private Double totalCostIncurred = null;

	private String receiptNo = null;

	private Long receiptDate = null;

	private String createdBy = null;

	private String lastModifiedBy = null;

	private Long createdTime = null;

	private Long lastModifiedTime = null;

	public VehicleFuellingDetails toDomain() {

		VehicleFuellingDetails vehicleFuellingDetails = new VehicleFuellingDetails();
		vehicleFuellingDetails.setId(id);
		vehicleFuellingDetails.setTenantId(tenantId);
		vehicleFuellingDetails.setTransactionId(transactionId);
		vehicleFuellingDetails.setTransactionDate(transactionDate);
		vehicleFuellingDetails.setVehicleType(VehicleType.builder().id(vehicleType).build());
		vehicleFuellingDetails.setVehicleRegNo(Vehicle.builder().id(vehicleRegNo).build());
		vehicleFuellingDetails.setVehicleReadingDuringFuelling(vehicleReadingDuringFuelling);
		vehicleFuellingDetails.setRefuellingStation(RefillingPumpStation.builder().id(refuellingStation).build());
		vehicleFuellingDetails.setFuelFilled(fuelFilled);
		vehicleFuellingDetails.setTypeOfFuel(typeOfFuel);
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
