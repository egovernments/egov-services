package builders.pgrCollection.createComplaint;

import entities.requests.pgrCollections.createComplaint.ComplaintRequest;
import entities.requests.pgrCollections.createComplaint.RequestInfo;
import entities.requests.pgrCollections.createComplaint.ServiceRequest;

public final class ComplaintRequestBuilder {

    ComplaintRequest complaintRequest = new ComplaintRequest();

    ServiceRequest serviceRequest = new ServiceRequestBuilder().build();
    RequestInfo requestInfo = new RequestInfoBuilder().build();

    ServiceRequest serviceRequest1 = new ServiceRequestBuilder("update").build();
    RequestInfo requestInfo1 = new RequestInfoBuilder("update").build();

    public ComplaintRequestBuilder() {
        complaintRequest.setServiceRequest(serviceRequest);
        complaintRequest.setRequestInfo(requestInfo);
    }

    public ComplaintRequestBuilder(String update) {
        complaintRequest.setServiceRequest(serviceRequest1);
        complaintRequest.setRequestInfo(requestInfo1);
    }

    public ComplaintRequestBuilder withRequestInfo(RequestInfo RequestInfo) {
        complaintRequest.RequestInfo = RequestInfo;
        return this;
    }

    public ComplaintRequestBuilder withServiceRequest(ServiceRequest ServiceRequest) {
        complaintRequest.ServiceRequest = ServiceRequest;
        return this;
    }

    public ComplaintRequest build() {
        return complaintRequest;
    }
}
