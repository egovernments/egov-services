package org.egov.collection.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class BusinessAccountSubLedger {

	private Long id;

	private Long detailType;

	private Long detailKey;

	private Double amount;

	private BusinessAccountDetails businessAccountDetails;

}
