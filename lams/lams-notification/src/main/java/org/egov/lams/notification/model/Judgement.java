package org.egov.lams.notification.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Judgement {

	@JsonProperty("judgementNo")
	private String judgementNo;

	@JsonProperty("judgementDate")
	private Date judgementDate;

	@JsonProperty("judgementRent")
	private Double judgementRent;

	@JsonProperty("effectiveDate")
	private Date effectiveDate;
}
