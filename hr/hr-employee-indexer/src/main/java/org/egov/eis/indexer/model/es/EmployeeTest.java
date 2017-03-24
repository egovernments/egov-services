package org.egov.eis.indexer.model.es;

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
public class EmployeeTest {

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

	@JsonProperty("testname")
	private String testName;

	@JsonProperty("testpassedyear")
	private String testPassedYear;

	@JsonProperty("testremarks")
	private String testRemarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("testcreateddate")
	private Date testCreatedDate;

	@JsonProperty("testcreatedby")
	private String testCreatedBy;
	
}
