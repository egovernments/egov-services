package io.swagger.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
