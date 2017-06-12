package entities.responses.eGovEIS.searchEmployee;

public class Employee {
    private String bankAccount;
    private String code;
    private Assignments[] assignments;
    private String gender;
    private int bankBranch;
    private Object documents;
    private String mobileNumber;
    private boolean active;
    private String emailId;
    private String userName;
    private String type;
    private String employeeStatus;
    private Object aadhaarNumber;
    private int bank;
    private int employeeType;
    private String name;
    private String tenantId;
    private int id;
    private String salutation;
    private Object pan;
    private int[] jurisdictions;

    public String getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Assignments[] getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Assignments[] assignments) {
        this.assignments = assignments;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getBankBranch() {
        return this.bankBranch;
    }

    public void setBankBranch(int bankBranch) {
        this.bankBranch = bankBranch;
    }

    public Object getDocuments() {
        return this.documents;
    }

    public void setDocuments(Object documents) {
        this.documents = documents;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmployeeStatus() {
        return this.employeeStatus;
    }

    public void setEmployeeStatus(String employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Object getAadhaarNumber() {
        return this.aadhaarNumber;
    }

    public void setAadhaarNumber(Object aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public int getBank() {
        return this.bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public int getEmployeeType() {
        return this.employeeType;
    }

    public void setEmployeeType(int employeeType) {
        this.employeeType = employeeType;
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

    public String getSalutation() {
        return this.salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public Object getPan() {
        return this.pan;
    }

    public void setPan(Object pan) {
        this.pan = pan;
    }

    public int[] getJurisdictions() {
        return this.jurisdictions;
    }

    public void setJurisdictions(int[] jurisdictions) {
        this.jurisdictions = jurisdictions;
    }
}
