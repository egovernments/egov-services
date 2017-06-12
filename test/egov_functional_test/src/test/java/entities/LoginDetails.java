package entities;

public class LoginDetails {

    private String loginId;
    private String password;
    private Boolean hasZone;

    public Boolean getHasZone() {
        return hasZone;
    }

    public void setHasZone(Boolean hasZone) {
        this.hasZone = hasZone;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
