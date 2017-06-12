package entities.ptis;

public class DocumentTypeValue {

    private String DocumentType;
    private String deedNo;
    private String deedDate;

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public String getDeedNo() {
        return deedNo;
    }

    public void setDeedNo(String deedNo) {
        this.deedNo = deedNo;
    }

    public String getDeedDate() {
        return deedDate;
    }

    public void setDeedDate(String deedDate) {
        this.deedDate = deedDate;
    }
}
