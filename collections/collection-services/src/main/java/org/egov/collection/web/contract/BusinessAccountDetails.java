package org.egov.collection.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@ToString

public class BusinessAccountDetails {
	private Long id;

	private BusinessDetails businessDetails;

	private Long chartOfAccounts;

	private Double amount;
}