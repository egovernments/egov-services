package entities.responses.lams;

public class AgreementsAllottee {
    private Object aadhaarNumber;
    private Object password;
    private long mobileNumber;
    private String name;
    private Object emailId;
    private int id;
    private Object permanentAddress;
    private Object pan;
    private String userName;

    public Object getAadhaarNumber() {
        return this.aadhaarNumber;
    }

    public void setAadhaarNumber(Object aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public Object getPassword() {
        return this.password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public long getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEmailId() {
        return this.emailId;
    }

    public void setEmailId(Object emailId) {
        this.emailId = emailId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getPermanentAddress() {
        return this.permanentAddress;
    }

    public void setPermanentAddress(Object permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public Object getPan() {
        return this.pan;
    }

    public void setPan(Object pan) {
        this.pan = pan;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
