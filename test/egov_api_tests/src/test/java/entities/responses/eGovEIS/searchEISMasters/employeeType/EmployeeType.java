package entities.responses.eGovEIS.searchEISMasters.employeeType;

public class EmployeeType {
    private String chartOfAccounts;
    private String name;
    private String tenantId;
    private int id;

    public String getChartOfAccounts() {
        return this.chartOfAccounts;
    }

    public void setChartOfAccounts(String chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
