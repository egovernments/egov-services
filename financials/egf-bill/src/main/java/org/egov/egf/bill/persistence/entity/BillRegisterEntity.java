package org.egov.egf.bill.persistence.entity;

import org.egov.egf.bill.domain.model.AuditDetails;
import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.Department;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRegisterEntity {
    public static final String TABLE_NAME = "egf_billregister";
    public static final String SEQUENCE_NAME = "seq_egf_billregister";
    private String tenantId;
    private String billType;
    private String billSubType;
    private String billNumber;
    private Long billDate;
    private Double billAmount;
    private Double passedAmount;
    private String moduleName;
    private String status;
    private String fund;
    private String function;
    private String fundsource;
    private String scheme;
    private String subScheme;
    private String functionary;
    private String division;
    private String department;
    private String sourcePath;
    private Boolean budgetCheckRequired;
    private String budgetAppropriationNo;
    private String partyBillNumber;
    private Long partyBillDate;
    private String description;
    private String createdBy;
    private String lastModifiedBy;
    private Long createdTime;
    private Long lastModifiedTime;

    public BillRegister toDomain() {
        final BillRegister billRegister = new BillRegister();
        billRegister.setTenantId(tenantId);
        billRegister.setBillType(billType);
        billRegister.setBillSubType(billSubType);
        billRegister.setBillNumber(billNumber);
        billRegister.setBillDate(billDate);
        billRegister.setBillAmount(billAmount);
        billRegister.setPassedAmount(passedAmount);
        billRegister.setModuleName(moduleName);
        billRegister.setStatus(FinancialStatusContract.builder().id(status).build());
        billRegister.setFund(FundContract.builder().id(fund).build());
        billRegister.setFunction(FunctionContract.builder().id(function).build());
        billRegister.setFundsource(FundsourceContract.builder().id(fundsource).build());
        billRegister.setScheme(SchemeContract.builder().id(scheme).build());
        billRegister.setSubScheme(SubSchemeContract.builder().id(subScheme).build());
        billRegister.setFunctionary(FunctionaryContract.builder().id(functionary).build());
        billRegister.setDivision(Boundary.builder().id(division).build());
        billRegister.setDepartment(Department.builder().id(department).build());
        billRegister.setSourcePath(sourcePath);
        billRegister.setBudgetCheckRequired(budgetCheckRequired);
        billRegister.setBudgetAppropriationNo(budgetAppropriationNo);
        billRegister.setPartyBillNumber(partyBillNumber);
        billRegister.setPartyBillDate(partyBillDate);
        billRegister.setDescription(description);
        billRegister.setAuditDetails(new AuditDetails());
        billRegister.getAuditDetails().setCreatedBy(createdBy);
        billRegister.getAuditDetails().setCreatedTime(createdTime);
        billRegister.getAuditDetails().setLastModifiedBy(lastModifiedBy);
        billRegister.getAuditDetails().setLastModifiedTime(lastModifiedTime);
        return billRegister;
    }

    public BillRegisterEntity toEntity(final BillRegister billRegister) {
        tenantId = billRegister.getTenantId();
        billType = billRegister.getBillType();
        billSubType = billRegister.getBillSubType();
        billNumber = billRegister.getBillNumber();
        billDate = billRegister.getBillDate();
        billAmount = billRegister.getBillAmount();
        passedAmount = billRegister.getPassedAmount();
        moduleName = billRegister.getModuleName();
        status = billRegister.getStatus() != null ? billRegister.getStatus().getId() : null;
        fund = billRegister.getFund() != null ? billRegister.getFund().getId() : null;
        function = billRegister.getFunction() != null ? billRegister.getFunction().getId() : null;
        fundsource = billRegister.getFundsource() != null ? billRegister.getFundsource().getId() : null;
        scheme = billRegister.getScheme() != null ? billRegister.getScheme().getId() : null;
        subScheme = billRegister.getSubScheme() != null ? billRegister.getSubScheme().getId() : null;
        functionary = billRegister.getFunctionary() != null ? billRegister.getFunctionary().getId() : null;
        division = billRegister.getDivision() != null ? billRegister.getDivision().getId() : null;
        department = billRegister.getDepartment() != null ? billRegister.getDepartment().getId() : null;
        sourcePath = billRegister.getSourcePath();
        budgetCheckRequired = billRegister.getBudgetCheckRequired();
        budgetAppropriationNo = billRegister.getBudgetAppropriationNo();
        partyBillNumber = billRegister.getPartyBillNumber();
        partyBillDate = billRegister.getPartyBillDate();
        description = billRegister.getDescription();
        createdBy = billRegister.getAuditDetails() != null ? billRegister.getAuditDetails().getCreatedBy() : null;
        createdTime = billRegister.getAuditDetails() != null ? billRegister.getAuditDetails().getCreatedTime() : null;
        lastModifiedBy = billRegister.getAuditDetails() != null ? billRegister.getAuditDetails().getLastModifiedBy() : null;
        lastModifiedTime = billRegister.getAuditDetails() != null ? billRegister.getAuditDetails().getLastModifiedTime() : null;
        return this;
    }
}
