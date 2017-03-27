package org.egov.lams.contract;

import java.util.List;

import org.egov.lams.model.Agreement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AgreementResponse {

	@JsonProperty("ResposneInfo")
	private ResponseInfo resposneInfo;
	
	@JsonProperty("Agreements")
	private List<Agreement> agreement;
}
