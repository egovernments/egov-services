package entities.responses.eGovEIS.searchEISMasters.position;

public class Position {
    private boolean isPostOutsourced;
    private Deptdesig deptdesig;
    private String name;
    private String tenantId;
    private boolean active;
    private int id;

    public boolean getIsPostOutsourced() {
        return this.isPostOutsourced;
    }

    public void setIsPostOutsourced(boolean isPostOutsourced) {
        this.isPostOutsourced = isPostOutsourced;
    }

    public Deptdesig getDeptdesig() {
        return this.deptdesig;
    }

    public void setDeptdesig(Deptdesig deptdesig) {
        this.deptdesig = deptdesig;
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

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
