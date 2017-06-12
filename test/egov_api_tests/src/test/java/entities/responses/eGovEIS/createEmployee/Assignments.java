package entities.responses.eGovEIS.createEmployee;

public class Assignments {
    private String govtOrderNumber;
    private Object documents;
    private String lastModifiedDate;
    private String toDate;
    private int lastModifiedBy;
    private int functionary;
    private String fromDate;
    private String createdDate;
    private int fund;
    private int createdBy;
    private boolean isPrimary;
    private int function;
    private int grade;
    private String tenantId;
    private int id;
    private int position;
    private int designation;
    private int department;
    private Hod[] hod;

    public String getGovtOrderNumber() {
        return this.govtOrderNumber;
    }

    public void setGovtOrderNumber(String govtOrderNumber) {
        this.govtOrderNumber = govtOrderNumber;
    }

    public Object getDocuments() {
        return this.documents;
    }

    public void setDocuments(Object documents) {
        this.documents = documents;
    }

    public String getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getToDate() {
        return this.toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public int getFunctionary() {
        return this.functionary;
    }

    public void setFunctionary(int functionary) {
        this.functionary = functionary;
    }

    public String getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getFund() {
        return this.fund;
    }

    public void setFund(int fund) {
        this.fund = fund;
    }

    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public boolean getIsPrimary() {
        return this.isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public int getFunction() {
        return this.function;
    }

    public void setFunction(int function) {
        this.function = function;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDesignation() {
        return this.designation;
    }

    public void setDesignation(int designation) {
        this.designation = designation;
    }

    public int getDepartment() {
        return this.department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public Hod[] getHod() {
        return this.hod;
    }

    public void setHod(Hod[] hod) {
        this.hod = hod;
    }
}
