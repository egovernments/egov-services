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

	@JsonProperty("employeeid")
	private long employeeId;

	@JsonProperty("employeecode")
	private String employeeCode;

	@JsonProperty("serviceid")
	private int serviceId;

	@JsonProperty("serviceentry")
	private String serviceEntry;

	@JsonProperty("servicefrom")
	private Date serviceFrom;

	@JsonProperty("serviceremarks")
	private String serviceRemarks;

	@JsonProperty("serviceorderno")
	private String serviceOrderNo;

	@JsonProperty("servicecreateddate")
	private Date serviceCreatedDate;
	
	@JsonProperty("servicecreatedby")
	private String serviceCreatedBy;

}

