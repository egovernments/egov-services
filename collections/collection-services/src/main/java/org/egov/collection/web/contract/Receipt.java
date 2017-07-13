package org.egov.collection.web.contract;


import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.WorkflowDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class Receipt   {

  private String tenantId;
  
  private String instrumentType;

  private String instrumentHeader;
    
  @JsonProperty("BillWrapper")
  private BillWrapper billInfoWrapper; //for collection-service

  @JsonProperty("Bank")
  private Bank bank;
  
  @JsonProperty("BankAccount")
  private BankAccount bankAccount;  
  
  private AuditDetails auditDetails;
  
  transient private WorkflowDetails workflowDetails;

  }

