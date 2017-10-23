package org.egov.lams.common.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * A Object holds the data for a unit details of Floor
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UnitDetail {
    private Long id = null;

    @NotNull
    private FloorDetail floor = null;

    @NotNull
    @Size(min = 1, max = 64)
    private String usageType = null;

    private String previousUnitNo = null;

    @NotNull
    private Double builtUpArea;

    @NotNull
    @Size(min = 1, max = 64)
    private String holdingType = null;

    @Size(max = 64)
    private String departmentName = null;

    @Size(max = 512)
    private String description = null;
}

