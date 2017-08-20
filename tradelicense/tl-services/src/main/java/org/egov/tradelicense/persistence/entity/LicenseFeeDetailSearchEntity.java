package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.model.LicenseFeeDetailSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseFeeDetailSearchEntity {

	public static final String TABLE_NAME = "egtl_fee_details";
	public static final String SEQUENCE_NAME = "seq_egtl_fee_details";

	private Long id;

	private Long licenseId;

	private String financialYear;

	private Double amount;

	private Boolean paid;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public LicenseFeeDetailSearch toDomain() {

		LicenseFeeDetailSearch licenseFeeDetail = new LicenseFeeDetailSearch();

		licenseFeeDetail.setId(this.id);

		licenseFeeDetail.setLicenseId(this.licenseId);

		licenseFeeDetail.setFinancialYear(this.financialYear);

		licenseFeeDetail.setPaid(this.paid);

		licenseFeeDetail.setAmount(this.amount);

		licenseFeeDetail.setCreatedBy(this.createdBy);
		licenseFeeDetail.setCreatedTime(this.createdTime);
		licenseFeeDetail.setLastModifiedBy(this.lastModifiedBy);
		licenseFeeDetail.setLastModifiedTime(this.lastModifiedTime);

		return licenseFeeDetail;
	}

	public LicenseFeeDetailSearchEntity toEntity(LicenseFeeDetailSearch licenseFeeDetail) {

		this.amount = licenseFeeDetail.getAmount();

		this.id = licenseFeeDetail.getId();

		this.licenseId = licenseFeeDetail.getLicenseId();

		this.financialYear = licenseFeeDetail.getFinancialYear();

		this.paid = licenseFeeDetail.getPaid();

		this.createdBy = licenseFeeDetail.getCreatedBy();

		this.lastModifiedBy = licenseFeeDetail.getLastModifiedBy();

		this.createdTime = licenseFeeDetail.getCreatedTime();

		this.lastModifiedTime = licenseFeeDetail.getLastModifiedTime();

		return this;

	}
}