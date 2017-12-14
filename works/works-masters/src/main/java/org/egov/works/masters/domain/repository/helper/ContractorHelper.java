package org.egov.works.masters.domain.repository.helper;

import java.math.BigDecimal;

import org.egov.works.masters.web.contract.AuditDetails;
import org.egov.works.masters.web.contract.Bank;
import org.egov.works.masters.web.contract.ChartOfAccount;
import org.egov.works.masters.web.contract.Contractor;
import org.egov.works.masters.web.contract.ContractorClass;
import org.egov.works.masters.web.contract.ContractorExemption;
import org.egov.works.masters.web.contract.ContractorStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContractorHelper {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("correspondenceAddress")
    private String correspondenceAddress = null;

    @JsonProperty("paymentAddress")
    private String paymentAddress = null;

    @JsonProperty("contactPerson")
    private String contactPerson = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("narration")
    private String narration = null;

    @JsonProperty("mobileNumber")
    private BigDecimal mobileNumber = null;

    @JsonProperty("panNumber")
    private String panNumber = null;

    @JsonProperty("tinNumber")
    private String tinNumber = null;

    @JsonProperty("bank")
    private String bank = null;

    @JsonProperty("bankAccountNumber")
    private BigDecimal bankAccountNumber = null;

    @JsonProperty("pwdApprovalCode")
    private String pwdApprovalCode = null;

    @JsonProperty("exemptedFrom")
    private String exemptedFrom = null;

    @JsonProperty("pwdApprovalValidTill")
    private Long pwdApprovalValidTill = null;

    @JsonProperty("epfRegistrationNumber")
    private String epfRegistrationNumber = null;

    @JsonProperty("accountCode")
    private String accountCode = null;

    @JsonProperty("ifscCode")
    private BigDecimal ifscCode = null;

    @JsonProperty("contractorClass")
    private String contractorClass = null;

    @JsonProperty("pmc")
    private Boolean pmc = false;
    
    @JsonProperty("status")
    private String status = null;

    @JsonProperty("createdBy")
    private String createdBy = null;

    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy = null;

    @JsonProperty("createdTime")
    private Long createdTime = null;

    @JsonProperty("lastModifiedTime")
    private Long lastModifiedTime = null;

    public Contractor toDomain() {
        Contractor contractor = new Contractor();
        contractor.setTenantId(this.tenantId);
        contractor.setCode(this.code);
        contractor.setName(this.name);
        contractor.setCorrespondenceAddress(this.correspondenceAddress);
        contractor.setPaymentAddress(this.paymentAddress);
        contractor.setContactPerson(this.contactPerson);
        contractor.setEmail(this.email);
        contractor.setNarration(this.narration);
        contractor.setMobileNumber(this.mobileNumber);
        contractor.setPanNumber(this.panNumber);
        contractor.setTinNumber(this.tinNumber);
        contractor.setBank(new Bank());
        contractor.getBank().setCode(this.bank);
        contractor.setBankAccountNumber(this.bankAccountNumber);
        contractor.setPwdApprovalCode(this.pwdApprovalCode);
        contractor.setExemptedFrom(ContractorExemption.valueOf(this.exemptedFrom));
        contractor.setPwdApprovalValidTill(this.pwdApprovalValidTill);
        contractor.setEpfRegistrationNumber(this.epfRegistrationNumber);
        contractor.setAccountCode(new ChartOfAccount());
        contractor.getAccountCode().setGlcode(this.accountCode);
        contractor.setIfscCode(ifscCode);
        contractor.setContractorClass(new ContractorClass());
        contractor.getContractorClass().setPropertyClass(this.contractorClass);
        contractor.setPmc(this.pmc);
        contractor.setStatus(this.status);
        contractor.setAuditDetails(new AuditDetails());
        contractor.getAuditDetails().setCreatedBy(this.createdBy);
        contractor.getAuditDetails().setCreatedTime(this.createdTime);
        contractor.getAuditDetails().setLastModifiedBy(this.lastModifiedBy);
        contractor.getAuditDetails().setLastModifiedTime(this.lastModifiedTime);
        return contractor;

    }
}
