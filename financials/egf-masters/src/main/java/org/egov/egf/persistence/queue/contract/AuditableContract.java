package org.egov.egf.persistence.queue.contract;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" })
public class AuditableContract {

	private static final long serialVersionUID = 7138056997693406739L;

	private Long createdBy;

	private Date createdDate;

	private Long lastModifiedBy;

	private Date lastModifiedDate;
}
