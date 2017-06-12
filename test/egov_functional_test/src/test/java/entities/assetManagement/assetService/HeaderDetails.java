package entities.assetManagement.assetService;

public class HeaderDetails {

    private String department;

    private String assetCategory;

    private String dateOfCreation;

    private String description;

    private String assetName;

    private String modeOfAcquisition;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAssetCategory() {
        return assetCategory;
    }

    public void setAssetCategory(String assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getModeOfAcquisition() {
        return modeOfAcquisition;
    }

    public void setModeOfAcquisition(String modeOfAcquisition) {
        this.modeOfAcquisition = modeOfAcquisition;
    }
}
