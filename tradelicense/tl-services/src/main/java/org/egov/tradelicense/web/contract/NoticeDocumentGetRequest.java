package org.egov.tradelicense.web.contract;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDocumentGetRequest {

    private List<Long> ids = new ArrayList<Long>();

    private Long licenseId;

    @NotNull
    private String tenantId;

    private String documentName;

    private String fileStoreId;

    @Min(1)
    @Max(500)
    private Short pageSize;

    private Short pageNumber;

    private String sortBy;

    private String sortOrder;
}
