package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BillAccountDetailsWrapper {

	@JsonProperty("BillAccountDetail")
	private BillAccountDetail billAccountDetails;
}
