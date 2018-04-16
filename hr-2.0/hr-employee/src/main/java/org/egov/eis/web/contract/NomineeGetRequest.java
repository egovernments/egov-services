package org.egov.eis.web.contract;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NomineeGetRequest {

	private List<Long> id;

	@Size(min=3, max=100)
	private String name;

	private Boolean nominated;

	private List<Long> employeeId;

	private String sort;

	private String sortBy;

	private String sortOrder;

	@NotNull
	@Size(max=256)
	private String tenantId;

	@Min(1)
	@Max(500)
	private Short pageSize;

	private Short pageNumber;

}
