package builders.employeeManagement.createEmployee;

import entities.employeeManagement.createEmployee.JurisdictionDetails;

public final class JurisdictionDetailsBuilder {

    JurisdictionDetails jurisdictionDetails = new JurisdictionDetails();

    public JurisdictionDetailsBuilder withJurisdictionType(String jurisdictionType) {
        jurisdictionDetails.setJurisdictionType(jurisdictionType);
        return this;
    }

    public JurisdictionDetailsBuilder withJurisdictionList(String jurisdictionList) {
        jurisdictionDetails.setJurisdictionList(jurisdictionList);
        return this;
    }

    public JurisdictionDetails build() {
        return jurisdictionDetails;
    }
}
