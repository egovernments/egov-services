package org.egov.collection.web.contract;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.collection.model.AuditDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailsWrapper {

	@NotNull
	@JsonProperty("BillDetail")
	private BillDetail billDetails;
	
	private String businessDetailsCode;

	private String refNo;

	private String event;

	private String receiptNumber;

	private Timestamp receiptDate;
	
	private String receiptType;

	private String channel;
  
	private String voucherHeader;
    
	private String collectionType;
  
	private String boundary;

	private String reasonForCancellation;
	
	private BigDecimal amountPaid;
	
	@NotNull
	@JsonProperty("BillAccountDetailsWrapper")
	private List<BillAccountDetailsWrapper> billAccountDetailsWrapper = new ArrayList<BillAccountDetailsWrapper>();//for collection-service

}
