package entities.wcms;

public class FieldInspectionDetails {

    private String material;
    private String quantity;
    private String unitOfMeasurement;
    private String rate;
    private String existingDistributionPipeline;
    private String pipelineToHomeDistance;
    private String estimationCharges;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getExistingDistributionPipeline() {
        return existingDistributionPipeline;
    }

    public void setExistingDistributionPipeline(String existingDistributionPipeline) {
        this.existingDistributionPipeline = existingDistributionPipeline;
    }

    public String getPipelineToHomeDistance() {
        return pipelineToHomeDistance;
    }

    public void setPipelineToHomeDistance(String pipelineToHomeDistance) {
        this.pipelineToHomeDistance = pipelineToHomeDistance;
    }

    public String getEstimationCharges() {
        return estimationCharges;
    }

    public void setEstimationCharges(String estimationCharges) {
        this.estimationCharges = estimationCharges;
    }
}
