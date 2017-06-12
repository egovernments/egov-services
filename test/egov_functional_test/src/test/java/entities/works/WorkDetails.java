package entities.works;

public class WorkDetails {

    private Boolean worksorderCreated;
    private Boolean billsCreated;
    private String nameOfWork;
    private String abstractEstimateNumber;
    private String estimatedAmount;
    private String workIdentificationNumber;
    private String actualEstimateAmount;
    private String grossAmountBilled;
    private String quantity;
    private String uom;
    private String expectedOutcome;

    public Boolean getWorksorderCreated() {
        return worksorderCreated;
    }

    public void setWorksorderCreated(Boolean worksorderCreated) {
        this.worksorderCreated = worksorderCreated;
    }

    public Boolean getBillsCreated() {
        return billsCreated;
    }

    public void setBillsCreated(Boolean billsCreated) {
        this.billsCreated = billsCreated;
    }

    public String getNameOfWork() {
        return nameOfWork;
    }

    public void setNameOfWork(String nameOfWork) {
        this.nameOfWork = nameOfWork;
    }

    public String getAbstractEstimateNumber() {
        return abstractEstimateNumber;
    }

    public void setAbstractEstimateNumber(String abstractEstimateNumber) {
        this.abstractEstimateNumber = abstractEstimateNumber;
    }

    public String getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(String estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

    public String getActualEstimateAmount() {
        return actualEstimateAmount;
    }

    public void setActualEstimateAmount(String actualEstimateAmount) {
        this.actualEstimateAmount = actualEstimateAmount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getExpectedOutcome() {
        return expectedOutcome;
    }

    public void setExpectedOutcome(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    public String getGrossAmountBilled() {
        return grossAmountBilled;
    }

    public void setGrossAmountBilled(String grossAmountBilled) {
        this.grossAmountBilled = grossAmountBilled;
    }
}
