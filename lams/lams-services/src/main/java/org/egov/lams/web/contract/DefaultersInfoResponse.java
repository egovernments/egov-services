package org.egov.lams.web.contract;

import java.util.HashSet;
import java.util.Set;

import org.egov.lams.model.DefaultersInfo;

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
public class DefaultersInfoResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo;

	@JsonProperty("Defaulters")
	private Set<DefaultersInfo> defaulters = new HashSet<>();

}