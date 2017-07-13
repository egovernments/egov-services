package org.egov.egf.voucher.persistence.entity;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FunctionaryContract;
import org.egov.egf.master.web.contract.FundsourceContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;
import org.egov.egf.voucher.domain.model.Vouchermis;

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
public class VouchermisEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_vouchermis";
	private String id;
	private String voucherId;
	private String billNumber;
	private String functionId;
	private String fundsourceId;
	private String schemeId;
	private String subSchemeId;
	private String functionaryId;
	private String sourcePath;
	private Boolean budgetCheckRequired;
	private String budgetAppropriationNo;

	public Vouchermis toDomain() {
		Vouchermis vouchermis = new Vouchermis();
		super.toDomain(vouchermis);
		vouchermis.setId(this.id);
		vouchermis.setBillNumber(this.billNumber);
		vouchermis.setFunction(FunctionContract.builder().id(functionId).build());
		vouchermis.setFundsource(FundsourceContract.builder().id(fundsourceId).build());
		vouchermis.setScheme(SchemeContract.builder().id(schemeId).build());
		vouchermis.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
		vouchermis.setFunctionary(FunctionaryContract.builder().id(functionaryId).build());
		vouchermis.setSourcePath(this.sourcePath);
		vouchermis.setBudgetCheckRequired(this.budgetCheckRequired);
		vouchermis.setBudgetAppropriationNo(this.budgetAppropriationNo);
		return vouchermis;
	}

	public VouchermisEntity toEntity(Vouchermis vouchermis) {
		super.toEntity(vouchermis);
		this.id = vouchermis.getId();
		this.billNumber = vouchermis.getBillNumber();
		this.functionId = vouchermis.getFunction() != null ? vouchermis.getFunction().getId() : null;
		this.fundsourceId = vouchermis.getFundsource() != null ? vouchermis.getFundsource().getId() : null;
		this.schemeId = vouchermis.getScheme() != null ? vouchermis.getScheme().getId() : null;
		this.subSchemeId = vouchermis.getSubScheme() != null ? vouchermis.getSubScheme().getId() : null;
		this.functionaryId = vouchermis.getFunctionary() != null ? vouchermis.getFunctionary().getId() : null;
		this.sourcePath = vouchermis.getSourcePath();
		this.budgetCheckRequired = vouchermis.getBudgetCheckRequired();
		this.budgetAppropriationNo = vouchermis.getBudgetAppropriationNo();
		return this;
	}

}
