package builders.works;

import entities.works.AdminSanctionDetails;

public class AdminSanctionDetailsBuilder {

    AdminSanctionDetails adminSanctionDetails = new AdminSanctionDetails();

    public AdminSanctionDetailsBuilder withAdministrationSanctionNumber(String administrationSanctionNumber) {
        adminSanctionDetails.setAdministrationSanctionNumber(administrationSanctionNumber);
        return this;
    }

    public AdminSanctionDetailsBuilder withAdminSanctionDate(String sanctionDate) {
        adminSanctionDetails.setAdminSanctionDate(sanctionDate);
        return this;
    }

    public AdminSanctionDetailsBuilder withAdminSanctionAuthority(String sanctionAuthority) {
        adminSanctionDetails.setAdminSanctionAuthority(sanctionAuthority);
        return this;
    }

    public AdminSanctionDetails build() {
        return adminSanctionDetails;
    }
}
