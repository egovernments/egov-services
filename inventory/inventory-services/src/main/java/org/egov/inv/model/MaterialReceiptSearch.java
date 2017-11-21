package org.egov.inv.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialReceiptSearch {

    private List<String> ids;

    private List<String> mrnNumber;

    private List<String> mrnStatus;

    private Long receiptDate;

    private List<String> receiptType;

    private String receiptPurpose;

    private String receivingStore;

    private String issueingStore;

    private String supplierCode;

    private String receivedBy;
    
    private String materialTypeName;
    
    private String storeName;
    
    private String financialYear;

    private String tenantId;

    private String sortBy;

    private Integer pageSize;
    
    private Integer pageNumber;

    private Integer offset;
}
