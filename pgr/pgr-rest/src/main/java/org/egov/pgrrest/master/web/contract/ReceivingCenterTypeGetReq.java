package org.egov.pgrrest.master.web.contract;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ReceivingCenterTypeGetReq {
	
	private List<Long> id;

    @Length(min = 3, max = 100)
    private String name;

    private String code;
    
    private String description;

    private Boolean active;
    
    private Boolean visible;

    @NotNull
    private String tenantId;

    private String sortBy;

    private String sortOrder;

    @Min(1)
    @Max(500)
    private Short pageSize;

    private Short pageNumber;

}
