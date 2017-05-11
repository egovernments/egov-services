package org.egov.user.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserKey implements Serializable {
	public static final String SEQ_USER = "seq_eg_user";
	private static final long serialVersionUID = -4233379616753115481L;

	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "tenantid", nullable = false)
	private String tenantId;
}



