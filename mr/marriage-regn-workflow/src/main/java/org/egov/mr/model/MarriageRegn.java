package org.egov.mr.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.mr.model.enums.Action;
import org.egov.mr.model.enums.ApplicationStatus;
import org.egov.mr.model.enums.Source;
import org.egov.mr.model.enums.Venue;
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
public class MarriageRegn {

	private String id;
	
	private RegistrationUnit regnUnit;

	@NotNull
	private Long marriageDate;

	@NotNull
	private Venue venue;

	@NotNull
	private String street;

	private String placeOfMarriage;

	@NotNull
	private String locality;

	@NotNull
	private String city;

	@NotNull
	private String marriagePhoto;

	private Fee fee;

	private MarryingPerson bridegroom;

	private MarryingPerson bride;

	private List<Witness> witnesses = new ArrayList<Witness>();

	private PriestInfo priest;

	private List<MarriageDocument> documents = new ArrayList<MarriageDocument>();

	private String serialNo;

	private String volumeNo;

	private String applicationNumber;
	
	private String regnNumber;
	
	private Long regnDate;

	private ApplicationStatus status;

	private Source source;

	private String stateId;

	private ApprovalDetails approvalDetails;
	
	private String rejectionReason;
	
	private String remarks;

	private List<MarriageCertificate> certificates = new ArrayList<MarriageCertificate>();
	
	private List<Demand> demands = new ArrayList<Demand>();
	
	private List<String> demandIds= new ArrayList<>();

	private Action actions;
	
	private AuditDetails auditDetails;
	
	private Boolean isActive;

	@NotNull
	private String tenantId;

}
