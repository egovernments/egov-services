package entities.responses.lams;

public class AgreementsAsset {
    private String code;
    private AgreementsAssetAssetCategory assetCategory;
    private String name;
    private AgreementsAssetLocationDetails locationDetails;
    private int id;
    private Object doorNo;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AgreementsAssetAssetCategory getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(AgreementsAssetAssetCategory assetCategory) {
        this.assetCategory = assetCategory;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AgreementsAssetLocationDetails getLocationDetails() {
        return this.locationDetails;
    }

    public void setLocationDetails(AgreementsAssetLocationDetails locationDetails) {
        this.locationDetails = locationDetails;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getDoorNo() {
        return this.doorNo;
    }

    public void setDoorNo(Object doorNo) {
        this.doorNo = doorNo;
    }
}
