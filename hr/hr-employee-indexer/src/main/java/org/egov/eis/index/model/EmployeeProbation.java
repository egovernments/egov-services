package org.egov.eis.index.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class EmployeeProbation {

	@JsonProperty("ulbname")
	private String ulbName;

	@JsonProperty("ulbcode")
	private String ulbCode;

	@JsonProperty("distname")
	private String distName;

	@JsonProperty("regname")
	private String regName;

	@JsonProperty("ulbgrade")
	private String ulbGrade;

	@JsonProperty("employeeid")
	private long employeeId;

	@JsonProperty("employeecode")
	private String employeeCode;

	@JsonProperty("probationdesignation")
	private String probationDesignation;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("probationdeclareddate")
	private Date probationDeclareDdate;

	@JsonProperty("probationordernumber")
	private String probationOrderNumber;

	@JsonProperty("probationremarks")
	private String probationRemarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("probationorderdate")
	private Date probationOrderDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("probationcreateddate")
	private Date probationCreatedDate;

	@JsonProperty("probationcreatedby")
	private String probationCreatedBy;

}
