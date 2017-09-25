package org.egov.egf.voucher.persistence.entity;

import java.util.Date;

import org.egov.common.domain.model.Auditable;
import org.egov.common.domain.model.Task;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.voucher.domain.model.Voucher;
import org.egov.egf.voucher.web.contract.Boundary;
import org.egov.egf.voucher.web.contract.Department;

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
public class VoucherEntity extends AuditableEntity {

	public static final String TABLE_NAME = "egf_voucher";

	public static final String SEQUENCE_NAME = "seq_egf_voucher";

	private String id;

	private String type;

	private String name;

	private String description;

	private String voucherNumber;

	private Date voucherDate;

	private String originalVoucherNumber;

	private String refVoucherNumber;

	private String moduleName;

	private String billNumber;

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

	private Boolean budgetCheckRequired = true;

	private String budgetAppropriationNo;

	private String stateId;

	public Voucher toDomain() {

		Voucher domain = new Voucher();

		super.toDomain(domain);

		domain.setId(this.id);
		domain.setType(this.type);
		domain.setName(this.name);
		domain.setDescription(this.description);
		domain.setVoucherNumber(this.voucherNumber);
		domain.setVoucherDate(this.voucherDate);
		domain.setOriginalVoucherNumber(this.originalVoucherNumber);
		domain.setRefVoucherNumber(this.refVoucherNumber);
		domain.setModuleName(this.moduleName);
		domain.setBillNumber(this.billNumber);
		domain.setFund(FundContract.builder().id(fundId).build());
		domain.setStatus(FinancialStatusContract.builder().id(statusId).build());
		domain.setFunction(FunctionContract.builder().id(functionId).build());
		domain.setFundsource(FundsourceContract.builder().id(fundsourceId).build());
		domain.setScheme(SchemeContract.builder().id(schemeId).build());
		domain.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
		domain.setFunctionary(FunctionaryContract.builder().id(functionaryId).build());
		domain.setDivision(Boundary.builder().id(divisionId).build());
		domain.setDepartment(Department.builder().id(departmentId).build());
		domain.setSourcePath(this.sourcePath);
		domain.setBudgetCheckRequired(this.budgetCheckRequired);
		domain.setBudgetAppropriationNo(this.budgetAppropriationNo);
		domain.setState(Task.builder().id(stateId).build());

		return domain;
	}

	public VoucherEntity toEntity(Voucher domain) {

		super.toEntity((Auditable) domain);

		this.id = domain.getId();
		this.type = domain.getType();
		this.name = domain.getName();
		this.description = domain.getDescription();
		this.voucherNumber = domain.getVoucherNumber();
		this.voucherDate = domain.getVoucherDate();
		this.originalVoucherNumber = domain.getOriginalVoucherNumber();
		this.refVoucherNumber = domain.getRefVoucherNumber();
		this.moduleName = domain.getModuleName();
		this.billNumber = domain.getBillNumber();
		this.fundId = domain.getFund() != null ? domain.getFund().getId() : null;
		this.statusId = domain.getStatus() != null ? domain.getStatus().getId() : null;
		this.functionId = domain.getFunction() != null ? domain.getFunction().getId() : null;
		this.fundsourceId = domain.getFundsource() != null ? domain.getFundsource().getId() : null;
		this.schemeId = domain.getScheme() != null ? domain.getScheme().getId() : null;
		this.subSchemeId = domain.getSubScheme() != null ? domain.getSubScheme().getId() : null;
		this.functionaryId = domain.getFunctionary() != null ? domain.getFunctionary().getId() : null;
		this.divisionId = domain.getDivision() != null ? domain.getDivision().getId() : null;
		this.departmentId = domain.getDepartment() != null ? domain.getDepartment().getId() : null;
		this.sourcePath = domain.getSourcePath();
		this.budgetCheckRequired = domain.getBudgetCheckRequired();
		this.budgetAppropriationNo = domain.getBudgetAppropriationNo();
		this.stateId = domain.getState() != null ? domain.getState().getId() : null;

		return this;

	}

}
