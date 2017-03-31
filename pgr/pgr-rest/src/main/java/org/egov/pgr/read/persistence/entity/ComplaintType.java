package org.egov.pgr.read.persistence.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static org.egov.pgr.read.persistence.entity.ComplaintType.SEQ_COMPLAINTTYPE;

@Entity
@Table(name = "egpgr_complainttype")
@SequenceGenerator(name = SEQ_COMPLAINTTYPE, sequenceName = SEQ_COMPLAINTTYPE, allocationSize = 1)
public class ComplaintType extends AbstractAuditable {
	public static final String SEQ_COMPLAINTTYPE = "SEQ_EGPGR_COMPLAINTTYPE";
	private static final long serialVersionUID = 8904645810221559541L;
	@Id
	@GeneratedValue(generator = SEQ_COMPLAINTTYPE, strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotBlank
	@SafeHtml
	@Length(max = 150)
	@Column(name = "name")
	private String name;

	@NotBlank
	@Length(max = 20)
	@SafeHtml
	@Column(name = "code", updatable = false)
	private String code;

	private Long department;

	@Length(max = 100)
	@SafeHtml
	private String description;

	@NotNull
	@Column(name = "slahours")
	private Integer slaHours;

	@Column(name = "isactive")
	private boolean isActive;

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

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Long getDepartment() {
		return department;
	}

	public void setDepartment(final Long department) {
		this.department = department;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(final boolean isActive) {
		this.isActive = isActive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Integer getSlaHours() {
		return slaHours;
	}

	public void setSlaHours(final Integer slaHours) {
		this.slaHours = slaHours;
	}

	public boolean isHasFinancialImpact() {
		return hasFinancialImpact;
	}

	public void setHasFinancialImpact(final boolean hasFinancialImpact) {
		this.hasFinancialImpact = hasFinancialImpact;
	}

	public ComplaintTypeCategory getCategory() {
		return category;
	}

	public void setCategory(final ComplaintTypeCategory category) {
		this.category = category;
	}

	public boolean isMetadata() {
		return metadata;
	}

	public void setMetadata(boolean metadata) {
		this.metadata = metadata;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
}
