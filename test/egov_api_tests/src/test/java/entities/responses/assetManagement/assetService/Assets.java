package entities.responses.assetManagement.assetService;

public class Assets {
    private String code;
    private AssetCategory assetCategory;
    private String length;
    private String description;
    private String modeOfAcquisition;
    private String accumulatedDepreciation;
    private String dateOfCreation;
    private String assetDetails;
    private String grossValue;
    private String tenantId;
    private String name;
    private LocationDetails locationDetails;
    private String width;
    private int id;
    private Department department;
    private String remarks;
    private Properties properties;
    private String status;
    private String totalArea;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getLength() {
        return this.length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModeOfAcquisition() {
        return this.modeOfAcquisition;
    }

    public void setModeOfAcquisition(String modeOfAcquisition) {
        this.modeOfAcquisition = modeOfAcquisition;
    }

    public String getAccumulatedDepreciation() {
        return this.accumulatedDepreciation;
    }

    public void setAccumulatedDepreciation(String accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public String getDateOfCreation() {
        return this.dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getAssetDetails() {
        return this.assetDetails;
    }

    public void setAssetDetails(String assetDetails) {
        this.assetDetails = assetDetails;
    }

    public String getGrossValue() {
        return this.grossValue;
    }

    public void setGrossValue(String grossValue) {
        this.grossValue = grossValue;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDetails getLocationDetails() {
        return this.locationDetails;
    }

    public void setLocationDetails(LocationDetails locationDetails) {
        this.locationDetails = locationDetails;
    }

    public String getWidth() {
        return this.width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalArea() {
        return this.totalArea;
    }

    public void setTotalArea(String totalArea) {
        this.totalArea = totalArea;
    }
}
