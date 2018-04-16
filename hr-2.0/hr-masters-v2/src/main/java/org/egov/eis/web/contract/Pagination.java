package org.egov.eis.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Pagination {

	private Integer totalResults;
	private Integer currentPage;
	private Integer totalPages;
	private Integer pageNumber;
	private Integer pageSize;

}