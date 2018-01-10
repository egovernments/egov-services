package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.AuditDetails;
import org.egov.inv.model.Disposal;
import org.egov.inv.model.Disposal.DisposalStatusEnum;
import org.egov.inv.model.Store;
import org.egov.inv.persistence.entity.MaterialIssueEntity.MaterialIssueEntityBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisposalEntity {

	public static final String TABLE_NAME = "disposal";
	public static final String SEQUENCE_NAME = "seq_disposal";
	public static final String ALIAS = "disposal";

	private String id;

	private String tenantId;

	private String store;

	private String disposalNumber;

	private Long disposalDate;

	private String handOverTo;

	private String auctionNumber;

	private String description;

	private String disposalStatus;

	private Long stateId;

	private Double totalDisposalValue;

	private String createdBy;

	private Long createdTime;

	private String lastModifiedBy;

	private Long lastModifiedTime;

	public Disposal toDomain() {
		Disposal disposal = new Disposal();
		disposal.setId(id);
		disposal.setTenantId(tenantId);
		Store sto = new Store();
		sto.setCode(store);
		disposal.setStore(sto);
		disposal.setDisposalNumber(disposalNumber);
		disposal.setDisposalDate(disposalDate);
		disposal.setHandOverTo(handOverTo);
		disposal.setAuctionNumber(auctionNumber);
		disposal.setDescription(description);
		disposal.setDisposalStatus(DisposalStatusEnum.valueOf(disposalStatus));
		disposal.setStateId(stateId);
		if(totalDisposalValue != null)
		disposal.setTotalDisposalValue(BigDecimal.valueOf(totalDisposalValue));
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(createdBy);
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedBy(lastModifiedBy);
		auditDetails.setLastModifiedTime(lastModifiedTime);
		return disposal;
	}

	public DisposalEntity toEntity(final Disposal disposal) {

		if (disposal.getId() != null)
			this.id = disposal.getId();
		this.tenantId = disposal.getTenantId();
		if (disposal.getStore() != null && disposal.getStore().getCode() != null)
			this.store = disposal.getStore().getCode();
		if (disposal.getDisposalNumber() != null)
			this.disposalNumber = disposal.getDisposalNumber();
		this.disposalDate = disposal.getDisposalDate();
		if (disposal.getHandOverTo() != null)
			this.handOverTo = disposal.getHandOverTo();
		if (disposal.getAuctionNumber() != null)
			this.auctionNumber = disposal.getAuctionNumber();
		if (disposal.getDescription() != null)
			this.description = disposal.getDescription();
		if (disposal.getDisposalStatus() != null)
			this.disposalStatus = disposal.getDisposalStatus().name();
		if (disposal.getStateId() != null)
			this.stateId = disposal.getStateId();
		if (disposal.getTotalDisposalValue() != null)
			this.totalDisposalValue = Double.valueOf(disposal.getTotalDisposalValue().toString());
		return this;
	}

}
