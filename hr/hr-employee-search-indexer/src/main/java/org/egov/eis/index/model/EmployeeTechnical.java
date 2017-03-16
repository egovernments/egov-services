package org.egov.eis.index.model;

import java.util.Date;



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
public class EmployeeTechnical {

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

	@JsonProperty("technicalskill")
	private String technicalSkill;

	@JsonProperty("technicalyear")
	private String technicalYear;

	@JsonProperty("technicalgrade")
	private String technicalGrade;

	@JsonProperty("technicalremarks")
	private String technicalRemarks;

	@JsonProperty("technicalcreateddate")
	private Date technicalCreatedDate;

	@JsonProperty("technicalcreatedby")
	private String technicalCreatedBy;

}
