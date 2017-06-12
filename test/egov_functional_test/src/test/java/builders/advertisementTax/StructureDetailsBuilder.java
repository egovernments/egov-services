package builders.advertisementTax;

import entities.advertisementTax.StructureDetails;

public class StructureDetailsBuilder {

    StructureDetails structureDetails = new StructureDetails();

    public StructureDetailsBuilder withMeasurement(String measurement) {
        structureDetails.setMeasurement(measurement);
        return this;
    }

    public StructureDetailsBuilder withMeasurementType(String measurementType) {
        structureDetails.setMeasurementType(measurementType);
        return this;
    }

    public StructureDetailsBuilder withTaxAmount(String taxAmount) {
        structureDetails.setTaxAmount(taxAmount);
        return this;
    }

    public StructureDetails build() {
        return structureDetails;
    }
}
