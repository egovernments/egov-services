
package org.egov.tl.masters.domain.model;

import java.util.List;

import javax.validation.constraints.Max;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {

	public static int DEFAULT_PAGE_SIZE = 500;
	public static int DEFAULT_PAGE_OFFSET = 0;

	private Integer totalResults;

	private Integer totalPages;

	@Max(500l)
	private Integer pageSize = DEFAULT_PAGE_SIZE;

	private Integer currentPage;

	private Integer offset = DEFAULT_PAGE_OFFSET;

	@JsonProperty(access = Access.WRITE_ONLY)
	List<T> pagedData;

}
