package org.egov.egf.master.web.contract;

import lombok.Data;
@Data
public class FundSearchContract extends FundContract {
    
        private String sortBy;
	private Integer pageSize;
	private Integer offset;
}
