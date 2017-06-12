package builders.eGovEIS.hrMaster.position.create;

import entities.requests.eGovEIS.hrMaster.position.create.Designation;

public class DesignationBuilder {

    Designation designation = new Designation();

    public DesignationBuilder() {
        designation.setId(4);
    }

    public DesignationBuilder withId(int id) {
        designation.setId(id);
        return this;
    }

    public Designation build() {
        return designation;
    }
}
