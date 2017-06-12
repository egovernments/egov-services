package builders.ptis;

import entities.ptis.EditFloorDetails;

public class EditFloorDetailsBuilder {

    EditFloorDetails editfloorDls = new EditFloorDetails();

    public EditFloorDetailsBuilder withEditFloorNumber(String editfloorNumber) {
        editfloorDls.setEditfloorNumber(editfloorNumber);
        return this;
    }

    public EditFloorDetailsBuilder withEditclassificationOfBuilding(String editclassificationOfBuilding) {
        editfloorDls.setEditclassificationOfBuilding(editclassificationOfBuilding);
        return this;
    }

    public EditFloorDetailsBuilder withEditnatureOfUsage(String editnatureOfUsage) {
        editfloorDls.setEditnatureOfUsage(editnatureOfUsage);
        return this;
    }

    public EditFloorDetailsBuilder withEditfirmName(String editfirmName) {
        editfloorDls.setEditfirmName(editfirmName);
        return this;
    }

    public EditFloorDetailsBuilder withEditoccupancy(String editoccupancy) {
        editfloorDls.setEditoccupancy(editoccupancy);
        return this;
    }

    public EditFloorDetailsBuilder withEditoccupantName(String editoccupantName) {
        editfloorDls.setEditoccupantName(editoccupantName);
        return this;
    }

    public EditFloorDetailsBuilder withEditconstructionDate(String editconstructionDate) {
        editfloorDls.setEditconstructionDate(editconstructionDate);
        return this;
    }

    public EditFloorDetailsBuilder withEditeffectiveFromDate(String editeffectiveFromDate) {
        editfloorDls.setEditeffectiveFromDate(editeffectiveFromDate);
        return this;
    }

    public EditFloorDetailsBuilder withEditunstructuredLand(String editunstructuredLand) {
        editfloorDls.setEditunstructuredLand(editunstructuredLand);
        return this;
    }

    public EditFloorDetailsBuilder withEditlength(String editlength) {
        editfloorDls.setEditlength(editlength);
        return this;
    }

    public EditFloorDetailsBuilder withEditbreadth(String editbreadth) {
        editfloorDls.setEditbreadth(editbreadth);
        return this;
    }

    public EditFloorDetailsBuilder withEditplinthArea(String editplinthArea) {
        editfloorDls.setEditplinthArea(editplinthArea);
        return this;
    }

    public EditFloorDetailsBuilder withEditbuildingPermissionNumber(String editbuildingPermissionNumber) {
        editfloorDls.setEditbuildingPermissionNumber(editbuildingPermissionNumber);
        return this;
    }

    public EditFloorDetailsBuilder withEditbuildingPermissionDate(String editbuildingPermissionDate) {
        editfloorDls.setEditbuildingPermissionDate(editbuildingPermissionDate);
        return this;
    }

    public EditFloorDetailsBuilder withEditplinthAreaInBuildingPlan(String editplinthAreaInBuildingPlan) {
        editfloorDls.setEditplinthAreaInBuildingPlan(editplinthAreaInBuildingPlan);
        return this;
    }

    public EditFloorDetails build() {
        return editfloorDls;
    }
}





