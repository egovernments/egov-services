package entities.responses.eGovEIS.searchEmployeeLeave;

public class LeaveType {
    private boolean encashable;
    private String lastModifiedDate;
    private double lastModifiedBy;
    private String description;
    private boolean payEligible;
    private boolean active;
    private String createdDate;
    private boolean accumulative;
    private double createdBy;
    private String name;
    private String tenantId;
    private int id;
    private boolean halfdayAllowed;

    public boolean getEncashable() {
        return this.encashable;
    }

    public void setEncashable(boolean encashable) {
        this.encashable = encashable;
    }

    public String getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public double getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(double lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPayEligible() {
        return this.payEligible;
    }

    public void setPayEligible(boolean payEligible) {
        this.payEligible = payEligible;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getAccumulative() {
        return this.accumulative;
    }

    public void setAccumulative(boolean accumulative) {
        this.accumulative = accumulative;
    }

    public double getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(double createdBy) {
        this.createdBy = createdBy;
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

    public boolean getHalfdayAllowed() {
        return this.halfdayAllowed;
    }

    public void setHalfdayAllowed(boolean halfdayAllowed) {
        this.halfdayAllowed = halfdayAllowed;
    }
}
