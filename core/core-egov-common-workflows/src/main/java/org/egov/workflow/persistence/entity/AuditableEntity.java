package org.egov.workflow.persistence.entity;

import java.util.Date;

import org.egov.workflow.domain.model.Auditable;
import org.egov.workflow.web.contract.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditableEntity {
    protected String tenantId;
    protected Long createdBy;
    protected Long lastModifiedBy;
    protected Date createdDate;
    protected Date lastModifiedDate;
    protected String deleteReason;

    protected void toDomain(Auditable domain) {
        if (createdBy != null) {
            
                domain.setCreatedBy(User.builder().id((this.getCreatedBy())).build());
        }
        if (lastModifiedBy != null) {
                domain.setLastModifiedBy(User.builder().id((this.getLastModifiedBy())).build());
        }
        domain.setCreatedDate(this.getCreatedDate());
        domain.setLastModifiedDate(this.getLastModifiedDate());
        domain.setTenantId(this.getTenantId());
        domain.setDeleteReason(this.getDeleteReason());
    }

    protected void toEntity(Auditable domain) {
        this.setCreatedBy(domain.getCreatedBy() != null ? domain.getCreatedBy().getId() : null);
        this.setLastModifiedBy(domain.getLastModifiedBy() != null ? domain.getLastModifiedBy().getId() : null);
        this.setCreatedDate(domain.getCreatedDate());
        this.setLastModifiedDate(domain.getLastModifiedDate());
        this.setTenantId(domain.getTenantId());
        this.setDeleteReason(domain.getDeleteReason());
    }

}
