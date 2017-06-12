package builders.eGovEIS.hrMaster.designation.create;

import entities.requests.eGovEIS.hrMaster.RequestInfo;
import entities.requests.eGovEIS.hrMaster.designation.create.Designation;
import entities.requests.eGovEIS.hrMaster.designation.create.HRMasterDesignationCreateRequest;

public class HRMasterDesignationCreateRequestBuilder {

    HRMasterDesignationCreateRequest hRMasterDesignationCreateRequest = new HRMasterDesignationCreateRequest();
    Designation designation = new Designation();
    RequestInfo requestInfo = new RequestInfo();

    public HRMasterDesignationCreateRequestBuilder() {
        hRMasterDesignationCreateRequest.setDesignation(designation);
        hRMasterDesignationCreateRequest.setRequestInfo(requestInfo);
    }

    public HRMasterDesignationCreateRequestBuilder withDesignation(Designation Designation) {
        hRMasterDesignationCreateRequest.setDesignation(Designation);
        return this;
    }

    public HRMasterDesignationCreateRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        hRMasterDesignationCreateRequest.setRequestInfo(RequestInfo);
        return this;
    }

    public HRMasterDesignationCreateRequest build() {
        return hRMasterDesignationCreateRequest;
    }
}
