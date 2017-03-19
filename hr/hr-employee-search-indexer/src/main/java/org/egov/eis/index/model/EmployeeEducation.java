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
public class EmployeeEducation {

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

	@JsonProperty("qualification")
	private String qualification;

	@JsonProperty("qualificationyear")
	private String qualificationYear;

	@JsonProperty("qualificationuniversity")
	private String qualificationUniversity;

	@JsonProperty("qualificationremarks")
	private String qualificationRemarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("qualificationcreateddate")
	private Date qualificationCreatedDate;

	@JsonProperty("qualificationcreatedby")
	private String qualificationCreatedBy;

}
