package org.egov.tradelicense.web.contract;

import java.util.ArrayList;
import java.util.Date;
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
    
    @NotNull
    private String tenantId;

    private Long licenseId;
    
    private String applicationNumber;
    
    private String mobileNumber;
    
    private String tradeTitle;
    
    private String ward;
    
    private String tradeLicenseNumber;
    
    private String applicationStatus;
    
    private String applicationType;
    
    private String documentType;
    
    private Long dateFrom;
    
    private Long dateTo;

    private String documentName;

    private String fileStoreId;
    
    private String ownerName;
    
    private String status;

    @Min(1)
    @Max(500)
    private Short pageSize;

    private Short pageNumber;

    private String sortBy;

    private String sortOrder;
}
