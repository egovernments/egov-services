package org.egov.common.web.contract;

import java.util.Date;

import org.egov.common.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuditableContract {
	protected String tenantId;
	protected User createdBy;
	protected User lastModifiedBy;
	protected Date createdDate;
	protected Date lastModifiedDate;

	/*
	 * protected void toDomain(Auditable domain) { if(this.createdBy!=null)
	 * domain.setCreatedBy(User.builder().id(this.getCreatedBy().getId()).build(
	 * )); if(this.lastModifiedBy!=null)
	 * domain.setLastModifiedBy(User.builder().id(this.getLastModifiedBy().getId
	 * ()).build()); domain.setCreatedDate(this.getCreatedDate());
	 * domain.setLastModifiedDate(this.getLastModifiedDate());
	 * domain.setTenantId(this.getTenantId()); }
	 *
	 * public void toContract(Auditable domain) {
	 * this.setCreatedBy(domain.getCreatedBy());
	 * this.setLastModifiedBy(domain.getLastModifiedBy());
	 * this.setCreatedDate(domain.getCreatedDate());
	 * this.setLastModifiedDate(domain.getLastModifiedDate());
	 * this.setTenantId(domain.getTenantId()); }
	 */

}
