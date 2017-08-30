package org.egov.citizen.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServiceReq {

	private String tenantId;	
	private String serviceRequestId;
	private String serviceCode;
	private Integer lat;
	private Integer lang;	
	private String address;	 
	private String addressId;	
	private String email;
	private String deviceId;	
	private String accountId;
	private String firstName;
	private String lastName;
	private String phone;
	private String description;
    private List<Value>	attributeValues;
	private String assignedTo;
	private Object backendServiceDetails;
	private AuditDetails auditDetails;
	private String action;
	private String consumerCode;
	private List<Comment> comments;	
	private List<Document> documents;	
	private String status;	
	private Object moduleObject;
	private String moduleStatus;	
	private BigDecimal additionalFee;


}
