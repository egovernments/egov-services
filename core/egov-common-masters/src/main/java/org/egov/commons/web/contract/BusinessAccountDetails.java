package org.egov.commons.web.contract;





import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class BusinessAccountDetails {
	private Long id;
	
	private BusinessDetails businessDetails;

	private Long chartOfAccounts;

	private Double amount;
}