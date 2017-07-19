package org.egov.collection.web.contract;


import javax.validation.constraints.NotNull;

import org.egov.collection.model.AuditDetails;
import org.egov.collection.model.WorkflowDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class Receipt   {

  private String tenantId;
  
  private String instrumentType;

  private String instrumentHeader;
  
  @NotNull
  @JsonProperty("Bill")
  private Bill bill;

  @JsonProperty("Bank")
  private Bank bank;
  
  @NotNull
  @JsonProperty("BankAccount")
  private BankAccount bankAccount;  
  
  private AuditDetails auditDetails;
  
  transient private WorkflowDetails workflowDetails;

  }

