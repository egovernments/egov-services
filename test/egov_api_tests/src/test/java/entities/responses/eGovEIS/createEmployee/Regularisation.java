package entities.responses.eGovEIS.createEmployee;

public class Regularisation {
    private String orderNo;
    private String createdDate;
    private Object documents;
    private int createdBy;
    private String lastModifiedDate;
    private int lastModifiedBy;
    private String declaredOn;
    private String tenantId;
    private int id;
    private int designation;
    private String orderDate;
    private String remarks;

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Object getDocuments() {
        return this.documents;
    }

    public void setDocuments(Object documents) {
        this.documents = documents;
    }

    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getDeclaredOn() {
        return this.declaredOn;
    }

    public void setDeclaredOn(String declaredOn) {
        this.declaredOn = declaredOn;
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

    public int getDesignation() {
        return this.designation;
    }

    public void setDesignation(int designation) {
        this.designation = designation;
    }

    public String getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
