package org.egov.lams.notification.models;

import java.util.Date;

import org.egov.lams.notification.model.enums.ReasonForCancellation;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cancellation {

	@JsonProperty("reasonForCancellation")
	private ReasonForCancellation reasonForCancellation;

	@JsonProperty("terminationDate")
	private Date terminationDate;

	@JsonProperty("orderNo")
	private String orderNo;

	@JsonProperty("orderDate")
	private Date orderDate;
}
