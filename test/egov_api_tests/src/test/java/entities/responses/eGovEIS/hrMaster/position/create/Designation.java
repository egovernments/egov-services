package entities.responses.eGovEIS.hrMaster.position.create;

public class Designation {
    private Object code;
    private Object chartOfAccounts;
    private Object name;
    private Object tenantId;
    private Object description;
    private Object active;
    private int id;

    public Object getCode() {
        return this.code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public Object getChartOfAccounts() {
        return this.chartOfAccounts;
    }

    public void setChartOfAccounts(Object chartOfAccounts) {
        this.chartOfAccounts = chartOfAccounts;
    }

    public Object getName() {
        return this.name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(Object tenantId) {
        this.tenantId = tenantId;
    }

    public Object getDescription() {
        return this.description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getActive() {
        return this.active;
    }

    public void setActive(Object active) {
        this.active = active;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
