package org.egov.workflow.web.contract;

import javax.validation.constraints.Max;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class Pagination {
	
	public static int DEFAULT_PAGE_SIZE=20;
	public static int DEFAULT_PAGE_OFFSET=0;

	private Integer totalResults;

	private Integer totalPages;

	@Max(500l)
	private Integer pageSize= Integer.valueOf(DEFAULT_PAGE_SIZE);

	private Integer currentPage;
	
	private Integer offSet=Integer.valueOf(DEFAULT_PAGE_OFFSET);
	
	public void map(Page page)
	{
		this.setCurrentPage(page.getNumber());
		this.setTotalPages(page.getTotalPages());
		this.setPageSize(page.getSize());
		this.setTotalResults(page.getNumberOfElements());
		
	}

}
