package org.egov.swm.persistence.entity;

import org.egov.swm.domain.model.AuditDetails;
import org.egov.swm.domain.model.Route;
import org.egov.swm.domain.model.Vehicle;
import org.egov.swm.domain.model.VehicleTripSheetDetails;

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
public class VehicleTripSheetDetailsEntity {

    private String tenantId = null;

    private String tripNo = null;

    private String vehicle = null;

    private String route = null;

    private Long tripStartDate = null;

    private Long tripEndDate = null;

    private Long inTime = null;

    private Long outTime = null;

    private Double entryWeight = null;

    private Double exitWeight = null;

    private String createdBy = null;

    private String lastModifiedBy = null;

    private Long createdTime = null;

    private Long lastModifiedTime = null;

    public VehicleTripSheetDetails toDomain() {

        final VehicleTripSheetDetails vehicleTripSheetDetails = new VehicleTripSheetDetails();
        vehicleTripSheetDetails.setTenantId(tenantId);
        vehicleTripSheetDetails.setTripNo(tripNo);
        vehicleTripSheetDetails.setVehicle(Vehicle.builder().regNumber(vehicle).build());
        vehicleTripSheetDetails.setRoute(Route.builder().code(route).build());
        vehicleTripSheetDetails.setTripStartDate(tripStartDate);
        vehicleTripSheetDetails.setTripEndDate(tripEndDate);
        vehicleTripSheetDetails.setInTime(inTime);
        vehicleTripSheetDetails.setOutTime(outTime);
        vehicleTripSheetDetails.setEntryWeight(entryWeight);
        vehicleTripSheetDetails.setExitWeight(exitWeight);
        vehicleTripSheetDetails.setAuditDetails(new AuditDetails());
        vehicleTripSheetDetails.getAuditDetails().setCreatedBy(createdBy);
        vehicleTripSheetDetails.getAuditDetails().setCreatedTime(createdTime);
        vehicleTripSheetDetails.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        vehicleTripSheetDetails.getAuditDetails().setLastModifiedTime(lastModifiedTime);

        if(entryWeight != null && exitWeight !=null)
            vehicleTripSheetDetails.setGarbageWeight(entryWeight - exitWeight);

        return vehicleTripSheetDetails;

    }

}
