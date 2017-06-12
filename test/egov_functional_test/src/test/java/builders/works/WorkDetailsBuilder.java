package builders.works;

import entities.works.WorkDetails;

public class WorkDetailsBuilder {

    WorkDetails workDetails = new WorkDetails();

    public WorkDetailsBuilder withWorksOrderCreated(Boolean worksOrderCreated) {
        workDetails.setWorksorderCreated(worksOrderCreated);
        return this;
    }

    public WorkDetailsBuilder withBillsCreated(Boolean billsCreated) {
        workDetails.setBillsCreated(billsCreated);
        return this;
    }

    public WorkDetailsBuilder withNameOfWork(String nameOfWork) {
        workDetails.setNameOfWork(nameOfWork);
        return this;
    }

    public WorkDetailsBuilder withAbstarctEstimateNumber(String abstarctEstimateNumber) {
        workDetails.setAbstractEstimateNumber(abstarctEstimateNumber);
        return this;
    }

    public WorkDetailsBuilder withEstimatedAmount(String estimatedAmount) {
        workDetails.setEstimatedAmount(estimatedAmount);
        return this;
    }

    public WorkDetailsBuilder withWorkIdentificationNumber(String workIdentificationNumber) {
        workDetails.setWorkIdentificationNumber(workIdentificationNumber);
        return this;
    }

    public WorkDetailsBuilder withActualEstimateAmount(String actualEstimateAmount) {
        workDetails.setActualEstimateAmount(actualEstimateAmount);
        return this;
    }

    public WorkDetailsBuilder withGrossAmountBilled(String grossAmountBilled) {
        workDetails.setGrossAmountBilled(grossAmountBilled);
        return this;
    }

    public WorkDetailsBuilder withQuantity(String quantity) {
        workDetails.setQuantity(quantity);
        return this;
    }

    public WorkDetailsBuilder withUOM(String uom) {
        workDetails.setUom(uom);
        return this;
    }

    public WorkDetailsBuilder withExpectedOutcome(String expectedOutcome) {
        workDetails.setExpectedOutcome(expectedOutcome);
        return this;
    }

    public WorkDetails build() {
        return workDetails;
    }
}
