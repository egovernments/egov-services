package entities.responses.userServices.userDetails;

public class UserDetailsResponse {
    private String mobileNumber;
    private Roles[] roles;
    private String name;
    private boolean active;
    private String emailId;
    private int id;
    private String userName;
    private String locale;
    private String type;
    private Actions[] actions;

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Actions[] getActions() {
        return this.actions;
    }

    public void setActions(Actions[] actions) {
        this.actions = actions;
    }
}
