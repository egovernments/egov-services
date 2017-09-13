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
public class Remission {

	@JsonProperty("remissionReason")
	private String remissionReason;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("fromDate")
	private Date fromDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("toDate")
	private Date toDate;

	@JsonProperty("remissionOrder")
	private String remissionOrder;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("remissionDate")
	private Date remissionDate;

	@JsonProperty("remissionRent")
	private Double remissionRent;
}
