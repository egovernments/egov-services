package org.egov.marriagefee.model;

import org.egov.marriagefee.model.enums.CertificateType;
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
public class MarriageCertificate {

	private String certificateNo;

	private Long certificateDate;

	private CertificateType certificateType;

	private String regnNumber;

	private String bridegroomPhoto;

	private String bridePhoto;

	private String husbandName;

	private String husbandAddress;

	private String wifeName;

	private String wifeAddress;

	private Long marriageDate;

	private String marriageVenueAddress;

	private Long regnDate;

	private String regnSerialNo;

	private String regnVolumeNo;

	private String certificatePlace;

	private String templateVersion;

	private String applicationNumber;

	private String tenantId;
}
