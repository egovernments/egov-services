package entities.ptis;

public class AssessmentDetails {

    private String reasonForCreation;
    private String extentOfSite;
    private String occupancyCertificateNumber;
    private String bifurcationReasonForCreation;
    private String ParentAssessmentNo;

    public String getReasonForCreation() {
        return reasonForCreation;
    }

    public void setReasonForCreation(String reasonForCreation) {
        this.reasonForCreation = reasonForCreation;
    }

    public String getExtentOfSite() {
        return extentOfSite;
    }

    public void setExtentOfSite(String extentOfSite) {
        this.extentOfSite = extentOfSite;
    }

    public String getOccupancyCertificateNumber() {
        return occupancyCertificateNumber;
    }

    public void setOccupancyCertificateNumber(String occupancyCertificateNumber) {
        this.occupancyCertificateNumber = occupancyCertificateNumber;
    }

    public String getBifurcationReasonForCreation() {
        return bifurcationReasonForCreation;
    }

    public void setBifurcationReasonForCreation(String bifurcationReasonForCreation) {
        this.bifurcationReasonForCreation = bifurcationReasonForCreation;
    }

    public String getParentAssessmentNo() {
        return ParentAssessmentNo;
    }

    public void setParentAssessmentNo(String parentAssessmentNo) {
        ParentAssessmentNo = parentAssessmentNo;
    }
}
