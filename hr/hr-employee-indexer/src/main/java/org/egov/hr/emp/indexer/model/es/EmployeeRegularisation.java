package org.egov.hr.emp.indexer.model.es;

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
public class EmployeeRegularisation {

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
	
	@JsonProperty("regularisationdesignation")
	private String regularisationDesignation;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("regularisationdeclareddate")
	private Date regularisationDeclaredDate;
	
	@JsonProperty("regularisationordernumber")
	private String regularisationOrderNumber;

	@JsonProperty("regularisationremarks")
	private String regularisationRemarks;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("regularisationorderdate")
	private Date regularisationOrderDate;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("regularisationcreateddate")
	private Date regularisationCreatedDate;

	@JsonProperty("regularisationcreatedby")
	private String regularisationCreatedBy;
	
}
