package builders.pgrCollection.createComplaint;

import entities.requests.pgrCollections.createComplaint.Values;

public final class ValuesBuilder {

    Values values = new Values();

    public ValuesBuilder() {
        values.setReceivingMode("MANUAL");
        values.setReceivingCenter("5");
        values.setStatus("REGISTERED");
        values.setComplainantAddress("erode");
    }

    public ValuesBuilder(String update) {
        values.setChildLocationId("131");
        values.setStatus("PROCESSING");
        values.setLocationId("173");
        values.setStateId("7");
        values.setReceivingMode("WEBSITE");
        values.setDepartmentId("19");
        values.setApprovalComments("approved");
        values.setAssignmentId("1");
    }

    public Values build() {
        return values;
    }
}
