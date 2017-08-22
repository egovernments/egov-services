package org.egov.mr.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.egov.mr.model.Location.LocationBuilder;
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
public class Fee {

	private String id;
	
	@NotNull
	private String tenantId;
	
	@NotNull
	private String feeCriteria;
	
	@NotNull
	private BigDecimal fee;
	
	@NotNull
	private Long fromDate;
	
	@NotNull
	private Long toDate;
	
}
