package org.egov.user.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "eg_role")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Role extends AbstractAuditable<RoleKey> {
    private static final long serialVersionUID = -3174785753813418004L;

    public Role(org.egov.user.domain.model.Role domainRole) {
        this.roleKey = new RoleKey(domainRole.getId(), domainRole.getTenantId());
        this.name = domainRole.getName();
        this.code= domainRole.getCode();
        this.description = domainRole.getDescription();
        this.setLastModifiedDate(domainRole.getLastModifiedDate());
        this.setCreatedDate(domainRole.getCreatedDate());
    }

    @EmbeddedId
    private RoleKey roleKey;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    public org.egov.user.domain.model.Role toDomain() {
        return org.egov.user.domain.model.Role.builder()
                .id(roleKey.getId())
                .name(name)
                .code(code)
                .description(description)
                .createdDate(getCreatedDate())
                .lastModifiedDate(getLastModifiedDate())
                .createdBy(getCreatedBy())
                .tenantId(roleKey.getTenantId())
                .lastModifiedBy(getLastModifiedBy()).build();
    }

	@Override
	public RoleKey getId() {
		return roleKey;
	}

	@Override
	protected void setId(RoleKey id) {
		this.roleKey = id;
	}
}
