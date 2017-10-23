package org.egov.lams.common.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by ramki on 23/10/17.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class HierarchyType {
    @NotNull
    private Long id;

    @NotNull
    @Size(max = 128)
    private String name;

    @NotNull
    @Size(max = 50)
    private String code;

    @Size(max = 256)
    private String localName;

    private String tenantId;
}
