package builders.lcms;

import entities.lcms.CreateLegalCase;

public class CreateLegalCaseBuilder {

    CreateLegalCase createLegalCase = new CreateLegalCase();

    public CreateLegalCaseBuilder withTypeOfCourt(String typeOfCourt) {
        createLegalCase.setTypeOfCourt(typeOfCourt);
        return this;
    }

    public CreateLegalCaseBuilder withPetitionType(String petitionType) {
        createLegalCase.setPetitionType(petitionType);
        return this;
    }

    public CreateLegalCaseBuilder withCourtName(String courtName) {
        createLegalCase.setCourtName(courtName);
        return this;
    }

    public CreateLegalCaseBuilder withCaseCategory(String caseCategory) {
        createLegalCase.setCaseCategory(caseCategory);
        return this;
    }

    public CreateLegalCaseBuilder withPetitionerName(String petitionerName) {
        createLegalCase.setPetitionerName(petitionerName);
        return this;
    }

    public CreateLegalCaseBuilder withRespondentName(String respondentName) {
        createLegalCase.setRespondentName(respondentName);
        return this;
    }

    public CreateLegalCase build() {
        return createLegalCase;
    }
}


