package builders.eGovEIS.hrMaster.designation.create;

import entities.requests.eGovEIS.hrMaster.designation.create.Designation;
import tests.BaseAPITest;

public class DesignationBuilder extends BaseAPITest {

    Designation designation = new Designation();

    public DesignationBuilder() {
        String accountOfficerCode = get3DigitRandomInt();
        designation.setName("Accounts Officer" + accountOfficerCode);
        designation.setCode("AO" + accountOfficerCode);
        designation.setDescription("Accounts Officer" + accountOfficerCode);
        designation.setChartOfAccounts(null);
        designation.setActive(true);
        designation.setTenantId("ap.kurnool");
    }

    public DesignationBuilder withCode(String code) {
        designation.setCode(code);
        return this;
    }

    public DesignationBuilder withChartOfAccounts(String chartOfAccounts) {
        designation.setChartOfAccounts(chartOfAccounts);
        return this;
    }

    public DesignationBuilder withName(String name) {
        designation.setName(name);
        return this;
    }

    public DesignationBuilder withTenantId(String tenantId) {
        designation.setTenantId(tenantId);
        return this;
    }

    public DesignationBuilder withDescription(String description) {
        designation.setDescription(description);
        return this;
    }

    public DesignationBuilder withActive(boolean active) {
        designation.setActive(active);
        return this;
    }

    public Designation build() {
        return designation;
    }
}
