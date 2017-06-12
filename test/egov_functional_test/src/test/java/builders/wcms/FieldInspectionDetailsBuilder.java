package builders.wcms;

import entities.wcms.FieldInspectionDetails;

public class FieldInspectionDetailsBuilder {

    FieldInspectionDetails fieldInspectionDetails = new FieldInspectionDetails();

    public FieldInspectionDetailsBuilder withMaterial(String assessmentNumber) {
        fieldInspectionDetails.setMaterial(assessmentNumber);
        return this;
    }

    public FieldInspectionDetailsBuilder withQuantity(String quantity) {
        fieldInspectionDetails.setQuantity(quantity);
        return this;
    }

    public FieldInspectionDetailsBuilder withUnitOfMeasurement(String unitOfMeasurement) {
        fieldInspectionDetails.setUnitOfMeasurement(unitOfMeasurement);
        return this;
    }

    public FieldInspectionDetailsBuilder withRate(String rate) {
        fieldInspectionDetails.setRate(rate);
        return this;
    }

    public FieldInspectionDetailsBuilder withExistingDistributionPipeLine(String existingDistributionPipeLine) {
        fieldInspectionDetails.setExistingDistributionPipeline(existingDistributionPipeLine);
        return this;
    }

    public FieldInspectionDetailsBuilder withPipelineToHomeDistance(String pipelineToHomeDistance) {
        fieldInspectionDetails.setPipelineToHomeDistance(pipelineToHomeDistance);
        return this;
    }

    public FieldInspectionDetailsBuilder withEstimationCharges(String estimationCharges) {
        fieldInspectionDetails.setEstimationCharges(estimationCharges);
        return this;
    }

    public FieldInspectionDetails build() {
        return fieldInspectionDetails;
    }
}
