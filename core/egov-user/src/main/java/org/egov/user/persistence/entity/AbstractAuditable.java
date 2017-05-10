package org.egov.user.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class AbstractAuditable<T extends Serializable> extends AbstractPersistable<T> {

	private static final long serialVersionUID = -3061749263902787420L;

	@Column(name = "createdby")
	private Long createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name = "createddate")
	private Date createdDate;

	@Column(name = "lastmodifiedby")
	private Long lastModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "lastmodifieddate")
	private Date lastModifiedDate;
}