package org.egov.pgrrest.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

import static org.egov.pgrrest.common.entity.ServiceType.SEQ_SERVICE_TYPE;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "egpgr_complainttype")
@SequenceGenerator(name = SEQ_SERVICE_TYPE, sequenceName = SEQ_SERVICE_TYPE, allocationSize = 1)
public class ServiceType extends AbstractAuditable<Long> {
	public static final String SEQ_SERVICE_TYPE = "SEQ_EGPGR_COMPLAINTTYPE";
	private static final long serialVersionUID = 8904645810221559541L;
	@Id
	@GeneratedValue(generator = SEQ_SERVICE_TYPE, strategy = GenerationType.SEQUENCE)
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
	private ServiceTypeCategory category;

	@Column(name = "metadata")
	private boolean metadata;

	private String type;

	@Transient
	private List<String> keywords;

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
