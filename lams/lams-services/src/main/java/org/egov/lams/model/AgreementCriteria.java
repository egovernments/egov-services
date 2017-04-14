package org.egov.lams.model;

import java.util.Date;
import java.util.Set;
import org.egov.lams.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgreementCriteria {

	 private String tenantId;
	 private Set<Long> agreementId;
	 private String agreementNumber;
	 private String tenderNumber;
	 private String acknowledgementNumber;
	 
	 @JsonFormat(pattern="dd/MM/yyyy")
	 private Date fromDate;
	 
	 @JsonFormat(pattern="dd/MM/yyyy")
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
	 private Long doorno;
	 private String allotteeName;
	 private Long mobileNumber;
	 private String offSet;
	 private String size;
	 private String stateId;
	 private Set<Long> asset;
	 private Set<Long> allottee;
}
