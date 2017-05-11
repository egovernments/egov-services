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
public class RoleKey implements Serializable {
	public static final String SEQ_ROLE = "SEQ_EG_ROLE";
	private static final long serialVersionUID = -5470737147505010547L;

	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "tenantid", nullable = false)
	private String tenantId;
}
