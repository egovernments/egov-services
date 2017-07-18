package org.egov.collection.web.contract;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
@Setter
@Getter
public class ReceiptSearchGetRequest {
    private List<String> receiptNumbers;

    private String consumerCode;

    private String fromDate;

    private String toDate;

    private String collectedBy;

    private String status;

    private String paymentType;

    private String classification;


    private String businessCode;

    @NotNull
    private String tenantId;
    
    private String sortBy;
    
    private String sortOrder;
}
