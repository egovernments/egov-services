package entities.requests.pgrCollections.createComplaint;

public class ComplaintRequest {

    public entities.requests.pgrCollections.createComplaint.RequestInfo RequestInfo;

    public entities.requests.pgrCollections.createComplaint.ServiceRequest ServiceRequest;

    public RequestInfo getRequestInfo() {
        return RequestInfo;
    }

    public void setRequestInfo(RequestInfo RequestInfo) {
        this.RequestInfo = RequestInfo;
    }

    public ServiceRequest getServiceRequest() {
        return ServiceRequest;
    }

    public void setServiceRequest(ServiceRequest ServiceRequest) {
        this.ServiceRequest = ServiceRequest;
    }

}

