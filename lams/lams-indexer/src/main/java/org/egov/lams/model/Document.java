package org.egov.lams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Document   {
	
  @JsonProperty("id")
  private Long id;

  @JsonProperty("documentType")
  private DocumentType documentType;

  @JsonProperty("agreement")
  private Long agreement;

  @JsonProperty("fileStore")
  private String fileStore;
  
  private String fileName;
  private String tenantId;
}

