package org.egov.inv.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialReceiptAddInfoSearch {

    private List<String> id;

    private String tenantId;

    private String lotNo;

    private String serialNo;

    private Long manufactureDate;

    private String oldReceiptNumber;

    private Long receivedDate;

    private Long expiryDate;

    private List<String> receiptDetailId;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;
}
