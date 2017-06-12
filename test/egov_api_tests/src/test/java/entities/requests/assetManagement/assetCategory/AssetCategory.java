package entities.requests.assetManagement.assetCategory;

import org.codehaus.jackson.annotate.JsonProperty;

public class AssetCategory {
    private String assetCategoryType;

    private String tenantId;

    private String[] depreciationRate;

    private String depreciationMethod;

    private String unitOfMeasurement;

    private String revaluationReserveAccount;

    private String accumulatedDepreciationAccount;

    @JsonProperty("CustomFields")
    private CustomFields[] customFields;

    private String depreciationExpenseAccount;

    private String name;

    private String parent;

    private String assetAccount;

    public String getAssetCategoryType() {
        return assetCategoryType;
    }

    public void setAssetCategoryType(String assetCategoryType) {
        this.assetCategoryType = assetCategoryType;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String[] getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(String[] depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public String getRevaluationReserveAccount() {
        return revaluationReserveAccount;
    }

    public void setRevaluationReserveAccount(String revaluationReserveAccount) {
        this.revaluationReserveAccount = revaluationReserveAccount;
    }

    public String getAccumulatedDepreciationAccount() {
        return accumulatedDepreciationAccount;
    }

    public void setAccumulatedDepreciationAccount(String accumulatedDepreciationAccount) {
        this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
    }

    public CustomFields[] getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomFields[] customFields) {
        this.customFields = customFields;
    }

    public String getDepreciationExpenseAccount() {
        return depreciationExpenseAccount;
    }

    public void setDepreciationExpenseAccount(String depreciationExpenseAccount) {
        this.depreciationExpenseAccount = depreciationExpenseAccount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(String assetAccount) {
        this.assetAccount = assetAccount;
    }

}
