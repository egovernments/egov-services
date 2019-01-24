package org.egov.hrms.model;

import javax.validation.constraints.NotNull;

import org.egov.hrms.model.enums.DeactivationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class DeactivationDetails {
	
	private String id;

	private String reasonForDeactivation;
	
	@NotNull
	private String orderNo;

	@NotNull
	private Long effectiveFrom;

	@NotNull
	private DeactivationType typeOfDeactivation;
	
	private String tenantId;

	private AuditDetails auditDetails;



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeactivationDetails other = (DeactivationDetails) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (reasonForDeactivation == null) {
			if (other.reasonForDeactivation != null)
				return false;
		} else if (!reasonForDeactivation.equals(other.reasonForDeactivation))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		}
		else if (orderNo != other.orderNo)
			return false;
		if (effectiveFrom == null) {
			if (other.effectiveFrom != null)
				return false;
		} else if (!effectiveFrom.equals(other.effectiveFrom))
			return false;
		if (typeOfDeactivation == null) {
			return other.typeOfDeactivation == null;
		} else return (typeOfDeactivation== other.typeOfDeactivation);
	}


}


