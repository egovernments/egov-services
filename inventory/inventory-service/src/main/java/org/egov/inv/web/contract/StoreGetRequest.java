package org.egov.inv.web.contract;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreGetRequest {

    private List<String> ids;

    private String code;

    private String name;

    private String description;

    private String department;

    private String contactNo1;

    private String email;

    private Boolean isCentralStore;

    private String storeInCharge;

    private Boolean active;

    private String sortBy;

    private Integer pageSize;

    private Integer offset;

    @NotNull
    private String tenantId;
}
