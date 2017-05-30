package org.egov.mr.model;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class Page {
	private Integer totalResults;

	private Integer totalPages;

	private Integer pageSize;

	private Integer currentPage;

	private Integer offSet;
}
