package org.egov.lams.notification.models;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Renewal {

	@JsonProperty("renewalOrderNo")
	private String renewalOrderNo;

	@JsonProperty("renewalOrderDate")
	private Date renewalOrderDate;

	@JsonProperty("reasonForRenewal")
	private Date reasonForRenewal;
}
