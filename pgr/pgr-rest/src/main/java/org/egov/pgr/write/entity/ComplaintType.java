package org.egov.pgr.write.entity;

import lombok.Getter;
import lombok.Setter;
import org.egov.pgr.common.entity.AbstractAuditable;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static org.egov.pgr.write.entity.ComplaintType.SEQ_COMPLAINTTYPE;

@Getter
@Setter
@Entity(name = "complainttype_write")
@Table(name = "egpgr_complainttype")
@SequenceGenerator(name = SEQ_COMPLAINTTYPE, sequenceName = SEQ_COMPLAINTTYPE, allocationSize = 1)
public class ComplaintType extends AbstractAuditable {
	public static final String SEQ_COMPLAINTTYPE = "SEQ_EGPGR_COMPLAINTTYPE";
	private static final long serialVersionUID = 8904645810221559541L;
	@Id
	@GeneratedValue(generator = SEQ_COMPLAINTTYPE, strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotBlank
	@Length(max = 150)
	@Column(name = "name")
	private String name;

	@NotBlank
	@Length(max = 20)
	@Column(name = "code", updatable = false)
	private String code;

	private Long department;

	@Length(max = 100)
	private String description;

	@NotNull
	@Column(name = "slahours")
	private Integer slaHours;

	@Column(name = "isactive")
	private boolean active;

	@Column(name = "hasfinancialimpact")
	private boolean hasFinancialImpact;

	@ManyToOne(optional = false)
	@JoinColumn(name = "category")
	private ComplaintTypeCategory category;

	@Column(name = "metadata")
	private boolean metadata;

	@Length(max = 50)
	private String type;

	@Length(max = 100)
	private String keywords;

	private String attributes;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

}
