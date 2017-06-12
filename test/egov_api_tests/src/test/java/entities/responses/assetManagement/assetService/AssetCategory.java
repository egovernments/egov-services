package entities.responses.assetManagement.assetService;

public class AssetCategory {
    private String unitOfMeasurement;
    private String parent;
    private String code;
    private String assetAccount;
    private String depreciationMethod;
    private String customFields;
    private String assetCategoryType;
    private String depreciationExpenseAccount;
    private String revaluationReserveAccount;
    private String tenantId;
    private String name;
    private int id;
    private String depreciationRate;
    private String accumulatedDepreciationAccount;

    public String getUnitOfMeasurement() {
        return this.unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAssetAccount() {
        return this.assetAccount;
    }

    public void setAssetAccount(String assetAccount) {
        this.assetAccount = assetAccount;
    }

    public String getDepreciationMethod() {
        return this.depreciationMethod;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public String getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }

    public String getAssetCategoryType() {
        return this.assetCategoryType;
    }

    public void setAssetCategoryType(String assetCategoryType) {
        this.assetCategoryType = assetCategoryType;
    }

    public String getDepreciationExpenseAccount() {
        return this.depreciationExpenseAccount;
    }

    public void setDepreciationExpenseAccount(String depreciationExpenseAccount) {
        this.depreciationExpenseAccount = depreciationExpenseAccount;
    }

    public String getRevaluationReserveAccount() {
        return this.revaluationReserveAccount;
    }

    public void setRevaluationReserveAccount(String revaluationReserveAccount) {
        this.revaluationReserveAccount = revaluationReserveAccount;
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepreciationRate() {
        return this.depreciationRate;
    }

    public void setDepreciationRate(String depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getAccumulatedDepreciationAccount() {
        return this.accumulatedDepreciationAccount;
    }

    public void setAccumulatedDepreciationAccount(String accumulatedDepreciationAccount) {
        this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
    }
}
