package org.egov.inv.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@Builder
public class ScrapDetailSearch {
	
	    private List<String> ids;
	    
	    private String ScrapNumber;
	    
	    private String material;
	    
	    private String tenantId;

	    private String sortBy;

	    private Integer pageSize;
	    
	    private Integer pageNumber;

	    private Integer offset;

}
