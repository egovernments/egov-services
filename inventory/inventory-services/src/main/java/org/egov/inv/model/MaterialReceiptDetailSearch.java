package org.egov.inv.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialReceiptDetailSearch {

    private List<String> ids;

    private List<String> mrnNumber;

    private String tenantId;

    private String material;

    private String batchNo;

    private String uomNo;

    private String orderNumber;

    private String poDetailId;

    private BigDecimal receivedQuantity;

    private BigDecimal acceptedQuantity;

    private BigDecimal unitRate;

    private String asset;

    private String voucherHeader;

    private Boolean scrapItem;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;
}
