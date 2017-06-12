package entities.responses.lams;

public class AgreementsRentIncrementMethod {
    private Object fromDate;
    private Object flatAmount;
    private Object assetCategory;
    private Object toDate;
    private Object percentage;
    private int id;
    private Object type;

    public Object getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(Object fromDate) {
        this.fromDate = fromDate;
    }

    public Object getFlatAmount() {
        return this.flatAmount;
    }

    public void setFlatAmount(Object flatAmount) {
        this.flatAmount = flatAmount;
    }

    public Object getAssetCategory() {
        return this.assetCategory;
    }

    public void setAssetCategory(Object assetCategory) {
        this.assetCategory = assetCategory;
    }

    public Object getToDate() {
        return this.toDate;
    }

    public void setToDate(Object toDate) {
        this.toDate = toDate;
    }

    public Object getPercentage() {
        return this.percentage;
    }

    public void setPercentage(Object percentage) {
        this.percentage = percentage;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getType() {
        return this.type;
    }

    public void setType(Object type) {
        this.type = type;
    }
}
