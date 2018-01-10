package org.egov.inv.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DisposalSearchContract {
	
	private List<String> id;
	
	private String tenantId;

	private String store;
	
	private String disposalNumber;
	
	private Long disposalDate;
	
	private String handOverTo;
	
	private String auctionNumber;
	
	private String disposalStatus;
	
	private String stateId;
	
	private Double totalDisposalValue;
	
	private Integer pageSize;
	
	private Integer pageNumber;
	
	private String sortBy;
	
	
	

}
