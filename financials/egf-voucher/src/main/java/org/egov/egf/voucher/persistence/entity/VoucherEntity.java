package org.egov.egf.voucher.persistence.entity;

import java.util.Date;

import org.egov.common.domain.model.Auditable;
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

    public Voucher toDomain() {

        Voucher voucher = new Voucher();

        super.toDomain(voucher);

        voucher.setId(this.id);
        voucher.setType(this.type);
        voucher.setName(this.name);
        voucher.setDescription(this.description);
        voucher.setVoucherNumber(this.voucherNumber);
        voucher.setVoucherDate(this.voucherDate);
        voucher.setOriginalVoucherNumber(this.originalVoucherNumber);
        voucher.setRefVoucherNumber(this.refVoucherNumber);
        voucher.setModuleName(this.moduleName);
        voucher.setBillNumber(this.billNumber);
        voucher.setFund(FundContract.builder().id(fundId).build());
        voucher.setStatus(FinancialStatusContract.builder().id(statusId).build());
        voucher.setFunction(FunctionContract.builder().id(functionId).build());
        voucher.setFundsource(FundsourceContract.builder().id(fundsourceId).build());
        voucher.setScheme(SchemeContract.builder().id(schemeId).build());
        voucher.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
        voucher.setFunctionary(FunctionaryContract.builder().id(functionaryId).build());
        voucher.setDivision(Boundary.builder().id(divisionId).build());
        voucher.setDepartment(Department.builder().id(departmentId).build());
        voucher.setSourcePath(this.sourcePath);
        voucher.setBudgetCheckRequired(this.budgetCheckRequired);
        voucher.setBudgetAppropriationNo(this.budgetAppropriationNo);

        return voucher;
    }

    public VoucherEntity toEntity(Voucher voucher) {

        super.toEntity((Auditable) voucher);

        this.id = voucher.getId();
        this.type = voucher.getType();
        this.name = voucher.getName();
        this.description = voucher.getDescription();
        this.voucherNumber = voucher.getVoucherNumber();
        this.voucherDate = voucher.getVoucherDate();
        this.originalVoucherNumber = voucher.getOriginalVoucherNumber();
        this.refVoucherNumber = voucher.getRefVoucherNumber();
        this.moduleName = voucher.getModuleName();
        this.billNumber = voucher.getBillNumber();
        this.fundId = voucher.getFund() != null ? voucher.getFund().getId() : null;
        this.statusId = voucher.getStatus() != null ? voucher.getStatus().getId() : null;
        this.functionId = voucher.getFunction() != null ? voucher.getFunction().getId() : null;
        this.fundsourceId = voucher.getFundsource() != null ? voucher.getFundsource().getId() : null;
        this.schemeId = voucher.getScheme() != null ? voucher.getScheme().getId() : null;
        this.subSchemeId = voucher.getSubScheme() != null ? voucher.getSubScheme().getId() : null;
        this.functionaryId = voucher.getFunctionary() != null ? voucher.getFunctionary().getId() : null;
        this.divisionId = voucher.getDivision() != null ? voucher.getDivision().getId() : null;
        this.departmentId = voucher.getDepartment() != null ? voucher.getDepartment().getId() : null;
        this.sourcePath = voucher.getSourcePath();
        this.budgetCheckRequired = voucher.getBudgetCheckRequired();
        this.budgetAppropriationNo = voucher.getBudgetAppropriationNo();

        return this;

    }

}
