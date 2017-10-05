package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <h1>Page</h1>
 * 
 * @author Narendra
 *
 */
@Setter
@Getter
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
