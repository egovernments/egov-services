package entities.responses.pgrCollections.SearchComplaint;

public class SearchComplaintResponse {
    private ServiceRequests[] serviceRequests;
    private Object responseInfo;

    public ServiceRequests[] getServiceRequests() {
        return this.serviceRequests;
    }

    public void setServiceRequests(ServiceRequests[] serviceRequests) {
        this.serviceRequests = serviceRequests;
    }

    public Object getResponseInfo() {
        return this.responseInfo;
    }

    public void setResponseInfo(Object responseInfo) {
        this.responseInfo = responseInfo;
    }
}
