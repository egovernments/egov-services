package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <h1>Page</h1>
 * @author Narendra
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Page {
	private Integer totalResults;

	private Integer totalPages;

	private Integer pageSize;

	private Integer currentPage;

	private Integer offSet;
}
