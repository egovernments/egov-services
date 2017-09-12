package org.egov.asset.model;

import javax.validation.constraints.Size;

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
public class Fund {

    @JsonProperty("id")
    private Long id;

    @Size(max = 50, min = 2)
    @JsonProperty("name")
    private String name;

    @Size(max = 50, min = 2)
    @JsonProperty("code")
    private String code;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("level")
    private Long level;

    @JsonProperty("parentId")
    private Long parentId;

    @JsonProperty("active")
    private Boolean active;

}
