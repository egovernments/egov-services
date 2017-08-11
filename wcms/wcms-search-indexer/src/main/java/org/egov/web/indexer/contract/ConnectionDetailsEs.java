package org.egov.web.indexer.contract;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConnectionDetailsEs {
	
    private long id;
    private String connectionType;
    private String applicationType;
    private String billingType;
    private String categoryType;
    private String categoryId;
    private String hscPipeSizeType;
    private String pipesizeId;
    private Long executionDate;
    private String supplyType;
    private int noOfFlats;
    private String supplyTypeId;
    private String propertyIdentifier;
    private String assetIdentifier;
    private String sourceType;
    private String sourceTypeId;
    private String waterTreatment;
    private String waterTreatmentId;
    private String connectionStatus;
    private String status;
    private Long stateId;
    private String demandid;
    private double sumpCapacity;
    private double donationCharge;
    private int numberOfTaps;
    private int numberOfPersons;
    private String legacyConsumerNumber;
    private String estimationNumber;
    private String workOrderNumber;
    private Boolean isLegacy;
    private String acknowledgementNumber;
    private String consumerNumber;
    private String bplCardHolderName;
    private long parentConnectionId;
    private List<DocumentOwner> documents;
    private List<ConnectionDemand> connectionDemands;
    private List<Timeline> timelines;
    private Property property;
    private Demand demand;
    private Asset asset;
    private List<Meter> meter;
    private List<EstimationCharge> estimationCharge;
    private WorkOrder workOrder;
    private AuditDetails auditDetails;
    private String tenantId;
    private WorkflowDetails workflowDetails;
    private String createdDate; 
}
