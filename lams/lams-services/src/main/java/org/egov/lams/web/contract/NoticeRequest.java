package org.egov.lams.web.contract;

import lombok.*;
import org.egov.lams.model.Notice;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

/**
 * NoticeRequest
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NoticeRequest   {
	
  @JsonProperty("RequestInfo")
  private RequestInfo requestInfo = null;
  
  @JsonProperty("Notice")
  @Valid
  private Notice notice;
 
}

