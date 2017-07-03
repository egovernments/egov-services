package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AssetStatus {

	private String tenantId = null;
	
	@Length(min = 3, max = 50)
	@NotNull
	private String objectName = null;
	
	@NotNull
	private List<StatusValue> statusValues = new ArrayList<StatusValue>();
	
	private AuditDetails auditDetails;

}
