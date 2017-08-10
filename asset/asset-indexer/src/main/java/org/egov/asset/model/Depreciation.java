package org.egov.asset.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Depreciation extends DepreciationCriteria{

	private Long voucherReference;

	private AuditDetails auditDetails;
	
	private List<DepreciationDetail> depreciationDetails = new ArrayList<>();
	
	@Builder
	private Depreciation(DepreciationCriteria depreciationCriteria,Long voucherReference,
				AuditDetails auditDetails,List<DepreciationDetail> depreciationDetails){
		super(depreciationCriteria);
		this.voucherReference = voucherReference;
		this.auditDetails = auditDetails;
		this.depreciationDetails = depreciationDetails;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Depreciation other = (Depreciation) obj;
		if (auditDetails == null) {
			if (other.auditDetails != null)
				return false;
		} else if (!auditDetails.equals(other.auditDetails))
			return false;
		if (depreciationDetails == null) {
			if (other.depreciationDetails != null)
				return false;
		} else if (!depreciationDetails.equals(other.depreciationDetails))
			return false;
		if (voucherReference == null) {
			if (other.voucherReference != null)
				return false;
		} else if (!voucherReference.equals(other.voucherReference))
			return false;
		return true;
	}
	
	
	
}
