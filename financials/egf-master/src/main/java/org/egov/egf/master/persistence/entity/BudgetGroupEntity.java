package org.egov.egf.master.persistence.entity;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.domain.enums.BudgetAccountType;
import org.egov.egf.master.domain.enums.BudgetingType;
import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.domain.model.ChartOfAccount;

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
public class BudgetGroupEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_budgetgroup";
	private String id;
	private String name;
	private String description;
	private String majorCodeId;
	private String maxCodeId;
	private String minCodeId;
	private String accountTypeId;
	private String budgetingTypeId;
	private Boolean active;

	public BudgetGroup toDomain() {
		BudgetGroup budgetGroup = new BudgetGroup();
		super.toDomain(budgetGroup);
		budgetGroup.setId(this.id);
		budgetGroup.setName(this.name);
		budgetGroup.setDescription(this.description);
		budgetGroup.setMajorCode(ChartOfAccount.builder().id(majorCodeId).build());
		budgetGroup.setMaxCode(ChartOfAccount.builder().id(maxCodeId).build());
		budgetGroup.setMinCode(ChartOfAccount.builder().id(minCodeId).build());
		budgetGroup.setAccountType(accountTypeId != null ? BudgetAccountType.valueOf(accountTypeId) : null);
		budgetGroup.setBudgetingType(budgetingTypeId != null ? BudgetingType.valueOf(budgetingTypeId) : null);
		budgetGroup.setActive(this.active);
		return budgetGroup;
	}

	public BudgetGroupEntity toEntity(BudgetGroup budgetGroup) {
		super.toEntity((Auditable) budgetGroup);
		this.id = budgetGroup.getId();
		this.name = budgetGroup.getName();
		this.description = budgetGroup.getDescription();
		this.majorCodeId = budgetGroup.getMajorCode() != null ? budgetGroup.getMajorCode().getId() : null;
		this.maxCodeId = budgetGroup.getMaxCode() != null ? budgetGroup.getMaxCode().getId() : null;
		this.minCodeId = budgetGroup.getMinCode() != null ? budgetGroup.getMinCode().getId() : null;
		this.accountTypeId = budgetGroup.getAccountType() != null ? budgetGroup.getAccountType().toString() : null;
		this.budgetingTypeId = budgetGroup.getBudgetingType() != null ? budgetGroup.getBudgetingType().toString()
				: null;
		this.active = budgetGroup.getActive();
		return this;
	}

}
