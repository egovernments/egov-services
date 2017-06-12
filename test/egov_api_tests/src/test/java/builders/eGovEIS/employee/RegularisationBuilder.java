package builders.eGovEIS.employee;

import entities.requests.eGovEIS.employee.Regularisation;

public class RegularisationBuilder {

    Regularisation regularisation = new Regularisation();

    public RegularisationBuilder() {
        regularisation.setDesignation(1);
        regularisation.setDeclaredOn("18/09/2016");
        regularisation.setOrderNo("A1");
        regularisation.setOrderDate("18/09/2016");
        regularisation.setRemarks("None");
        regularisation.setCreatedBy(1);
        regularisation.setCreatedDate("18/09/2016");
        regularisation.setLastModifiedBy(1);
        regularisation.setLastModifiedDate("18/09/2016");
    }

    public Regularisation build() {
        return regularisation;
    }
}
