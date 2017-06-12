package entities.works;

public class AdminSanctionDetails {

    private String administrationSanctionNumber;
    private String adminSanctionDate;
    private String adminSanctionAuthority;

    public String getAdminSanctionAuthority() {
        return adminSanctionAuthority;
    }

    public void setAdminSanctionAuthority(String adminSanctionAuthority) {
        this.adminSanctionAuthority = adminSanctionAuthority;
    }

    public String getAdministrationSanctionNumber() {
        return administrationSanctionNumber;
    }

    public void setAdministrationSanctionNumber(String administrationSanctionNumber) {
        this.administrationSanctionNumber = administrationSanctionNumber;
    }

    public String getAdminSanctionDate() {
        return adminSanctionDate;
    }

    public void setAdminSanctionDate(String adminSanctionDate) {
        this.adminSanctionDate = adminSanctionDate;
    }
}
