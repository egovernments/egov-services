package org.egov.workflow.web.contract;

import java.util.Date;

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
    protected String deleteReason;


}
