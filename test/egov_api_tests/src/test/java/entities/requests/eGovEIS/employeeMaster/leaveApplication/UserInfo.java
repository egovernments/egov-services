package entities.requests.eGovEIS.employeeMaster.leaveApplication;

public class UserInfo {
    private Roles[] roles;
    private String name;
    private String tenantId;
    private int id;

    public Roles[] getRoles() {
        return this.roles;
    }

    public void setRoles(Roles[] roles) {
        this.roles = roles;
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
