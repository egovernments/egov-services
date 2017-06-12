package entities.tradeLicense;

public class TradeLocationDetails {

    private String propertyAssessmentNumber;
    private String ownershipType;
    private String locality;
    private String ward;

    public String getpropertyAssessmentNumber() {
        return propertyAssessmentNumber;
    }

    public void setPropertyAssessmentNumber(String propertyAssessmentNumber) {
        this.propertyAssessmentNumber = propertyAssessmentNumber;
    }

    public String getownershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }
}
