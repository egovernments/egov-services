package entities.lcms;

public class CreateLegalCase {

    private String typeOfCourt;
    private String petitionType;
    private String courtName;
    private String caseCategory;
    private String petitionerName;
    private String respondentName;

    public String getTypeOfCourt() {
        return typeOfCourt;
    }

    public void setTypeOfCourt(String typeOfCourt) {
        this.typeOfCourt = typeOfCourt;
    }

    public String getPetitionType() {
        return petitionType;
    }

    public void setPetitionType(String petitionType) {
        this.petitionType = petitionType;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(String caseCategory) {
        this.caseCategory = caseCategory;
    }

    public String getPetitionerName() {
        return petitionerName;
    }

    public void setPetitionerName(String petitionerName) {
        this.petitionerName = petitionerName;
    }

    public String getRespondentName() {
        return respondentName;
    }

    public void setRespondentName(String respondentName) {
        this.respondentName = respondentName;
    }
}
