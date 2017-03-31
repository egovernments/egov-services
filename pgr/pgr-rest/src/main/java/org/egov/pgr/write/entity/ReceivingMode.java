package org.egov.pgr.write.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity(name = "receiving_mode_write")
@Table(name = "egpgr_receivingmode")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = ReceivingMode.SEQ_RECEIVINGMODE, sequenceName = ReceivingMode.SEQ_RECEIVINGMODE, allocationSize = 1)
public class ReceivingMode extends AbstractPersistable<Long> {

	protected static final String SEQ_RECEIVINGMODE = "seq_egpgr_receivingmode";

	@Id
	@GeneratedValue(generator = SEQ_RECEIVINGMODE, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Length(min = 1, max = 150)
	private String name;

	@Length(min = 1, max = 50)
	private String code;

	private boolean visible;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	protected void setId(final Long id) {
		this.id = id;
	}
}