package org.egov.mr.web.contract;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Component
@Builder
public class FeeCriteria {

	private Set<String>id;
	
	@NotNull
    private String tenantId;
	
	private String feeCriteria;
	
	private Long fromDate;
	
	private Long toDate;
	
	@Min(1)
	@Max(500)
	private Short pageSize;
	
}
