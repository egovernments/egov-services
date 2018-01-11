package org.egov.inv.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapSearch {
	
	    private List<String> ids;
	    
	    private List<String> scrapNumber;
	    
	    private String store;
	    
	    private Long scrapDate;
	    
	    private String scrapStatus;
	    
	    private Integer stateId;
	
	    private String tenantId;

	    private String sortBy;

	    private Integer pageSize;
	    
	    private Integer pageNumber;

	    private Integer offset;

}
