package org.egov.pgr.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static org.egov.pgr.common.entity.ComplaintType.SEQ_COMPLAINTTYPE;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "egpgr_complainttype")
@SequenceGenerator(name = SEQ_COMPLAINTTYPE, sequenceName = SEQ_COMPLAINTTYPE, allocationSize = 1)
public class ComplaintType extends AbstractAuditable {
	public static final String SEQ_COMPLAINTTYPE = "SEQ_EGPGR_COMPLAINTTYPE";
	private static final long serialVersionUID = 8904645810221559541L;
	@Id
	@GeneratedValue(generator = SEQ_COMPLAINTTYPE, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "code", updatable = false)
	private String code;

	private Long department;

	private String description;

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

	private String type;

	private String keywords;

//	private String attributes;
	
	@Column(name = "tenantid")
	private String tenantId;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

}
