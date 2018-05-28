package org.egov.asset.model;

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
public class Document {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("asset")
    private Long asset;

    @JsonProperty("fileStore")
    private String fileStore;

    @JsonProperty("tenantId")
    private String tenantId;
}
