package org.egov.egf.bill.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRegisterEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_billregister";
    public static final String SEQUENCE_NAME = "seq_egf_billregister";
    private String id;
    private String billType;
    private String billSubType;
    private String billNumber;
    private Date billDate;
    private BigDecimal billAmount;
    private BigDecimal passedAmount;
    private String moduleName;
    private String statusId;
    private String fundId;
    private String functionId;
    private String fundsourceId;
    private String schemeId;
    private String subSchemeId;
    private String functionaryId;
    private String divisionId;
    private String departmentId;
    private String sourcePath;
    private Boolean budgetCheckRequired;
    private String budgetAppropriationNo;
    private String partyBillNumber;
    private Date partyBillDate;
    private String description;

    public BillRegister toDomain() {
	BillRegister billRegister = new BillRegister();
	super.toDomain(billRegister);
	billRegister.setId(this.id);
	billRegister.setBillType(this.billType);
	billRegister.setBillSubType(this.billSubType);
	billRegister.setBillNumber(this.billNumber);
	billRegister.setBillDate(this.billDate);
	billRegister.setBillAmount(this.billAmount);
	billRegister.setPassedAmount(this.passedAmount);
	billRegister.setModuleName(this.moduleName);
	billRegister.setStatus(FinancialStatusContract.builder().id(statusId).build());
	billRegister.setFund(FundContract.builder().id(fundId).build());
	billRegister.setFunction(FunctionContract.builder().id(functionId).build());
	billRegister.setFundsource(FundsourceContract.builder().id(fundsourceId).build());
	billRegister.setScheme(SchemeContract.builder().id(schemeId).build());
	billRegister.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
	billRegister.setFunctionary(FunctionaryContract.builder().id(functionaryId).build());
	billRegister.setDivision(Boundary.builder().id(divisionId).build());
	billRegister.setDepartment(Department.builder().id(departmentId).build());
	billRegister.setSourcePath(this.sourcePath);
	billRegister.setBudgetCheckRequired(this.budgetCheckRequired);
	billRegister.setBudgetAppropriationNo(this.budgetAppropriationNo);
	billRegister.setPartyBillNumber(this.partyBillNumber);
	billRegister.setPartyBillDate(this.partyBillDate);
	billRegister.setDescription(this.description);
	return billRegister;
    }

    public BillRegisterEntity toEntity(BillRegister billRegister) {
	super.toEntity((Auditable) billRegister);
	this.id = billRegister.getId();
	this.billType = billRegister.getBillType();
	this.billSubType = billRegister.getBillSubType();
	this.billNumber = billRegister.getBillNumber();
	this.billDate = billRegister.getBillDate();
	this.billAmount = billRegister.getBillAmount();
	this.passedAmount = billRegister.getPassedAmount();
	this.moduleName = billRegister.getModuleName();
	this.statusId = billRegister.getStatus() != null ? billRegister.getStatus().getId() : null;
	this.fundId = billRegister.getFund() != null ? billRegister.getFund().getId() : null;
	this.functionId = billRegister.getFunction() != null ? billRegister.getFunction().getId() : null;
	this.fundsourceId = billRegister.getFundsource() != null ? billRegister.getFundsource().getId() : null;
	this.schemeId = billRegister.getScheme() != null ? billRegister.getScheme().getId() : null;
	this.subSchemeId = billRegister.getSubScheme() != null ? billRegister.getSubScheme().getId() : null;
	this.functionaryId = billRegister.getFunctionary() != null ? billRegister.getFunctionary().getId() : null;
	this.divisionId = billRegister.getDivision() != null ? billRegister.getDivision().getId() : null;
	this.departmentId = billRegister.getDepartment() != null ? billRegister.getDepartment().getId() : null;
	this.sourcePath = billRegister.getSourcePath();
	this.budgetCheckRequired = billRegister.getBudgetCheckRequired();
	this.budgetAppropriationNo = billRegister.getBudgetAppropriationNo();
	this.partyBillNumber = billRegister.getPartyBillNumber();
	this.partyBillDate = billRegister.getPartyBillDate();
	this.description = billRegister.getDescription();
	return this;
    }
}
