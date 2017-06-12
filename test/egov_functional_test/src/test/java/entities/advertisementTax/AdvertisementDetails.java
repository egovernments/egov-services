package entities.advertisementTax;

public class AdvertisementDetails {

    private String category;
    private String subCategory;
    private String classType;
    private String revenueInspector;
    private String propertyType;

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getRevenueInspector() {
        return revenueInspector;
    }

    public void setRevenueInspector(String revenueInspector) {
        this.revenueInspector = revenueInspector;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
}
