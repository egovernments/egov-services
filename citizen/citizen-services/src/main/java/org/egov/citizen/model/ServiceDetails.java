package org.egov.citizen.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDetails {
	
  private long id; 
  private String seq_number;
  private String tenantid;
  private String serviceCode;
  private String consumerCode;
  private String email;
  private String mobileNumber;
  private long createdby;
  private String assignedto;
  private Date createddate;
  private String json;

}
