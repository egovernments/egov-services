package org.egov.mr.web.contract;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Component
@Builder
public class MarriageCertCriteria {
	
	
	private String applicationNumber;
	
	private String regnNo;
	
	@NotNull
    private String tenantId;

	@Min(1)
	@Max(500)
	private Short pageSize;
	
	private Short pageNo=1;

}
