package entities.assetManagement;


public class AssetCategoryDetails {

    private String name;
    private String assetCategoryType;
    private String parentCategory;
    private String depreciationMethod;
    private String assetAccountCode;
    private String accumulatedDepreciationCode;
    private String revaluationReserveAccountCode;
    private String depreciationExpenceAccount;
    private String UOM;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetCategoryType() {
        return assetCategoryType;
    }

    public void setAssetCategoryType(String assetCategoryType) {
        this.assetCategoryType = assetCategoryType;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getDepreciationMethod() {
        return depreciationMethod;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        this.depreciationMethod = depreciationMethod;
    }

    public String getAssetAccountCode() {
        return assetAccountCode;
    }

    public void setAssetAccountCode(String assetAccountCode) {
        this.assetAccountCode = assetAccountCode;
    }

    public String getAccumulatedDepreciationCode() {
        return accumulatedDepreciationCode;
    }

    public void setAccumulatedDepreciationCode(String accumulatedDepreciationCode) {
        this.accumulatedDepreciationCode = accumulatedDepreciationCode;
    }

    public String getRevaluationReserveAccountCode() {
        return revaluationReserveAccountCode;
    }

    public void setRevaluationReserveAccountCode(String revaluationReserveAccountCode) {
        this.revaluationReserveAccountCode = revaluationReserveAccountCode;
    }

    public String getDepreciationExpenceAccount() {
        return depreciationExpenceAccount;
    }

    public void setDepreciationExpenceAccount(String depreciationExpenceAccount) {
        this.depreciationExpenceAccount = depreciationExpenceAccount;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }
}
