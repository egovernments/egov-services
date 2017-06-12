package builders.eGovEIS.hrMaster.designation.search;

import entities.requests.eGovEIS.hrMaster.RequestInfo;
import entities.requests.eGovEIS.hrMaster.designation.search.HRMasterDesignationSearchRequest;

public class HRMasterDesignationSearchRequestBuilder {

    HRMasterDesignationSearchRequest hRMasterDesignationSearchRequest = new HRMasterDesignationSearchRequest();
    RequestInfo requestInfo = new RequestInfo();

    public HRMasterDesignationSearchRequestBuilder() {
        hRMasterDesignationSearchRequest.setRequestInfo(requestInfo);
    }

    public HRMasterDesignationSearchRequestBuilder withRequestInfo(RequestInfo requestInfo) {
        hRMasterDesignationSearchRequest.setRequestInfo(requestInfo);
        return this;
    }

    public HRMasterDesignationSearchRequest build() {
        return hRMasterDesignationSearchRequest;
    }
}
