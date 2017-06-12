package entities.sewerageTax;

public class ConnectionDetails {

    String propertyType;
    String numOfClosetsForResidential;
    String numOfClosetsForNonResidential;
    String documentNum;

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getNumOfClosetsForResidential() {
        return numOfClosetsForResidential;
    }

    public void setNumOfClosetsForResidential(String numOfClosetsForResidential) {
        this.numOfClosetsForResidential = numOfClosetsForResidential;
    }

    public String getNumOfClosetsForNonResidential() {
        return numOfClosetsForNonResidential;
    }

    public void setNumOfClosetsForNonResidential(String numOfClosetsForNonResidential) {
        this.numOfClosetsForNonResidential = numOfClosetsForNonResidential;
    }

    public String getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(String documentNum) {
        this.documentNum = documentNum;
    }
}
