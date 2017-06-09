package org.egov.property.model;

import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * Description : This model have common properties of master
 * @author Narendra
 *
 */

@Data
@Component
public class MasterModel {

	private String tenantId;

	private String code;

	private String name;

	private String description;
}
