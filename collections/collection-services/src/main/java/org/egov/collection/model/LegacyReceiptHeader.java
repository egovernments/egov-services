package org.egov.collection.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LegacyReceiptHeader {
    @NotNull
    private Long id;
    
    private Long legacyReceiptId;
    
    private String  receiptNo;
    
    @NotNull
    private Date receiptDate;
    
    private String department;
    
    private String serviceName;
    
    private String consumerNo;
    
    private String consumerName;
    
    private Double totalAmount;
    
    private Double advanceAmount; 
    
    private List<LegacyReceiptDetails> legacyReceiptDetails;
    
    private Double adjustmentAmount;
    
    private String consumerAddress;
    
    private String payeeName;
    
    private String instrumentType;
    
    private Date instrumentDate;
    
    private String instrumentNo;
    
    private String bankName;
    
    private String manualreceiptnumber;
    
    private Date manualreceiptDate;
    @NotNull
    private String tenantId;
    
    private String remarks;

}
