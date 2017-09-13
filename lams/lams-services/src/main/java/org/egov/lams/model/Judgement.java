package org.egov.lams.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("judgementDate")
	private Date judgementDate;

	@JsonProperty("judgementRent")
	private Double judgementRent;

	@JsonFormat(pattern="dd/MM/yyyy")
	@JsonProperty("effectiveDate")
	private Date effectiveDate;
}
