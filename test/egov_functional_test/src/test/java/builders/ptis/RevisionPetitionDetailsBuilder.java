package builders.ptis;

import entities.ptis.RevisionPetitionDetails;

public class RevisionPetitionDetailsBuilder {

    RevisionPetitionDetails revisionPetitionDetails = new RevisionPetitionDetails();

    public RevisionPetitionDetailsBuilder withRevisionPetitionDetail(String revisionPetitionDetail) {
        revisionPetitionDetails.setRevisionPetitionDetail(revisionPetitionDetail);
        return this;
    }

    public RevisionPetitionDetails build() {
        return revisionPetitionDetails;
    }
}
