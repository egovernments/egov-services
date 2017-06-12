package builders.ptis;

import entities.ptis.FloorDetails;

public class FloorDetailsBuilder {

    FloorDetails floorDetails = new FloorDetails();

    public FloorDetailsBuilder withFloorNumber(String floorNumber) {
        floorDetails.setFloorNumber(floorNumber);
        return this;
    }

    public FloorDetailsBuilder withClassificationOfBuilding(String classificationOfBuilding) {
        floorDetails.setClassificationOfBuilding(classificationOfBuilding);
        return this;
    }

    public FloorDetailsBuilder withNatureOfUsage(String natureOfUsage) {
        floorDetails.setNatureOfUsage(natureOfUsage);
        return this;
    }

    public FloorDetailsBuilder withFirmName(String firmName) {
        floorDetails.setFirmName(firmName);
        return this;
    }

    public FloorDetailsBuilder withOccupancy(String occupancy) {
        floorDetails.setOccupancy(occupancy);
        return this;
    }

    public FloorDetailsBuilder withOccupantName(String occupantName) {
        floorDetails.setOccupantName(occupantName);
        return this;
    }

    public FloorDetailsBuilder withConstructionDate(String constructionDate) {
        floorDetails.setConstructionDate(constructionDate);
        return this;
    }

    public FloorDetailsBuilder withEffectiveFromDate(String effectiveFromDate) {
        floorDetails.setEffectiveFromDate(effectiveFromDate);
        return this;
    }

    public FloorDetailsBuilder withLength(String length) {
        floorDetails.setLength(length);
        return this;
    }

    public FloorDetailsBuilder withBreadth(String breadth) {
        floorDetails.setBreadth(breadth);
        return this;
    }

    public FloorDetailsBuilder withUnstructuredLand(String unstructuredLand) {
        floorDetails.setUnstructuredLand(unstructuredLand);
        return this;
    }

    public FloorDetailsBuilder withBuildingPermissionNumber(String buildingPermissionNumber) {
        floorDetails.setBuildingPermissionNumber(buildingPermissionNumber);
        return this;
    }

    public FloorDetailsBuilder withBuildingPermissionDate(String buildingPermissionDate) {
        floorDetails.setBuildingPermissionDate(buildingPermissionDate);
        return this;
    }

    public FloorDetailsBuilder withPlinthAreaInBuildingPlan(String plinthAreaInBuildingPlan) {
        floorDetails.setPlinthAreaInBuildingPlan(plinthAreaInBuildingPlan);
        return this;
    }

    public FloorDetails build() {
        return floorDetails;
    }
}
