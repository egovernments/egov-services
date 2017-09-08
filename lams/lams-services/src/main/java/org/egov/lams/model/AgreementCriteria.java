package org.egov.lams.model;

import java.util.Date;
import java.util.Set;
import javax.validation.constraints.NotNull;

import lombok.*;
import org.egov.lams.model.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
public class AgreementCriteria {

	@NotNull
	 private String tenantId;
	 private Set<Long> agreementId;
	 private String agreementNumber;
	 private String tenderNumber;
	 private String acknowledgementNumber;
	 
	 @DateTimeFormat(pattern="dd/MM/yyyy")
	 private Date fromDate;
	 
	 @DateTimeFormat(pattern="dd/MM/yyyy")
	 private Date toDate;
	 private Status status;
	 private String tinNumber;
	 private String tradelicenseNumber;
	 private Long assetCategory;
	 private String shoppingComplexNo;
	 private String assetCode;
	 private Long locality;
	 private Long revenueWard;
	 private Long electionWard;
	 private Long doorNo;
	 private String allotteeName;
	 private Long mobileNumber;
	 private Long offSet;
	 private Long size;
	 private String stateId;
	 private Set<Long> asset;
	 private Set<Long> allottee;
}
