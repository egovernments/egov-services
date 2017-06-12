package builders.works;

import entities.works.EstimateHeaderDetails;

public class EstimateHeaderDetailsBuilder {
    EstimateHeaderDetails estimateHeaderDetails = new EstimateHeaderDetails();

    public EstimateHeaderDetailsBuilder withDate(String date) {
        estimateHeaderDetails.setDate(date);
        return this;
    }

    public EstimateHeaderDetailsBuilder withSubject(String subject) {
        estimateHeaderDetails.setSubject(subject);
        return this;
    }

    public EstimateHeaderDetailsBuilder withRequirementNumber(String requirement) {
        estimateHeaderDetails.setRequirementNumber(requirement);
        return this;
    }

    public EstimateHeaderDetailsBuilder withDescription(String description) {
        estimateHeaderDetails.setDescription(description);
        return this;
    }

    public EstimateHeaderDetailsBuilder withElectionWard(String electionWard) {
        estimateHeaderDetails.setElectionWard(electionWard);
        return this;
    }

    public EstimateHeaderDetailsBuilder withLocation(String location) {
        estimateHeaderDetails.setLocation(location);
        return this;
    }

    public EstimateHeaderDetailsBuilder withWorkCategory(String workCategory) {
        estimateHeaderDetails.setWorkCategory(workCategory);
        return this;
    }

    public EstimateHeaderDetailsBuilder withBeneficiary(String beneficiary) {
        estimateHeaderDetails.setBeneficiary(beneficiary);
        return this;
    }

    public EstimateHeaderDetailsBuilder withNatureOfWork(String natureOfWork) {
        estimateHeaderDetails.setNatureOfWork(natureOfWork);
        return this;
    }

    public EstimateHeaderDetailsBuilder withTypeOfWork(String typeOfWork) {
        estimateHeaderDetails.setTypeOfWork(typeOfWork);
        return this;
    }

    public EstimateHeaderDetailsBuilder withSubTypeOfWork(String subTypeOfWork) {
        estimateHeaderDetails.setSubTypeOfWork(subTypeOfWork);
        return this;
    }

    public EstimateHeaderDetailsBuilder withModeOfEntrustment(String modeOfEntrustment) {
        estimateHeaderDetails.setModeOfEntrustment(modeOfEntrustment);
        return this;
    }

    public EstimateHeaderDetails build() {
        return estimateHeaderDetails;
    }
}