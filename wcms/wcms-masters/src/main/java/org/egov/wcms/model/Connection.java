package org.egov.wcms.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class Connection {
	
	@NotNull
	private long id;

	@NotNull
	private String connectionType;
	
	@NotNull
	private String billingType;
	
	@NotNull
	private String categoryType;
	
	@NotNull
	private double hscPipeSizeType;
	
	@NotNull
	private String supplyType;
	
	@NotNull
	private String sourceType;
	
	@NotNull
	private String connectionStatus;
	
	@NotNull
	private double sumpCapacity;
	
	@NotNull
	private long donationCharge;
	
	@NotNull
	private int numberOfTaps;
	
	@NotNull
	private int numberOfPersons;
	
	@NotNull
	private String legacyConsumerNumber;
	
	@NotNull
	private String acknowledgementNumber;
	
	@NotNull
	private String consumerNumber;
	
	@NotNull
	private String bplCardHolderName;
	
	@NotNull
	private List<DocumentOwner> documents;
	
	@NotNull
	private List<Timeline> timelines;
	
	@NotNull
	private Property property;
	
	@NotNull
	private Meter meter;
	
	@NotNull
	private EstimationCharge estimationCharge;
	
	@NotNull
	private WorkOrder workOrder;
	
	@NotNull
	private MeterReading meterReadings;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
	
}
