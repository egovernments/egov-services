package org.egov.property.model;

import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
public class MasterModel {

	private String tenantId;

	private String code;

	private String name;

	private String description;
}
