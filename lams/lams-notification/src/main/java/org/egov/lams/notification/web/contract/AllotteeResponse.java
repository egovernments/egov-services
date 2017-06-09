package org.egov.lams.notification.web.contract;
import java.util.List;

import org.egov.lams.notification.model.Allottee;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
public class AllotteeResponse {
	
	 @JsonProperty("ResponseInfo")
	  private ResponseInfo responseInfo;

	  @JsonProperty("user")
	  private List<Allottee> allottees;
}
