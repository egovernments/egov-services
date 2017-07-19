package org.egov.asset.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetConfigurationCriteria {

    private List<Long> id;

    @Size(min = 3, max = 50)
    private String name;

    private Long effectiveFrom;

    @NotNull
    private String tenantId;

}
