package builders.leaseAndAgreement;

import entities.leaseAndAgreement.LandAgreementDetails;

public final class LandAgreementDetailsBuilder {

    LandAgreementDetails landAgreementDetails = new LandAgreementDetails();

    public LandAgreementDetailsBuilder withTenderNumber(String tenderNumber) {
        landAgreementDetails.setTenderNumber(tenderNumber);
        return this;
    }

    public LandAgreementDetailsBuilder withTenderDate(String tenderDate) {
        landAgreementDetails.setTenderDate(tenderDate);

        return this;
    }

    public LandAgreementDetailsBuilder withNatureOfAllotment(String natureOfAllotment) {
        landAgreementDetails.setNatureOfAllotment(natureOfAllotment);
        return this;
    }

    public LandAgreementDetailsBuilder withCouncilNumber(String councilNumber) {
        landAgreementDetails.setCouncilNumber(councilNumber);
        return this;
    }

    public LandAgreementDetailsBuilder withCouncilDate(String councilDate) {
        landAgreementDetails.setCouncilDate(councilDate);
        return this;
    }

    public LandAgreementDetailsBuilder withLandRent(String landRent) {
        landAgreementDetails.setLandRent(landRent);
        return this;
    }

    public LandAgreementDetailsBuilder withPaymentCycle(String paymentCycle) {
        landAgreementDetails.setPaymentCycle(paymentCycle);
        return this;
    }

    public LandAgreementDetailsBuilder withBankGuaranteeAmount(String bankGuaranteeAmount) {
        landAgreementDetails.setBankGuaranteeAmount(bankGuaranteeAmount);
        return this;
    }

    public LandAgreementDetailsBuilder withBankGuaranteeDate(String bankGuaranteeDate) {
        landAgreementDetails.setBankGuaranteeDate(bankGuaranteeDate);
        return this;
    }

    public LandAgreementDetailsBuilder withSolvencyCertificateNumber(String solvencyCertificateNumber) {
        landAgreementDetails.setSolvencyCertificateNumber(solvencyCertificateNumber);
        return this;
    }

    public LandAgreementDetailsBuilder withSolvencyCertificateDate(String solvencyCertificateDate) {
        landAgreementDetails.setSolvencyCertificateDate(solvencyCertificateDate);
        return this;
    }

    public LandAgreementDetailsBuilder withSecurityDeposit(String securityDeposit) {
        landAgreementDetails.setSecurityDeposit(securityDeposit);
        return this;
    }

    public LandAgreementDetailsBuilder withSecurityDepositDate(String securityDepositDate) {
        landAgreementDetails.setSecurityDepositDate(securityDepositDate);
        return this;
    }

    public LandAgreementDetailsBuilder withCommencementDate(String commencementDate) {
        landAgreementDetails.setCommencementDate(commencementDate);
        return this;
    }

    public LandAgreementDetailsBuilder withRentIncrementMethod(String rentIncrementMethod) {
        landAgreementDetails.setRentIncrementMethod(rentIncrementMethod);
        return this;
    }

    public LandAgreementDetailsBuilder withTimePeriod(String timePeriod) {
        landAgreementDetails.setTimePeriod(timePeriod);
        return this;
    }

    public LandAgreementDetails build() {
        return landAgreementDetails;
    }
}
