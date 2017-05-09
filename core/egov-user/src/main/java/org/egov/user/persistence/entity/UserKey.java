package org.egov.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

import static org.egov.user.persistence.entity.UserKey.SEQ_USER;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = SEQ_USER, sequenceName = SEQ_USER, allocationSize = 1)
public class UserKey implements Serializable {
	public static final String SEQ_USER = "seq_eg_user";

	@Column(name = "id", nullable = false)
	@GeneratedValue(generator = SEQ_USER, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "tenantid", nullable = false)
	private String tenantId;
}



