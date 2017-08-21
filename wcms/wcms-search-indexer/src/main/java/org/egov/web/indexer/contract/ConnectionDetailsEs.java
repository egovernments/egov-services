package org.egov.web.indexer.contract;

import java.util.Date;

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
    private Date executionDate;
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
    private String tenantId;
    private Date createdDate; 
}
