package org.egov.wcms.web.contract;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class MeterStatusGetRequest {

	private List<Long> ids;

	private String code;

	private String meterStatus;

	@NotNull
	private String tenantId;

	private String sortBy;

	private String sortOrder;

}
