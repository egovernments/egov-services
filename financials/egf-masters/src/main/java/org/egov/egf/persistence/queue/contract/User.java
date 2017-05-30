package org.egov.egf.persistence.queue.contract;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("userName")
	private String userName = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("roles")
	private Set<Role> roles = null;

}
