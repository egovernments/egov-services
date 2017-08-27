package org.egov.wcms.web.contract;


import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GapcodeGetRequest {
	
	private Long id;

    @Length(min = 3, max = 100)
    private String name;

    private String code;

    private Boolean active;

    @NotNull
    private String tenantId;

    private String sortBy;

    private String sortOrder;

    @Min(1)
    @Max(500)
    private Short pageSize;

    private Short pageNumber;

}
