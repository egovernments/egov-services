package org.egov.egf.domain.model.contract;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    private Long id = null;

    private String userName = null;

    private String name = null;

    private Set<Role> roles = null;

}
