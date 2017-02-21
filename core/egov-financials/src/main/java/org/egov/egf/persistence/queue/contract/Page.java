package org.egov.egf.persistence.queue.contract;

import lombok.Data;

@Data
public class Page {

	private Integer totalResults;

	private Integer totalPages;

	private Integer pageSize;

	private Integer currentPage;

}
