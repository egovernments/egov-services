package org.egov.inv.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderSearch {

	private String tenantId;
	private List<String> ids;
	private String store;
	private String purchaseOrderNumber;
	private Long purchaseOrderDate;
	private String purchaseType;
	private String rateType;
	private String supplier;
	private BigDecimal advanceAmount;
	private BigDecimal totalAmount;
	private BigDecimal advancePercentage;
	private Long expectedDeliveryDate;
	private String deliveryTerms;
	private String paymentTerms;
	private String remarks;
	private String status;
	private String fileStoreId;
	private Boolean searchPoAdvReq;
	private String designation;
	private Long stateId;
	private Integer pageSize;
	private Integer pageNumber;
	private String sortBy;

}
