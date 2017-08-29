package org.egov.tradelicense.persistence.entity;
import java.sql.Timestamp;

import org.egov.tradelicense.domain.enums.ApplicationType;
import org.egov.tradelicense.domain.model.TradeLicense;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseApplicationEntity {

	public static final String TABLE_NAME = "egtl_license_application";
	public static final String SEQUENCE_NAME = "seq_egtl_license_application";
	
	private Long id;
	
	private String applicationNumber;
	
	private String tenantId;
	
	private String applicationType;
	
	private String status;
	
	private String state_id;
	
	private Timestamp applicationDate;
	
	private long licenseId;
	
	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;
	
	public TradeLicense toDomain( TradeLicense license){
		
		license.setApplicationDate( this.getApplicationDate().getTime() );
		license.setApplicationNumber( this.getApplicationNumber() );
		license.setApplicationType( ApplicationType.valueOf( this.getApplicationType()) );
		
		return license;
	}
	
	public LicenseApplicationEntity toEntity(TradeLicenseEntity license){
		
		this.tenantId = license.getTenantId();
		
		this.setApplicationNumber( license.getApplicationNumber());
		
		this.setApplicationType( license.getApplicationType().toString());
		
		this.setStatus( license.getStatus().toString());
		
//		this.setApplicationDate( license.getApplicationDate());
		this.applicationDate = license.getApplicationDate();
		
		this.setLicenseId( license.getId());
		
		this.setCreatedBy( license.getCreatedBy());
		this.setCreatedTime( license.getCreatedTime());
		
		this.setLastModifiedBy( license.getLastModifiedBy());
		this.setLastModifiedTime( license.getLastModifiedTime());
		
		return this;
	}
}
