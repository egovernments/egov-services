package org.egov.collection.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.joda.time.LocalDate;

@Setter
@Getter
@ToString
public class Receipt   {

  private String tenantId;

  private String event;

  private String receiptNumber;

  private LocalDate receiptDate;

  private String channel;
  
  private String voucherHeader;
  
  private String fundSource;

  private String instrumentType;

  private String instrumentHeader;
  
  private String department;

  private String boundary;

  private String reasonForCancellation;

  @JsonProperty("BillInfo")
  private BillInfo billInfo;
  
  @JsonProperty("Bank")
  private Bank bank;
  
  @JsonProperty("BankAccount")
  private BankAccount bankAccount;
  
  @JsonProperty("Fund")
  private Fund fund;
  
  @JsonProperty("Function")
  private Function function;
  
  

  }

