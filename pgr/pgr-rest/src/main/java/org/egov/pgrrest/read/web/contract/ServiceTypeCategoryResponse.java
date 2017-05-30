package org.egov.pgrrest.read.web.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.egov.common.contract.response.ResponseInfo;

import java.util.List;

@Getter
@AllArgsConstructor
public class ServiceTypeCategoryResponse {

    private ResponseInfo responseInfo;

    private List<ServiceTypeCategory> serviceTypeCategories;

}
