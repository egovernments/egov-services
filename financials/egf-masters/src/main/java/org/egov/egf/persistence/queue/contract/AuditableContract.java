package org.egov.egf.persistence.queue.contract;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "tenantId" })
public class AuditableContract {

    private Long createdBy;

    private Date createdDate;

    private Long lastModifiedBy;

    private Date lastModifiedDate;

    @NotNull
    private String tenantId;
}
