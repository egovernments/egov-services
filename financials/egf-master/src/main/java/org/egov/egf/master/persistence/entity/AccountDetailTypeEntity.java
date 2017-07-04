package org.egov.egf.master.persistence.entity;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.domain.model.AccountDetailType;

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
public class AccountDetailTypeEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_accountdetailtype";
	private String id;
	private String name;
	private String description;
	private String tableName;
	private Boolean active;
	private String fullyQualifiedName;

	public AccountDetailType toDomain() {
		AccountDetailType accountDetailType = new AccountDetailType();
		super.toDomain(accountDetailType);
		accountDetailType.setId(this.id);
		accountDetailType.setName(this.name);
		accountDetailType.setDescription(this.description);
		accountDetailType.setTableName(this.tableName);
		accountDetailType.setActive(this.active);
		accountDetailType.setFullyQualifiedName(this.fullyQualifiedName);
		return accountDetailType;
	}

	public AccountDetailTypeEntity toEntity(AccountDetailType accountDetailType) {
		super.toEntity((Auditable) accountDetailType);
		this.id = accountDetailType.getId();
		this.name = accountDetailType.getName();
		this.description = accountDetailType.getDescription();
		this.tableName = accountDetailType.getTableName();
		this.active = accountDetailType.getActive();
		this.fullyQualifiedName = accountDetailType.getFullyQualifiedName();
		return this;
	}

}
