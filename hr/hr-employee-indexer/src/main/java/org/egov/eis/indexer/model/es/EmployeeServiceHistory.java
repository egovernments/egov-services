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
public class EmployeeServiceHistory {

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

	@JsonProperty("serviceid")
	private Long serviceId;

	@JsonProperty("employeeid")
	private Long employeeId;

	@JsonProperty("employeecode")
	private String employeeCode;

	@JsonProperty("serviceentry")
	private String serviceEntry;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("servicefrom")
	private Date serviceFrom;

	@JsonProperty("serviceremarks")
	private String serviceRemarks;

	@JsonProperty("serviceorderno")
	private String serviceOrderNo;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("servicecreateddate")
	private Date serviceCreatedDate;
	
	@JsonProperty("servicecreatedby")
	private String serviceCreatedBy;

}

