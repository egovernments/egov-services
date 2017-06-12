package builders.wcms;

import entities.wcms.EnclosedDocument;

public class EnclosedDocumentBuilder {

    EnclosedDocument enclosedDocument = new EnclosedDocument();

    public EnclosedDocumentBuilder withDocumentNo1(String documentNo) {
        enclosedDocument.setDocumentN01(documentNo);
        return this;
    }

    public EnclosedDocumentBuilder withDocumentNo2(String documentNo) {
        enclosedDocument.setDocumentN02(documentNo);
        return this;
    }

    public EnclosedDocumentBuilder withDocumentNo3(String documentNo) {
        enclosedDocument.setDocumentN03(documentNo);
        return this;
    }

    public EnclosedDocumentBuilder withDocumentDate1(String documentDate) {
        enclosedDocument.setDocumentDate1(documentDate);
        return this;
    }

    public EnclosedDocumentBuilder withDocumentDate2(String documentDate) {
        enclosedDocument.setDocumentDate2(documentDate);
        return this;
    }

    public EnclosedDocumentBuilder withDocumentDate3(String documentDate) {
        enclosedDocument.setDocumentDate3(documentDate);
        return this;
    }

    public EnclosedDocument build() {
        return enclosedDocument;
    }
}
